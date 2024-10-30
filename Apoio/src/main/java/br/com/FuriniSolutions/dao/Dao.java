package br.com.FuriniSolutions.dao;

import java.util.List;

public interface Dao<PK, T> {
    
    public void create(T entity);
    public T retrive(PK pk);
    public void update(T entity);
    public boolean delete(PK pk);
    public List<T> findAll();
}
