package com.project.order.confirm;

import com.project.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ConfirmPurchaseJob {
    private final JobRepository jobRepository;

    private final JobLauncher jobLauncher;

    private final PlatformTransactionManager transactionManager;

    private final ConfirmPurchaseReader confirmPurchaseReader;

    private final ConfirmPurchaseProcessor confirmPurchaseProcessor;

    private final ConfirmPurchaseWriter confirmPurchaseWriter;


    @Scheduled(cron = "0 0 * * * ?")
    public void runPurchaseConfirmationJob() throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(orderAutoConfirmationJob(), jobParameters);
    }

    @Bean
    public Job orderAutoConfirmationJob() {
        return new JobBuilder("orderAutoConfirmationJob", jobRepository)
                .start(orderAutoConfirmationStep())
                .build();
    }

    @Bean
    public Step orderAutoConfirmationStep() {
        return new StepBuilder("orderAutoConfirmationStep", jobRepository)
                .<Orders, Orders>chunk(10, transactionManager)
                .reader(confirmPurchaseReader)
                .processor(confirmPurchaseProcessor)
                .writer(confirmPurchaseWriter)
                .build();
    }
}
