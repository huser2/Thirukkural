package com.ext.techapp.thirukkural.db;

/**
 * Created by Selvam on 11/22/2015.
 */
public class Favorite {
    private    long COLUMN_NAME_ENTRY_ID;
    private    String COLUMN_COUPLET_ID;
    private    String COLUMN_CHAPTER_ID;

    public long getCOLUMN_NAME_ENTRY_ID() {
        return COLUMN_NAME_ENTRY_ID;
    }

    public void setCOLUMN_NAME_ENTRY_ID(long COLUMN_NAME_ENTRY_ID) {
        this.COLUMN_NAME_ENTRY_ID = COLUMN_NAME_ENTRY_ID;
    }

    public String getCOLUMN_COUPLET_ID() {
        return COLUMN_COUPLET_ID;
    }

    public void setCOLUMN_COUPLET_ID(String COLUMN_COUPLET_ID) {
        this.COLUMN_COUPLET_ID = COLUMN_COUPLET_ID;
    }

    public String getCOLUMN_CHAPTER_ID() {
        return COLUMN_CHAPTER_ID;
    }

    public void setCOLUMN_CHAPTER_ID(String COLUMN_CHAPTER_ID) {
        this.COLUMN_CHAPTER_ID = COLUMN_CHAPTER_ID;
    }

    @Override
    public String toString() {
        return getCOLUMN_NAME_ENTRY_ID()+":"+getCOLUMN_CHAPTER_ID()+":"+getCOLUMN_COUPLET_ID();
    }
}
