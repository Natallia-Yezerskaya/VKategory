package com.natallia.vkategory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

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

    private boolean isResumed = false;

    private Bundle mySavedInstanceState;


    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         

         
        setContentView(R.layout.activity_main);


        dbHelper = new DBHelper(this);

        MyColor.initialize(this);

         VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
             @Override
             public void onResult(VKSdk.LoginState res) {
                 if (isResumed) {
                     switch (res) {
                         case LoggedOut:
                             showLogin();
                             break;
                         case LoggedIn:
                             startMainActivity();
                             break;
                         case Pending:
                             break;
                         case Unknown:
                             break;
                     }
                 }
             }

             @Override
             public void onError(VKError error) {

             }

         });




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
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (VKSdk.isLoggedIn()) {
            startMainActivity();
        } else {
            showLogin();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    public void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, new LoginFragment())
                .commitAllowingStateLoss();
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
                        showLogin();
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
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                startMainActivity();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
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


    public static class LoginFragment extends android.support.v4.app.Fragment {
        public LoginFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);
            v.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.login(getActivity(), sMyScope);
                }
            });
            return v;
        }

    }


    private void startMainActivity() {



        final FragmentManager fm = getSupportFragmentManager();
        if (mySavedInstanceState==null) {
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
    }


}
