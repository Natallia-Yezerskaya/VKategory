package com.natallia.vkategory.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.natallia.vkategory.models.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Natallia on 23.03.2016.
 */
public class QuestionService {
    private final String url = "jdbc:sqlite:main.sqlite";
    private ConnectionSource source;
    private Dao<Category, String> dao;

    public QuestionService() throws SQLException {
      //  source = new JdbcConnectionSource(url);
        dao = DaoManager.createDao(source, Category.class);
    }

    public List<Category> getAll() throws SQLException {
        return dao.queryForAll();
    }
}