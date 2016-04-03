package com.natallia.vkategory.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKPostArray;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Natallia on 22.03.2016.
 */
public class DataManager1 {
    public Activity activity;

    public static String LOG_TAG = "myTags";
    ContentValues cv = new ContentValues();


    SQLiteDatabase db;

    public DataManager1(Activity activity, DBHelper dbHelper) {
        this.activity = activity;
        db = dbHelper.getWritableDatabase();
    }

    public void addCategory(String name){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("id_parent", 1);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("categories", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        getCategories();
    }

    public void getCategories(){
        Cursor c = db.query("categories", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int idParentColIndex = c.getColumnIndex("id_parent");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", id_parent = " + c.getInt(idParentColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }


    public List<String> getCategoriesList(){
        List<String> arrayList = new ArrayList<>();
        Cursor c = db.query("categories", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            do {
                arrayList.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        } else

        c.close();
        return arrayList;
    }
    public void createPostsList(){
        VKRequest request = new VKRequest("fave.getPosts");
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

                // VKApiPost post  = postArray.get(0) ;

                List<String> posts = new ArrayList<>();
                for (int i = 0; i <postArray.size() ; i++) {
                    posts.add(postArray.get(i).text);

                    ContentValues cv = new ContentValues();
                    cv.put("name", postArray.get(i).text);
                    cv.put("id_category", 1);
                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("notes", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                }
            }
        });
    }
    public List<String> getPostsList(){
        List<String> arrayList = new ArrayList<>();
        Cursor c = db.query("notes", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            do {
                arrayList.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        } else

            c.close();
        return arrayList;
    }


    public List<String> getPostsListByCategory(int idCategory){
        String selection = null;
        String[] selectionArgs = null;
        List<String> arrayList = new ArrayList<>();
        selection = "id_category = ?";
        selectionArgs = new String[] { String.valueOf(idCategory) };
        Cursor c = db.query("notes", null, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("name");
            do {
                arrayList.add(c.getString(nameColIndex));
            } while (c.moveToNext());
        } else

            c.close();
        return arrayList;
    }

}
