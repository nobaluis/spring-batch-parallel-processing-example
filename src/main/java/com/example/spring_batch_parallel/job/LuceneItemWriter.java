package com.example.spring_batch_parallel.job;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LuceneItemWriter implements ItemWriter<Document> {

    private final IndexWriter indexWriter;

    @Override
    public void write(Chunk<? extends Document> chunk) throws Exception {
        List<Document> docs = new ArrayList<>(chunk.getItems());
        indexWriter.addDocuments(docs);
    }
}
