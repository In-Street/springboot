package cyf.gradle.search.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 *
 * @author Cheng Yufei
 * @create 2017-12-07 11:51
 **/
@Service
@Slf4j
public class FoundationSearch implements ElasticsearchCrudRepository,ElasticsearchRepository {

    @Override
    public Iterable findAll(Sort sort) {
        return null;
    }

    @Override
    public Page findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public Iterable save(Iterable entities) {
        return null;
    }

    @Override
    public Object findOne(Serializable serializable) {
        return null;
    }

    @Override
    public boolean exists(Serializable serializable) {
        return false;
    }

    @Override
    public Iterable findAll() {
        return null;
    }

    @Override
    public Iterable findAll(Iterable iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Serializable serializable) {

    }

    @Override
    public void delete(Object entity) {

    }

    @Override
    public void delete(Iterable entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Object index(Object entity) {
        return null;
    }

    @Override
    public Iterable search(QueryBuilder query) {
        return null;
    }

    @Override
    public Page search(QueryBuilder query, Pageable pageable) {
        return null;
    }

    @Override
    public Page search(SearchQuery searchQuery) {
        return null;
    }

    @Override
    public Page searchSimilar(Object entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    public void refresh() {

    }
}