package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BaseDao;
import com.ltev.bookdb.domain.LongIdEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDaoImpl<T extends LongIdEntity> implements BaseDao<T, Long> {

    private String COUNT_SQL = "select count(*) from <table>";
    private String SELECT_BY_ID_SQL = "select * from <table> where id = ?";
    private String DELETE_BY_ID_SQL = "delete from <table> where id = ?";

    protected final DataSource dataSource;

    public AbstractDaoImpl(DataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        COUNT_SQL = COUNT_SQL.replace("<table>", tableName);
        SELECT_BY_ID_SQL = SELECT_BY_ID_SQL.replace("<table>", tableName);
        DELETE_BY_ID_SQL = DELETE_BY_ID_SQL.replace("<table>", tableName);
    }

    @Override
    public long count() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(COUNT_SQL);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            setInsertParameters(entity, ps);
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                } else {
                    throw new SQLException("Insert entity failed: " + entity);
                }
            }

            connection.commit();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private T update(T entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ps.setLong(1, entity.getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    updateRow(entity, rs);
                    rs.updateRow();
                } else {
                    throw new SQLException("No next. Update Author failed: " + entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public Optional<T> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(getEntityFromRS(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Delete Author failed: " + id + ". Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * General findBy... method
     * @param sql
     * @param params - List with parameters for sql placeholder: '?' for PreparedStatement
     * @return
     */
    protected List<T> findBy(String sql, List<Object> params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)){

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<T> entities = new ArrayList<>();

                while (rs.next()) {
                    T entity = getEntityFromRS(rs);
                    entities.add(entity);
                }

                return entities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    abstract protected String getInsertSql();

    abstract protected void setInsertParameters(T entity, PreparedStatement ps) throws SQLException;

    abstract protected void updateRow(T entity, ResultSet rs) throws SQLException;

    abstract protected T getEntityFromRS(ResultSet rs) throws SQLException;
}
