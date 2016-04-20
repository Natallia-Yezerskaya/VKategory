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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.natallia.vkategory.UI.MyColor;
import com.natallia.vkategory.UI.PostDraggingListener;
import com.natallia.vkategory.database.DBHelper;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.fragments.CategoryFragment;
import com.natallia.vkategory.fragments.FragmentDetail;
import com.natallia.vkategory.fragments.FragmentMain;
import com.natallia.vkategory.fragments.FragmentSlideShow;
import com.natallia.vkategory.fragments.PostsFragment;
import com.vk.sdk.VKSdk;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,PostDraggingListener {

    private static String POST_FRAGMENT_INSTANCE_NAME = "fragmentPost";
    private static String CATEGORY_FRAGMENT_INSTANCE_NAME = "fragmentCategory";
    private static String MAIN_FRAGMENT_INSTANCE_NAME = "fragmentMain";
    private static String DETAIL_FRAGMENT_INSTANCE_NAME = "fragmentMain";
    PostsFragment fragmentPost = null;
    CategoryFragment fragmentCategory = null;
    public DataManager dataManager;
    private DBHelper dbHelper;
    private FrameLayout.LayoutParams lParams1;
    private int mWidthLayoutCategory;
    FrameLayout layoutCategory;

    public FragmentMain fragmentMain;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
         final FragmentManager fm = getSupportFragmentManager();
         
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//         toolbar.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 fragmentMain = (FragmentMain) fm.findFragmentByTag(MAIN_FRAGMENT_INSTANCE_NAME);
//                 if (fragmentMain == null) {
//                     PostsFragment fragmentPost = (PostsFragment) fragmentMain.getChildFragmentManager().findFragmentByTag(FragmentMain.POST_FRAGMENT_INSTANCE_NAME);
//                     if (fragmentMain == null) {
//                         fragmentPost.setFocusOnFirstItem();
//                     }
//                 }
//             }
//         });

         dbHelper = new DBHelper(this);
         dataManager = new DataManager(this,dbHelper);
         MyColor.initialize(this);


         

         if (savedInstanceState==null) {
             fragmentMain = (FragmentMain) fm.findFragmentByTag(MAIN_FRAGMENT_INSTANCE_NAME);
             if (fragmentMain == null) {
                 fragmentMain = FragmentMain.createFragment();
                 getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.container_main, fragmentMain, MAIN_FRAGMENT_INSTANCE_NAME)
                                 //.addToBackStack(null)
                         .commit();
             }
         }


         //setSupportActionBar(toolbar);
  /*
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),
                        "You selected Settings", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_logout:
                Toast.makeText(getApplicationContext(),
                        "You selected Logout", Toast.LENGTH_LONG).show();
                        VKSdk.logout();
                        startActivity(new Intent(this,LoginActivity.class));
                       // if (!VKSdk.isLoggedIn()) {((LoginActivity) getActivity()).showLogin();}

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }



    public void openDetailFragment(int postID) {
        FragmentDetail details = FragmentDetail.createFragment();
        details.setPostDraggingListener(this);
        details.setPostID(postID);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, details,DETAIL_FRAGMENT_INSTANCE_NAME)
                .addToBackStack(null)
                .commit();
    }

    public void openSlideShowFragment(int postID,int position) {
        FragmentSlideShow slideShow = FragmentSlideShow.createFragment();
        slideShow.setPostID(postID);
        slideShow.setPosition(position);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, slideShow,DETAIL_FRAGMENT_INSTANCE_NAME)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onPostDrag(boolean isDragging) {

    }

    @Override
    public void onPostDetail(int postID) {

    }

    @Override
    public void onPostSlideShow(int postID, int position) {
        openSlideShowFragment(postID,position);
    }
}
