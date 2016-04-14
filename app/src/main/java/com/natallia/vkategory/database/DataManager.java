package com.natallia.vkategory.database;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.stmt.query.Not;
import com.natallia.vkategory.UI.AsyncRequestListener;
import com.natallia.vkategory.database.DAO.CategoryDAO;
import com.natallia.vkategory.database.DAO.NotesDAO;
import com.natallia.vkategory.database.DAO.PhotoDAO;
import com.natallia.vkategory.models.Category;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKPostArray;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataManager  {
    public Activity activity;
    private static DataManager instance;
    public static String LOG_TAG = "myTags";
    List<Note> notesLoaded;
    SQLiteDatabase db;
    NotesDAO notesDAO;
    CategoryDAO categoryDAO;
    PhotoDAO photoDAO;

    public static DataManager getInstance (){
        return instance;
    }

    public DataManager(Activity activity, DBHelper dbHelper) {
        this.activity = activity;
        instance = this;
        db = dbHelper.getWritableDatabase();
        try {
            categoryDAO = HelperFactory.getHelper().getCategoryDao();
            notesDAO = HelperFactory.getHelper().getNoteDao();
            photoDAO = HelperFactory.getHelper().getPhotoDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Category addCategory(String name){
        Category category = new Category();
        category.setName(name);
        try {
           categoryDAO.create(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public List<Category> getCategoriesList(){
        List<Category> list = null;
        try {
            list =  categoryDAO.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    public void createPostsList(){
        VKRequest request =  new VKRequest("fave.getPosts", VKParameters.from(VKApiConst.COUNT,"10"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKPostArray postArray = new VKPostArray();
                try {
                    postArray.parse(response.json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Category> listCategory  = null;
                try {
                    listCategory = categoryDAO.getAllCategories();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Category cat = listCategory.get(0);
                for (int i = 0; i <postArray.size() ; i++) {
                    Note note = new Note();
                    note.setText(postArray.get(i).text);
                    note.setCategory(cat);
                    note.setVkID(postArray.get(i).id);
                    try {
                        notesDAO.create(note);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.d(LOG_TAG, "row inserted, Text = " + postArray.get(i).text);

                    if (postArray.get(i).attachments.size()!=0){
                        for (int j = 0; j <postArray.get(i).attachments.size() ; j++) {
                            if (postArray.get(i).attachments.get(j).getType().equals( "photo")){
                                Photo photo = new Photo();
                                photo.setVkURL_130(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_130.toString());
                                photo.setVkURL_640(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_604.toString());
                                note.addPhoto(photo);
                            }
                        }
                    }
                    try {
                        notesDAO.update(note);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void loadPostsList(int offset, final AsyncRequestListener asyncRequestListener){
        notesLoaded = new ArrayList<Note>();
        VKRequest request =  new VKRequest("fave.getPosts", VKParameters.from(VKApiConst.COUNT, "10", VKApiConst.OFFSET, String.valueOf(offset)));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKPostArray postArray = new VKPostArray();
                try {
                    postArray.parse(response.json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<Category> listCategory  = null;
                try {
                    listCategory = categoryDAO.getAllCategories();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Category cat = listCategory.get(0);
                for (int i = 0; i <postArray.size() ; i++) {
                    Note note = new Note();
                    note.setText(postArray.get(i).text);
                    note.setCategory(cat);
                    note.setVkID(postArray.get(i).id);

                    try {
                        notesDAO.create(note);
                        Log.d(LOG_TAG, "row inserted, Text = " + postArray.get(i).text);
                        if (postArray.get(i).attachments.size()!=0){
                            for (int j = 0; j <postArray.get(i).attachments.size() ; j++)
                                if (postArray.get(i).attachments.get(j).getType().equals("photo")) {
                                    Photo photo = new Photo();
                                    photo.setVkURL_130(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_130.toString());
                                    photo.setVkURL_640(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_604);
                                    note.addPhoto(photo);
                                }
                        }

                        notesDAO.update(note);
                        notesLoaded.add(note);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                asyncRequestListener.onDataReceived(notesLoaded);
            }

        });

    }
    public List<Note> getPostsList() throws SQLException {
        return notesDAO.getAllNotes();
    }



    public List<Note> getPostsListByCategory(int idCategory){
        List<Note> noteList = new ArrayList<Note>();
        try {
            noteList = notesDAO.queryBuilder().where().eq(Note.FIELD_CATEGORY_ID, idCategory).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteList;
    }

    public int getCategoryIdByPostId(int idPost){
        List<Note> noteList = new ArrayList<Note>();
        try {
            noteList = notesDAO.queryBuilder().where().eq(Note.FIELD_ID, idPost).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteList.get(0).getCategory().getId();
    }

    public Note getPostById(int idPost) throws SQLException{
        return notesDAO.getNoteById(idPost);
    }


    public boolean replacePostsIntoCategory(int idNote,int idCategory){
        try {
            List<Note>  noteList = notesDAO.queryBuilder().where().eq(Note.FIELD_ID, idNote).query();
            List<Category>  categoryList = categoryDAO.queryBuilder().where().eq(Category.FIELD_ID, idCategory).query();
            int oldCategoryId = noteList.get(0).getCategory().getId();
            if (oldCategoryId == idCategory) {
                return false;
            }
            Note note = noteList.get(0);
            note.setCategory(categoryList.get(0));
            notesDAO.update(note);
            Log.d(LOG_TAG, "update from " + oldCategoryId + " to " + categoryList.get(0).getId() );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<Photo> getPhotoList(Note post) throws SQLException{
        Iterator<Photo> iter = post.getPhotos().iterator();
        List<Photo> photos = new ArrayList<Photo>();
        while (iter.hasNext()) {
            photos.add(iter.next());
        }
        return photos;
    }
}
