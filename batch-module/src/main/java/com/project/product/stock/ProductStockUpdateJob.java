package com.project.product.stock;

import com.project.domain.products.StockLogs;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ProductStockUpdateJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<Long> stockLogReader;
    private final ItemProcessor<Long, Map.Entry<Long, List<StockLogs>>> productStockProcessor;
    private final ItemWriter<Map.Entry<Long, List<StockLogs>>> productWriter;

    private final JobExecutionListener jobExecutionListener;

    @Bean
    public Job productStockUpdateJob() {
        JobBuilder jobBuilder = new JobBuilder("productStockUpdateJob", jobRepository)
                .listener(jobExecutionListener)
                .incrementer(new RunIdIncrementer());

        return jobBuilder.start(productStockUpdateStep()).build();
    }

    @Bean
    public Step productStockUpdateStep() {
        StepBuilder stepBuilder = new StepBuilder("productStockUpdateStep", jobRepository);

        return stepBuilder
                .<Long, Map.Entry<Long, List<StockLogs>>>chunk(10)
                .reader(stockLogReader)
                .processor(productStockProcessor)
                .writer(productWriter)
                .build();
    }
}
