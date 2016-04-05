package com.natallia.vkategory.database.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.natallia.vkategory.models.Category;
import com.natallia.vkategory.models.Post;

import java.sql.SQLException;
import java.util.List;

public class PostDAO extends BaseDaoImpl<Post, Integer>{

    public PostDAO(ConnectionSource connectionSource,
                   Class<Post> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Post> getAllPosts() throws SQLException{
        return this.queryForAll();
    }
}