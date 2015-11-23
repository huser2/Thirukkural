package com.ext.techapp.thirukkural.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Selvam on 11/22/2015.
 */
public class FavoritesDataSource {

    private SQLiteDatabase database;
    private FavoriteReaderDbHelper dbHelper;
    private String[] allColumns = {
            FavoriteColumns.FeedEntry.COLUMN_CHAPTER_ID,FavoriteColumns.FeedEntry.COLUMN_COUPLET_ID };

    public FavoritesDataSource(Context context) {
        dbHelper = new FavoriteReaderDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Favorite createFavorite(String chapter,String couplet) {
        ContentValues values = new ContentValues();
        values.put(FavoriteColumns.FeedEntry.COLUMN_CHAPTER_ID, chapter);
        values.put(FavoriteColumns.FeedEntry.COLUMN_COUPLET_ID, couplet);

        Cursor cursorCheck = database.query(FavoriteColumns.FeedEntry.TABLE_NAME,
                allColumns, FavoriteColumns.FeedEntry.COLUMN_COUPLET_ID + " = " + couplet, null,
                null, null, null);
        cursorCheck.moveToFirst();
        Favorite newComment=null;
        if (cursorCheck.getCount() == 0) {
            long insertId = database.insert(FavoriteColumns.FeedEntry.TABLE_NAME, null,
                    values);
            Cursor cursor = database.query(FavoriteColumns.FeedEntry.TABLE_NAME,
                    allColumns, FavoriteColumns.FeedEntry.COLUMN_COUPLET_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newComment = cursorToFavorite(cursor);
            cursor.close();
        }else {
            newComment = cursorToFavorite(cursorCheck);
            cursorCheck.close();
        }
        return newComment;
    }


    public void deleteFavorite(Favorite favorite) {
        long id = favorite.getCOLUMN_COUPLET_ID();
        database.delete(FavoriteColumns.FeedEntry.TABLE_NAME, FavoriteColumns.FeedEntry.COLUMN_COUPLET_ID
                + " = " + id, null);
    }

    public List<Favorite> getAllFavorites() {
        List<Favorite> favorites = new ArrayList<Favorite>();

        Cursor cursor = database.query(FavoriteColumns.FeedEntry.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Favorite favorite = cursorToFavorite(cursor);
            favorites.add(favorite);
            cursor.moveToNext();
        }
        cursor.close();
        return favorites;
    }

    private Favorite cursorToFavorite(Cursor cursor) {
        Favorite favorite = new Favorite();
        favorite.setCOLUMN_CHAPTER_ID(cursor.getLong(0));
        favorite.setCOLUMN_COUPLET_ID(cursor.getLong(1));
        return favorite;
    }
}
