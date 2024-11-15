package com.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public JobExecutionListener productStockUpdateJobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("Starting job: {}", jobExecution.getJobInstance().getJobName());
                log.info("Start time: {}", jobExecution.getStartTime());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("Finished job: {}", jobExecution.getJobInstance().getJobName());
                log.info("End time: {}", jobExecution.getEndTime());
                log.info("Job Status: {}", jobExecution.getStatus());

                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("Job successful");
                } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    log.error("Job failed with exceptions:");
                    jobExecution.getAllFailureExceptions().forEach(e -> log.error("Exception: ", e));
                }
            }
        };
    }

}