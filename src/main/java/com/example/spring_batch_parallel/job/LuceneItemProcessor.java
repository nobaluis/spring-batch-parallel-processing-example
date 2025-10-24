package com.example.spring_batch_parallel.job;

import com.example.spring_batch_parallel.model.Book;
import org.apache.lucene.document.*;
import org.springframework.batch.item.ItemProcessor;

public class LuceneItemProcessor implements ItemProcessor<Book, Document> {

    @Override
    public Document process(Book item) {
        if (item == null) {
            return null;
        }

        Document doc = new Document();
        doc.add(new StringField("book_id", String.valueOf(item.bookId()), Field.Store.YES));

        if (item.isbn() != null && !item.isbn().isEmpty()) {
            doc.add(new StringField("isbn", item.isbn(), Field.Store.YES));
        }

        if (item.langEncode() != null && !item.langEncode().isBlank()) {
            doc.add(new StringField("language_code", item.langEncode(), Field.Store.YES));
        }

        if (item.title() != null && !item.title().isBlank()) {
            doc.add(new TextField("title", item.title(), Field.Store.YES));
        }

        if (item.authors() != null && !item.authors().isBlank()) {
            doc.add(new TextField("authors", item.authors(), Field.Store.YES));
        }

        if (item.originalTitle() != null && !item.originalTitle().isBlank()) {
            doc.add(new TextField("original_title", item.originalTitle(), Field.Store.NO));
        }

        doc.add(new LongPoint("goodreads_book_id", item.goodReadsBookId()));
        doc.add(new StoredField("goodreads_book_id", item.goodReadsBookId()));

        doc.add(new IntPoint("ratings_count", item.ratingsCount()));
        doc.add(new StoredField("ratings_count", item.ratingsCount()));

        doc.add(new DoublePoint("average_rating", item.averageRating()));
        doc.add(new StoredField("average_rating", item.averageRating()));

        if (item.originalPubYear() != null) {
            doc.add(new DoublePoint("publication_year", item.originalPubYear()));
            doc.add(new StoredField("publication_year", item.originalPubYear()));
        }

        if (item.imageUrl() != null && !item.imageUrl().isBlank()) {
            doc.add(new StoredField("image_url", item.imageUrl()));
        }
        if (item.smallImageUrl() != null && !item.smallImageUrl().isBlank()) {
            doc.add(new StoredField("small_image_url", item.smallImageUrl()));
        }

        return doc;
    }
}
