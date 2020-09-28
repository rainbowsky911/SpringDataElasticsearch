package com.example.demo.es.repository;



import com.example.demo.es.entity.BookBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 接口关系：
 * ElasticsearchRepository --> ElasticsearchCrudRepository --> PagingAndSortingRepository --> CrudRepository
 */
public interface BookRepository extends ElasticsearchRepository<BookBean, String> {

    //Optional<BookBean> findById(String id);

    Page<BookBean> findByAuthor(String author, Pageable pageable);

    Page<BookBean> findByTitle(String title, Pageable pageable);
    List<BookBean> findByAuthor(String author);

}
