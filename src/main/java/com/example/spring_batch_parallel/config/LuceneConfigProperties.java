package com.example.spring_batch_parallel.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "lucene")
public class LuceneConfigProperties {
    private String indexDir;
    private Config config;

    @Getter
    @Setter
    static class Config {
        private double ramBuffer;
        private IndexWriterConfig.OpenMode openMode;
        private boolean enableCompoundFile;
    }
}
