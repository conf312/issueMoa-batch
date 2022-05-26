package com.issuemoa.batch.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuemoa.batch.domain.board.Board;
import com.issuemoa.batch.domain.board.BoardRepository;
import com.issuemoa.batch.util.HttpConnectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.persistence.EntityManagerFactory;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final HttpConnectionUtil httpConnectionUtil;
    private final EntityManagerFactory entityManagerFactory;
    private final BoardRepository boardRepository;

    @Value("${globals.naver.endpoint.news}")
    private String endpointNews;

    @Bean
    public Job jobPullNaverNews() throws Exception {
        return jobBuilderFactory.get("jobPullNaverNews")
                .start(StepPullNaverNews())
                .build();
    }

    @Bean
    public Step StepPullNaverNews() throws Exception {
        return stepBuilderFactory.get("stepPullNaverNews")
            .tasklet((contribution, chunkContext) -> {

            log.info("==> stepPullNaverNews");

            String query = URLEncoder.encode("주식", "UTF-8");
            HashMap<String, Object> result = httpConnectionUtil.getDataFromUrlJson("naver",
                    endpointNews + "?query=" + query, "", "UTF-8", false, "application/json");

            ArrayList<HashMap<String, String>> items = (ArrayList<HashMap<String, String>>) result.get("items");

            ArrayList<Board> saveData = new ArrayList<>();

            for (HashMap<String, String> br : items) {
                saveData.add(new Board("NEWS", "Y", br.get("title"), br.get("description")));
            }

            boardRepository.saveAll(saveData);

            return RepeatStatus.FINISHED;
        })
        .build();

            // Example Chunk
//            return stepBuilderFactory.get("stepPullNaverNews")
//                .<Board, Board> chunk(10)
//                .reader(reader(null))
//                .processor(processor(null))
//                .writer(writer(null))
//                .build();
    }

//    @Bean
//    @StepScope
//    public JpaPagingItemReader<Board> reader(String requestDate) throws Exception {
//        log.info("==> reader value : " + requestDate);
//
//        Map<String, Object> parameterValues = new HashMap<>();
//        parameterValues.put("price", 1000);
//
//        return new JpaPagingItemReaderBuilder<Board>()
//                .pageSize(10)
//                .parameterValues(parameterValues)
//                .queryString("SELECT m FROM Board m WHERE m.price >= : price")
//                .entityManagerFactory(entityManagerFactory)
//                .name("JpaPagingItemReader")
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<Board, Board> processor(String requestDate) {
//        return new ItemProcessor <Board, Board> () {
//            @Override
//            public Board process(Board Board) throws Exception {
//                log.info("==> processor Board : " + Board);
//                log.info("==> processor value : " + requestDate);
//
//                // 100원 추가
//                //Board.setPrice(Board.getPrice() + 100);
//                return Board;
//            }
//        };
//    }
//
//    @Bean
//    @StepScope
//    public JpaItemWriter<Board> writer(String requestDate) {
//        log.info("==> writer value : " + requestDate);
//        return new JpaItemWriterBuilder<Board>()
//                .entityManagerFactory(entityManagerFactory)
//                .build();
//    }
}
