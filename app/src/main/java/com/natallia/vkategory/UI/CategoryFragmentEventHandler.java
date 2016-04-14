package com.natallia.vkategory.UI;

public interface CategoryFragmentEventHandler {
    boolean OnPostCategoryChange(int postId,int CategoryId);

    void refreshPost(int id);

}
