package com.natallia.vkategory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
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

/**
 * Created by Natallia on 22.03.2016.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "VKategory.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private CategoryDAO categoryDao = null;
    private NotesDAO noteDao = null;
    private PostDAO postDao = null;
    private PhotoDAO photoDao = null;
    private RuntimeExceptionDao<Category, Integer> simpleRuntimeDao = null;

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

            DataManager.getInstance().createPostsList(0);
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
           // category = new Category("First" + 1);
            //
            // dao.create(category);
            Log.i(DBHelper.class.getName(), "created new entries in onCreate: ");

        } catch (SQLException e) {
            e.printStackTrace();
        }



       /* db.execSQL("create table categories ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "id_parent integer" + ");");

        db.execSQL("create table notes ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "id_category integer" + ");");

        ContentValues cv = new ContentValues();
        cv.put("name", "спорт");
        cv.put("id_parent", 1);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("categories", null, cv);
        Log.d("myTags", "row inserted, ID = " + rowID);

        cv = new ContentValues();
        cv.put("name", "торты");
        cv.put("id_parent", 1);
        // вставляем запись и получаем ее ID
        rowID = db.insert("categories", null, cv);
        Log.d("myTags", "row inserted, ID = " + rowID);
*/
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
        simpleRuntimeDao = null;
    }
}
