package com.natallia.vkategory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.natallia.vkategory.UI.CategoryFragmentEventHandler;
import com.natallia.vkategory.database.DBHelper;
import com.natallia.vkategory.database.DataManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CategoryFragmentEventHandler {

    private static String POST_FRAGMENT_INSTANCE_NAME = "fragmentPost";
    private static String CATEGORY_FRAGMENT_INSTANCE_NAME = "fragmentCategory";
    PostsFragment fragmentPost = null;
    CategoryFragment fragmentCategory = null;
    public DataManager dataManager;
    private DBHelper dbHelper;
    private LinearLayout.LayoutParams lParams1;
    private LinearLayout.LayoutParams lParams2;
   // PostsFragment postsFragment;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

         dbHelper = new DBHelper(this);
         dataManager = new DataManager(this,dbHelper);
         //dataManager.createPostsList(0);

         FragmentManager fm = getSupportFragmentManager();
         fragmentCategory = (CategoryFragment) fm.findFragmentByTag(CATEGORY_FRAGMENT_INSTANCE_NAME);
         if(fragmentCategory == null){
             fragmentCategory = new CategoryFragment();
             fragmentCategory.setCategoryFragmentEventHandler(this);
             getSupportFragmentManager()
                     .beginTransaction()
                     .replace(R.id.containerCategory, fragmentCategory)
                     .commitAllowingStateLoss();
         }

         fragmentPost = (PostsFragment) fm.findFragmentByTag(POST_FRAGMENT_INSTANCE_NAME);
         if(fragmentPost == null){
            refreshPostsFragment(0);
         }

         //refreshPostsFragment(0);

        /*

         setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void refreshPostsFragment(int idCategory) {

        Intent intent = new Intent();
        intent.putExtra("idCategory", idCategory);
        fragmentPost = PostsFragment.createFragment(intent);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerPosts,fragmentPost,POST_FRAGMENT_INSTANCE_NAME)
                .commitAllowingStateLoss();
    }

    @Override
    public void OnPostCategoryChange(int postId, int categoryId) {
        DataManager.getInstance().replacePostsIntoCategory(postId, categoryId);
        fragmentPost.removePostFromView(postId);
    }


    public void CategoryForChoosing(boolean forChoosing) {

        fragmentCategory.CategoryForChoosing(forChoosing);

        FrameLayout layoutCategory = (FrameLayout) findViewById(R.id.containerCategory);
        FrameLayout layoutPosts = (FrameLayout) findViewById(R.id.containerPosts);
        lParams1 = (LinearLayout.LayoutParams) layoutCategory.getLayoutParams();
        lParams2 = (LinearLayout.LayoutParams) layoutPosts.getLayoutParams();

        if (forChoosing){
            lParams1.width =  lParams1.width+30;
            lParams2.rightMargin = lParams2.rightMargin-30;
        }
       else{
            lParams1.weight = 2;
            lParams2.weight = 8;
        }



        //int oldWidth = layout.getWidth();
        //layout.setMinimumWidth(oldWidth+10);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
