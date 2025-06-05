package co.edu.icesi.pollafutbolera.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GenericService<T, ID> {

    public ResponseEntity<List<T>>  findAll() throws Exception;
    public ResponseEntity<T> save(Long id, T entity) throws Exception;
    public ResponseEntity<T> findById(ID id) throws Exception;
    public void deleteById(ID id) throws Exception;

}