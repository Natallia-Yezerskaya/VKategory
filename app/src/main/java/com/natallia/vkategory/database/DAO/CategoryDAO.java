package com.natallia.vkategory.database.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.natallia.vkategory.models.Category;
import com.natallia.vkategory.models.Note;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAO extends BaseDaoImpl<Category, Integer>{

    public CategoryDAO(ConnectionSource connectionSource,
                       Class<Category> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Category> getAllCategories() throws SQLException{
        return this.queryForAll();
    }
    public Category getCategoryById(int id) throws SQLException{
        return this.queryForId(id);
    }
}
