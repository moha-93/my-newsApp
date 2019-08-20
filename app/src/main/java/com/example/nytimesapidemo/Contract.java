package com.example.nytimesapidemo;

import android.provider.BaseColumns;

public class Contract {

    public static class ArticleTable implements BaseColumns {

        public static final String TABLE_NAME = "articles_content";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_PUBLISHED_DATE = "published_date";
        public static final String COLUMN_IS_FAVORITE = "favorite";

    }
}
