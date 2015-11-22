package com.ext.techapp.thirukkural.db;

import android.provider.BaseColumns;

/**
 * Created by Selvam on 11/22/2015.
 */
public final class Favorites {

    public Favorites(){};

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_COUPLET_ID = "couplet";
        public static final String COLUMN_CHAPTER_ID = "chapter";

    }
}
