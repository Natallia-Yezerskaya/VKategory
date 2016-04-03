package com.natallia.vkategory.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.dao.ForeignCollection;
import com.natallia.vkategory.UI.AsyncRequestListener;
import com.natallia.vkategory.database.DAO.CategoryDAO;
import com.natallia.vkategory.database.DAO.NotesDAO;
import com.natallia.vkategory.database.DAO.PhotoDAO;
import com.natallia.vkategory.models.Category;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.natallia.vkategory.models.Post;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPostArray;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natallia on 22.03.2016.
 */
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

    public List<String> getCategoriesListString(){
        List<String> list = new ArrayList<>();
        try {
           List<Category> listCategory = categoryDAO.getAllCategories();
            for (int i = 0; i <listCategory.size() ; i++) {
                list.add(listCategory.get(i).getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void createPostsList(int offset){
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
                    //todo добавляем первое фото

                    if (postArray.get(i).attachments.size()!=0){
                        for (int j = 0; j <postArray.get(i).attachments.size() ; j++) {
                            if (postArray.get(i).attachments.get(j).getType().equals( "photo")){
                                Photo photo = new Photo();
                                photo.setVkURL_130(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_130.toString());
                                photo.setVkURL_640(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_604.toString());
//                                try {
//                                    photoDAO.create(photo);
//                                } catch (SQLException e) {
//                                    e.printStackTrace();
//                                }
                                note.addPhoto(photo);
                                //note.setPhoto(photo);
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
                            for (int j = 0; j <postArray.get(i).attachments.size() ; j++) {
                                if (postArray.get(i).attachments.get(j).getType().equals( "photo")){
                                    Photo photo = new Photo();
                                    photo.setVkURL_130(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_130.toString());
                                    photo.setVkURL_640(((VKApiPhoto) postArray.get(i).attachments.get(j)).photo_604.toString());
//                                try {
//                                    photoDAO.create(photo);
//                                } catch (SQLException e) {
//                                    e.printStackTrace();
//                                }
                                    note.addPhoto(photo);
                                    //note.setPhoto(photo);
                                }
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

    public List<String> getPostsListString(){
        List<String> arrayList = new ArrayList<>();
        List<Note> list;
        try {
            list = notesDAO.getAllNotes();

            for (int i = 0; i < list.size(); i++) {
                arrayList.add(list.get(i).getText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
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


    public List<String> getPostsListByCategorySTRING(int idCategory){
        List<String> stringList = new ArrayList<>();
        try {
            List<Note>  noteList = notesDAO.queryBuilder().where().eq(Note.FIELD_CATEGORY_ID, idCategory).query();
            for (int i = 0; i <noteList.size() ; i++) {
                stringList.add(noteList.get(i).getText());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public void replacePostsIntoCategory(int idNote,int idCategory){
        List<String> stringList = new ArrayList<>();
        try {
            List<Note>  noteList = notesDAO.queryBuilder().where().eq(Note.FIELD_ID, idNote).query();
            List<Category>  categoryList = categoryDAO.queryBuilder().where().eq(Category.FIELD_ID, idCategory).query();

            int oldCategoryId = noteList.get(0).getCategory().getId();
            //for (int i = 0; i <noteList.size() ; i++) {
            Note note = noteList.get(0);
                note.setCategory(categoryList.get(0));
            notesDAO.update(note);
            Log.d(LOG_TAG, "update from " + oldCategoryId + " to " + categoryList.get(0).getId() );
            //}

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
