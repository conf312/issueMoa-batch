package com.issuemoa.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Component
public class IssueMoaBatchSchduler {

    private final Job jobPullNaverNews;
    private final JobLauncher jobLauncher;

    //@Scheduled(cron = "0 0 12 * * *") // everyday 12:00 pm
    @Scheduled(fixedDelay = 6000)
    public void pullNaverNews() {
        log.info("==> schedulerPullNaverNews Call");

        Map<String, JobParameter> jobParameterMap = new HashMap<>();
        jobParameterMap.put("requestDate", new JobParameter(String.valueOf(LocalDate.now())));
        JobParameters jobParameters = new JobParameters(jobParameterMap);

        try {
            jobLauncher.run(jobPullNaverNews, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
