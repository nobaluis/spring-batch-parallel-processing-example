package com.example.spring_batch_parallel.model;

public record Book(
        long bookId,
        long goodReadsBookId,
        long bestBookId,
        long workId,
        int booksCount,
        String isbn,
        String isbn13,
        String authors,
        Double originalPubYear,
        String originalTitle,
        String title,
        String langEncode,
        double averageRating,
        int ratingsCount,
        int workRatingsCount,
        int workTextReviewsCount,
        int ratings1,
        int ratings2,
        int ratings3,
        int ratings4,
        int ratings5,
        String imageUrl,
        String smallImageUrl
) {
}
