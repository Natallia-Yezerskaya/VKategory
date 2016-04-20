package com.natallia.vkategory.UI;

public interface PostDraggingListener {
    void onPostDrag(boolean isDragging);

    void onPostDetail(int postID);

    void onPostSlideShow(int postID,int position);
}
