package com.project.shipping.preparation;

import com.project.domain.order.Orders;
import com.project.domain.payment.Payments;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
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
public class ShippingPreparationJob {
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final CompletedPaymentReader completedPaymentReader;
    private final OrderProcessor orderProcessor;
    private final OrderWriter orderWriter;
    private final PlatformTransactionManager transactionManager;

    @Scheduled(cron = "0 */30 * * * *")
    public void performJob() throws Exception {
        jobLauncher.run(updateOrderStatusJob(), new JobParametersBuilder().toJobParameters());

    }

    @Bean
    public Job updateOrderStatusJob() {
        return new JobBuilder("updateOrderStatusJob", jobRepository)
                .start(updateOrderStatusStep())
                .build();
    }

    @Bean
    public Step updateOrderStatusStep() {
        return new StepBuilder("updateOrderStatusStep", jobRepository)
                .<Payments, Orders>chunk(10, transactionManager)
                .reader(completedPaymentReader)
                .processor(orderProcessor)
                .writer(orderWriter)
                .build();
    }

}
