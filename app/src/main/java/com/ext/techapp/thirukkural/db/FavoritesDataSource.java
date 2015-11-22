package com.ext.techapp.thirukkural.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Selvam on 11/22/2015.
 */
public class FavoritesDataSource {

    private SQLiteDatabase database;
    private FavoriteReaderDbHelper dbHelper;
    private String[] allColumns = { Favorites.FeedEntry.COLUMN_NAME_ENTRY_ID,
            Favorites.FeedEntry.COLUMN_CHAPTER_ID,Favorites.FeedEntry.COLUMN_COUPLET_ID };

    public FavoritesDataSource(Context context) {
        dbHelper = new FavoriteReaderDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Favorite createFavorite(String chatper,String couplet) {
        ContentValues values = new ContentValues();
        values.put(Favorites.FeedEntry.COLUMN_CHAPTER_ID, chatper);
        values.put(Favorites.FeedEntry.COLUMN_COUPLET_ID, couplet);

        long insertId = database.insert(Favorites.FeedEntry.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(Favorites.FeedEntry.TABLE_NAME,
                allColumns, Favorites.FeedEntry.COLUMN_NAME_ENTRY_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Favorite newComment = cursorToFavorite(cursor);
        cursor.close();
        return newComment;
    }


    public void deleteFavorite(Favorite favorite) {
        long id = favorite.getCOLUMN_NAME_ENTRY_ID();
        System.out.println("Comment deleted with id: " + id);
        database.delete(Favorites.FeedEntry.TABLE_NAME, Favorites.FeedEntry.COLUMN_NAME_ENTRY_ID
                + " = " + id, null);
    }

    public List<Favorite> getAllFavorites() {
        List<Favorite> favorites = new ArrayList<Favorite>();

        Cursor cursor = database.query(Favorites.FeedEntry.TABLE_NAME,
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
        favorite.setCOLUMN_NAME_ENTRY_ID(cursor.getLong(0));
        favorite.setCOLUMN_CHAPTER_ID(cursor.getString(1));
        favorite.setCOLUMN_COUPLET_ID(cursor.getString(2));
        return favorite;
    }
}
