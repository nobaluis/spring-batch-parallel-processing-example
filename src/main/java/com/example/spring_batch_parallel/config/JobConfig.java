package com.example.spring_batch_parallel.config;

import com.example.spring_batch_parallel.job.LuceneItemProcessor;
import com.example.spring_batch_parallel.job.LuceneItemWriter;
import com.example.spring_batch_parallel.job.LuceneJobListener;
import com.example.spring_batch_parallel.model.Book;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.concurrent.Future;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    @Value("${job.input-data-dir}")
    private String inputDataDir;

    @Value("${job.chunk-size}")
    private int chunkSize;

    private final LuceneItemWriter luceneItemWriter;
    private final LuceneJobListener luceneJobListener;

@Bean
public Job indexingJob(JobRepository jobRepository, Step indexingStep){
    return new JobBuilder("indexingJob", jobRepository)
            .start(indexingStep)
            .listener(luceneJobListener)
            .build();
}

@Bean
public Step indexingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
    return new StepBuilder("indexingStep", jobRepository)
            .<Book, Future<Document>>chunk(chunkSize, transactionManager)
            .reader(itemReader())
            .processor(itemProcessor())
            .writer(itemWriter())
            .build();
}

    @Bean
    public ItemReader<Book> itemReader() {
        return new FlatFileItemReaderBuilder<Book>()
                .name("csvBookReader")
                .resource(new FileSystemResource(inputDataDir))
                .linesToSkip(1)
                .delimited()
                .names("bookId", "goodReadsBookId", "bestBookId", "workId", "booksCount",
                        "isbn", "isbn13", "authors", "originalPubYear", "originalTitle",
                        "title", "langEncode", "averageRating", "ratingsCount",
                        "workRatingsCount", "workTextReviewsCount", "ratings1", "ratings2",
                        "ratings3", "ratings4", "ratings5", "imageUrl", "smallImageUrl")
                .fieldSetMapper(new RecordFieldSetMapper<>(Book.class))
                .build();
    }

    @Bean
    public AsyncItemProcessor<Book, Document> itemProcessor() {
        var processor = new AsyncItemProcessor<Book, Document>();
        processor.setDelegate(new LuceneItemProcessor());
        processor.setTaskExecutor(asyncTaskExecutor());
        return processor;
    }

    @Bean
    public AsyncItemWriter<Document> itemWriter() {
        var writer = new AsyncItemWriter<Document>();
        writer.setDelegate(luceneItemWriter);
        return writer;
    }

    @Bean
    public TaskExecutor asyncTaskExecutor() {
        var executor = new SimpleAsyncTaskExecutor("async-");
        executor.setVirtualThreads(true);
        return executor;
    }

}
