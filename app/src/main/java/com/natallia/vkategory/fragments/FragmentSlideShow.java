package com.natallia.vkategory.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;


import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.natallia.vkategory.R;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Natallia on 12.04.2016.
 */
public class FragmentSlideShow extends Fragment implements  ViewSwitcher.ViewFactory,
        GestureDetector.OnGestureListener {

    static final String TAG = "SlideShow";
    private int mPostID;
    private ImageSwitcher mImageSwitcher;
    private int position;
    private List<Photo> photos;
    private Button buttonForward;
    private Button buttonPrevious;
    private ImageView currentView;
    private TextView tvCount;

    private GestureDetectorCompat mGestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static FragmentSlideShow createFragment() {
        FragmentSlideShow fragment = new FragmentSlideShow();
        return fragment;
    }

    public int getPostID() {
        return mPostID;
    }

    public void setPostID(int mPostID) {
        this.mPostID = mPostID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow, container, false);

        if (savedInstanceState != null) {
            mPostID = savedInstanceState.getInt("postId", 0);
            position = savedInstanceState.getInt("position",0);
        }

        try {
            mGestureDetector = new GestureDetectorCompat(getContext(), this);
            Note post = DataManager.getInstance().getPostById(mPostID);
            tvCount = (TextView) view.findViewById(R.id.tvCount);

            mImageSwitcher = (ImageSwitcher) view.findViewById(R.id.image_switcher);
            mImageSwitcher.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                      return mGestureDetector.onTouchEvent(event);
                }
            });

            photos = DataManager.getInstance().getPhotoList(post);
            refreshTvCount();
            mImageSwitcher.setFactory(this);
            buttonForward = (Button) view.findViewById(R.id.buttonForward);
            buttonForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPositionNext();
                    ImageView currentView = (ImageView) mImageSwitcher.getCurrentView();
                    Picasso.with(getContext())
                            .load(photos.get(position).getVkURL_640())
                            .placeholder(android.R.drawable.star_big_on)
                            .into(currentView);
                }
            });
            buttonPrevious = (Button) view.findViewById(R.id.buttonPrev);
            buttonPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView currentView = (ImageView) mImageSwitcher.getCurrentView();
                    setPositionPrev();
                    Picasso.with(getContext()) //передаем контекст приложения
                            .load(photos.get(position).getVkURL_640())
                            .placeholder(android.R.drawable.star_big_on)
                            .into(currentView);
                }
            });

            Animation inAnimation = new AlphaAnimation(0, 1);
            inAnimation.setDuration(6000);
            Animation outAnimation = new AlphaAnimation(1, 0);
            outAnimation.setDuration(6000);

//            Animation slideInLeftAnimation = AnimationUtils.loadAnimation(getContext(),
//                    android.R.anim.slide_in_left);
//            Animation slideOutRight = AnimationUtils.loadAnimation(getContext(),
//                    android.R.anim.slide_out_right);

            mImageSwitcher.setInAnimation(inAnimation);
            mImageSwitcher.setOutAnimation(outAnimation);
            ImageView currentView = (ImageView) mImageSwitcher.getCurrentView();
            Picasso.with(getContext()) //передаем контекст приложения
                        .load(photos.get(position).getVkURL_640())
                        .placeholder(android.R.drawable.star_big_on)
                        .into(currentView);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("postId", mPostID);
        outState.putInt("position", position);
    }



    @Override
    public View makeView() {
        ImageView imageView = new ImageView(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                Log.d(TAG, "onTouchEvent: ");
                mGestureDetector.onTouchEvent(event);
                return super.onTouchEvent(event);
            }
        };
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new
                ImageSwitcher.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundColor(0xFF000000);
        return imageView;
    }

    public void setPositionNext() {
        position++;
        if (position > photos.size() - 1) {
           // position = 0;
            position = photos.size() - 1;
        }
    }

    public void setPositionPrev() {
        position--;
        if (position < 0) {
            //position = photos.size() - 1;
            position = 0;
        }
    }


    public void refreshTvCount() {
        tvCount.setText(""+(position+1)+"/"+photos.size());
    }




    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            Log.d(TAG, "onFling: ");
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // справа налево
            currentView = (ImageView) mImageSwitcher.getCurrentView();
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                setPositionNext();
                refreshTvCount();

                Picasso.with(getContext()) //передаем контекст приложения
                        .load(photos.get(position).getVkURL_640())
                        .placeholder(android.R.drawable.star_big_on)
                        .into(currentView);

               // mImageSwitcher.setImageResource(mImageIds[position]);
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // слева направо
                setPositionPrev();
                refreshTvCount();
                Picasso.with(getContext()) //передаем контекст приложения
                        .load(photos.get(position).getVkURL_640())
                        .placeholder(android.R.drawable.star_big_on)
                        .into(currentView);
            }
        } catch (Exception e) {
            // nothing
            return true;
        }
        return true;
    }



    public class ImageSwitcherPicasso implements Target {

        private ImageSwitcher mImageSwitcher;
        private Context mContext;

        public ImageSwitcherPicasso(Context context, ImageSwitcher imageSwitcher){
            mImageSwitcher = imageSwitcher;
            mContext = context;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            mImageSwitcher.setImageDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {

        }

        @Override
        public void onPrepareLoad(Drawable drawable) {

        }

    }



}
