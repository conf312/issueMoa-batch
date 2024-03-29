package com.issuemoa.batch.job.configs;

import com.issuemoa.batch.job.tasklets.TaskletNaverNewsRank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobNaverNewsRankConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TaskletNaverNewsRank taskletNaverNewsRank;

    @Bean
    public Job jobNaverNewsRank() {
        return jobBuilderFactory.get("jobNaverNewsRank")
            .start(stepNaverNewsRank())
            .build();
    }

    @Bean
    public Step stepNaverNewsRank() {
        DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_NEVER);
        return stepBuilderFactory.get("stepNaverNewsRank").tasklet(taskletNaverNewsRank).transactionAttribute(transactionAttribute).build();
    }
}
