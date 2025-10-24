package com.example.spring_batch_parallel.config;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class LuceneConfig {
    private final LuceneConfigProperties luceneConfig;

    @Bean
    public Directory luceneDir() throws IOException {
        return FSDirectory.open(Paths.get(luceneConfig.getIndexDir()));
    }

    @Bean
    public IndexWriter luceneIndexWriter() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        writerConfig.setRAMBufferSizeMB(luceneConfig.getConfig().getRamBuffer());
        writerConfig.setOpenMode(luceneConfig.getConfig().getOpenMode());
        writerConfig.setUseCompoundFile(luceneConfig.getConfig().isEnableCompoundFile());
        return new IndexWriter(luceneDir(), writerConfig);
    }
}
