package com.productions.pieter.notificationanalyzer.Models;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Application DAO implementation.
 *
 * Created by pieter on 26/09/14.
 */
public class ApplicationDaoImpl extends BaseDaoImpl<Application, String> implements ApplicationDao {
    public ApplicationDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Application.class);
    }

    @Override
    public List<Application> getIgnoredApps() throws SQLException {
        return this.queryForEq(Application.FIELD_IGNORE, true);
    }
}
