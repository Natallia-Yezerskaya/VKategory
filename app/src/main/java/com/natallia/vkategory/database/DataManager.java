package com.natallia.vkategory.database;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiOwner;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPostArray;
import com.vk.sdk.api.model.VKUsersArray;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataManager  {
    //public Activity activity;
    private static DataManager instance;
    public static String TAG = "myTags";
    List<Note> notesLoaded;
    SQLiteDatabase db;
    NotesDAO notesDAO;
    CategoryDAO categoryDAO;
    PhotoDAO photoDAO;

    public static DataManager getInstance (){
        return instance;
    }

    public DataManager(DBHelper dbHelper) {
        //this.activity = activity;
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

    public Category renameCategory(Category category, String name){
        try {
            category.setName(name);
            categoryDAO.update(category);
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

        AsyncLoader loader = new AsyncLoader();

        loader.setListener(new AsyncLoader.AsyncLoaderListener() {
            @Override
            public void onAllLoaded(VKPostArray postArray, VKList<VKApiUser> userList, VKList<VKApiCommunity> groupsList) {

                Category cat = null;
                try {
                    cat = categoryDAO.getCategoryById(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (int i = postArray.size() - 1; i > 0; i++) {
                    VKApiPost post = postArray.get(i);

                    Note note = new Note();
                    note.setText(post.text);
                    note.setCategory(cat);
                    note.setVkID(post.id);
                    note.setFrom_id(post.from_id);
                    note.setOwner_id(post.to_id);
                    if (userList.getById(post.from_id) != null) {
                        note.setSourceName(userList.getById(post.from_id).toString());
                        note.setSourcePhoto_50(userList.getById(post.from_id).photo_50);
                        note.setSourcePhoto_100(userList.getById(post.from_id).photo_100);
                    }
                    if (groupsList.getById(-post.from_id) != null) {
                        note.setSourceName(groupsList.getById(-post.from_id).name);
                        note.setSourcePhoto_50(groupsList.getById(-post.from_id).photo_50);
                        note.setSourcePhoto_100(groupsList.getById(-post.from_id).photo_100);
                    }
                    note.setDate(post.date);
                    try {
                        notesDAO.create(note);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "row inserted, Text = " + postArray.get(i).text);

                    if (post.attachments.size() != 0) {
                        for (int j = 0; j < postArray.get(i).attachments.size(); j++) {
                            if (postArray.get(i).attachments.get(j).getType().equals("photo")) {
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

        loader.fetchPosts(0);

    }

    public void loadPostsList(int offset, final AsyncRequestListener asyncRequestListener){
        notesLoaded = new ArrayList<Note>();
        VKRequest request =  new VKRequest("fave.getPosts", VKParameters.from(VKApiConst.COUNT, "10", VKApiConst.OFFSET, String.valueOf(offset),VKApiConst.EXTENDED,"1"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKPostArray postArray = new VKPostArray();
                VKList<VKApiUser> userList = new VKList<VKApiUser>();
                VKList<VKApiCommunity> groupsList = new VKList<VKApiCommunity>();

                try {
                    postArray.parse(response.json);
                    userList.fill(response.json.getJSONObject("response").getJSONArray("profiles"), VKApiUser.class);
                    groupsList.fill(response.json.getJSONObject("response").getJSONArray("groups"), VKApiCommunity.class);
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

                    VKApiPost post = postArray.get(i);

                    Note note = new Note();
                    note.setText(postArray.get(i).text);
                    note.setCategory(cat);
                    note.setVkID(postArray.get(i).id);

                    note.setFrom_id(post.from_id);
                    note.setOwner_id(post.to_id);
                    if (userList.getById(post.from_id)!=null) {
                        note.setSourceName(userList.getById(post.from_id).toString());
                        note.setSourcePhoto_50(userList.getById(post.from_id).photo_50);
                        note.setSourcePhoto_100(userList.getById(post.from_id).photo_100);
                    }
                    if (groupsList.getById(-post.from_id)!=null){
                        note.setSourceName(groupsList.getById(-post.from_id).name);
                        note.setSourcePhoto_50(groupsList.getById(-post.from_id).photo_50);
                        note.setSourcePhoto_100(groupsList.getById(-post.from_id).photo_100);
                    }
                    note.setDate(post.date);

                    try {
                        notesDAO.create(note);
                        Log.d(TAG, "row inserted, Text = " + postArray.get(i).text);
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
            Log.d(TAG, "update from " + oldCategoryId + " to " + categoryList.get(0).getId() );
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

    public void deleteCategory(int idCategory){
        try {
            Category categoryAll = categoryDAO.getCategoryById(1);
            Category category  = categoryDAO.getCategoryById(idCategory);
            List<Note>  noteList = notesDAO.queryBuilder().where().eq(Note.FIELD_CATEGORY_ID, idCategory).query();
            for (int i = 0; i <noteList.size() ; i++) {
                noteList.get(i).setCategory(categoryAll);
                notesDAO.update(noteList.get(i));
                Log.d(TAG, "update from " + idCategory + " to all" );
            }
            categoryDAO.delete(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class AsyncLoader {
        private int MAX_RECORDS = 1000;
        private int PACKAGE_SIZE = 50;

        public interface AsyncLoaderListener {
            void onAllLoaded(VKPostArray globalPostArray, VKList<VKApiUser> globalUserList,VKList<VKApiCommunity> globalGroupsList);
        }
        VKPostArray globalPostArray = new VKPostArray();
        VKList<VKApiUser> globalUserList = new VKList<VKApiUser>();
        VKList<VKApiCommunity> globalGroupsList = new VKList<VKApiCommunity>();
        private AsyncLoaderListener listener;


        public void setListener(AsyncLoaderListener listener) {
            this.listener = listener;
        }

        private void fetchPosts(int offset) {
            VKRequest request = new VKRequest("fave.getPosts", VKParameters.from(VKApiConst.COUNT, String.valueOf(PACKAGE_SIZE), VKApiConst.OFFSET, String.valueOf(offset), VKApiConst.EXTENDED, "1"));
            request.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKPostArray postArray = new VKPostArray();
                    VKList<VKApiUser> userList = new VKList<VKApiUser>();
                    VKList<VKApiCommunity> groupsList = new VKList<VKApiCommunity>();
                    try {
                        userList.fill(response.json.getJSONObject("response").getJSONArray("profiles"), VKApiUser.class);
                        groupsList.fill(response.json.getJSONObject("response").getJSONArray("groups"), VKApiCommunity.class);
                        postArray.parse(response.json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    globalPostArray.addAll(postArray);
                    globalGroupsList.addAll(groupsList);
                    globalUserList.addAll(userList);
                }

            });

            offset+=PACKAGE_SIZE;
            if (offset<MAX_RECORDS){
                fetchPosts(offset);
            } else {
                listener.onAllLoaded(globalPostArray,globalUserList,globalGroupsList);
            }
        }
    }
}
