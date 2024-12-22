package com.ltev.bookdb.dao_jdbc_template.impl;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

public abstract class AbstractDaoJdbcTemplateImpl<T extends LongIdEntity> implements BaseDao<T, Long> {

    private String COUNT_SQL = "select count(*) from :table";
    private String SELECT_BY_ID_SQL = "select * from :table where id = ?";
    private String DELETE_BY_ID_SQL = "delete from :table where id = ?";

    protected final JdbcTemplate jdbcTemplate;

    public AbstractDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        COUNT_SQL = COUNT_SQL.replace(":table", tableName);
        SELECT_BY_ID_SQL = SELECT_BY_ID_SQL.replace(":table", tableName);
        DELETE_BY_ID_SQL = DELETE_BY_ID_SQL.replace(":table", tableName);
    }

    @Override
    public long count() {
        return jdbcTemplate.queryForObject(COUNT_SQL, Long.class);
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
        if (entity.getId() != null) {
            return update(entity);
        }

        int affected = jdbcTemplate.update(getInsertSql(), getInsertParameters(entity));
        if (affected != 1) {
            throw new RuntimeException("Update failed. Affected rows: " + affected);
        }

        Long generatedId = jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
        entity.setId(generatedId);
        return entity;
    }

    protected T update(T entity) {
        int affected = jdbcTemplate.update(getUpdateSql(), getUpdateParameters(entity));
        if (affected != 1) {
            throw new RuntimeException("Update failed. Affected rows: " + affected);
        }
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        try {
            T entity = jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, getRowMapper(), id);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        int affected = jdbcTemplate.update(DELETE_BY_ID_SQL, id);
        if (affected != 1) {
            throw new RuntimeException("Delete failed. Affected rows: " + affected);
        }
    }

    abstract protected String getInsertSql();

    abstract protected Object[] getInsertParameters(T entity);

    abstract protected String getUpdateSql();

    abstract protected Object[] getUpdateParameters(T entity);

    abstract protected RowMapper<T> getRowMapper();
}
