package com.natallia.vkategory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.natallia.vkategory.database.DAO.CategoryDAO;
import com.natallia.vkategory.database.DAO.NotesDAO;
import com.natallia.vkategory.database.DAO.PhotoDAO;
import com.natallia.vkategory.database.DAO.PostDAO;
import com.natallia.vkategory.models.Category;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.natallia.vkategory.models.Post;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "VKategory.db";
    private static final int DATABASE_VERSION = 1;
    // the DAO object we use to access
    private CategoryDAO categoryDao = null;
    private NotesDAO noteDao = null;
    private PostDAO postDao = null;
    private PhotoDAO photoDao = null;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Photo.class);
            TableUtils.createTable(connectionSource, Note.class);

            DataManager.getInstance().createPostsList();
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
         Dao<Category, Integer> dao = null;
        try {
            dao = getCategoryDao();
            // create some entries in the onCreate
            Category category = new Category("All");
            dao.create(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CategoryDAO getCategoryDao() throws SQLException {
        if (categoryDao == null) {

            categoryDao =   new CategoryDAO(getConnectionSource(), Category.class);
        }
        return categoryDao;
    }

    public NotesDAO getNoteDao() throws SQLException {
        if (noteDao == null) {
            noteDao =  new NotesDAO(getConnectionSource(), Note.class);
        }
        return noteDao;
    }

    public PostDAO getPostDao() throws SQLException {
        if (postDao == null) {
            postDao =  new PostDAO(getConnectionSource(), Post.class);
        }
        return postDao;
    }

    public PhotoDAO getPhotoDao() throws SQLException {
        if (photoDao == null) {
            photoDao =  new PhotoDAO(getConnectionSource(), Photo.class);
        }
        return photoDao;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    @Override
    public void close() {
        super.close();
        categoryDao = null;
        noteDao = null;
        postDao = null;
    }
}
