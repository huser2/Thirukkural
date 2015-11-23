package com.ext.techapp.thirukkural.db;

/**
 * Created by Selvam on 11/22/2015.
 */
public class Favorite {

    public long getCOLUMN_COUPLET_ID() {
        return COLUMN_COUPLET_ID;
    }

    public void setCOLUMN_COUPLET_ID(long COLUMN_COUPLET_ID) {
        this.COLUMN_COUPLET_ID = COLUMN_COUPLET_ID;
    }

    public long getCOLUMN_CHAPTER_ID() {
        return COLUMN_CHAPTER_ID;
    }

    public void setCOLUMN_CHAPTER_ID(long COLUMN_CHAPTER_ID) {
        this.COLUMN_CHAPTER_ID = COLUMN_CHAPTER_ID;
    }

    private    long COLUMN_COUPLET_ID;
    private    long COLUMN_CHAPTER_ID;

    @Override
    public String toString() {
        return getCOLUMN_CHAPTER_ID()+":"+getCOLUMN_COUPLET_ID();
    }
}
