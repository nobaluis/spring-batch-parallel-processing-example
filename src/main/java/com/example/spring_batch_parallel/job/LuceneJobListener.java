package com.example.spring_batch_parallel.job;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.index.IndexWriter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LuceneJobListener implements JobExecutionListener {

    private final IndexWriter indexWriter;

    @Override
    public void afterJob(JobExecution jobExecution){
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
