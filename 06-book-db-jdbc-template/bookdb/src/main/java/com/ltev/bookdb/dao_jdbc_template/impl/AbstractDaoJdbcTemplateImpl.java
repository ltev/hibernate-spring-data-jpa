package com.ltev.bookdb.dao_jdbc_template.impl;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDaoJdbcTemplateImpl<T extends LongIdEntity> implements BaseDao<T, Long> {

    private String COUNT_SQL = "select count(*) from <table>";
    private String SELECT_BY_ID_SQL = "select * from <table> where id = ?";
    private String DELETE_BY_ID_SQL = "delete from <table> where id = ?";

    protected final DataSource dataSource;

    public AbstractDaoJdbcTemplateImpl(DataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        COUNT_SQL = COUNT_SQL.replace("<table>", tableName);
        SELECT_BY_ID_SQL = SELECT_BY_ID_SQL.replace("<table>", tableName);
        DELETE_BY_ID_SQL = DELETE_BY_ID_SQL.replace("<table>", tableName);
    }

    @Override
    public long count() {
        return 0;
    }

    /**
     * Insert into db when id == null
     * Update db row when id != null
     *
     * @param entity
     * @return original object
     */
    @Override
    public T save(T entity) {
        return entity;
    }

    private T update(T entity) {
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
    }

    /**
     * General findBy... method
     * @param sql
     * @param params - List with parameters for sql placeholder: '?' for PreparedStatement
     * @return
     */
    protected List<T> findBy(String sql, List<Object> params) {
        return List.of();
    }

    abstract protected String getInsertSql();

    abstract protected void setInsertParameters(T entity, PreparedStatement ps) throws SQLException;

    abstract protected void updateRow(T entity, ResultSet rs) throws SQLException;

    abstract protected T getEntityFromRS(ResultSet rs) throws SQLException;
}
