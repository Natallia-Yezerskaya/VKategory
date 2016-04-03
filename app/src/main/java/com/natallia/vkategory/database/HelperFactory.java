package com.natallia.vkategory.database;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Natallia on 23.03.2016.
 */
public class HelperFactory {

        private static DBHelper dbHelper;

        public static DBHelper getHelper(){
            return dbHelper;
        }
        public static void setHelper(Context context){
            dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        }
        public static void releaseHelper(){
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }
