package com.example.demo.es.service.impl;




import com.example.demo.es.entity.BookBean;
import com.example.demo.es.repository.BookRepository;
import com.example.demo.es.service.BookService;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("blogService")
public class BookServiceImpl implements BookService {



    @Autowired
    @Qualifier("bookRepository")
    private BookRepository bookRepository;


    @Override
    public Optional<BookBean> findById(String id) {
        //CrudRepository中的方法
        return bookRepository.findById(id);
    }

    @Override
    public BookBean save(BookBean blog) {
        return bookRepository.save(blog);
    }

    @Override
    public void delete(BookBean blog) {
        bookRepository.delete(blog);
    }

    @Override
    public Optional<BookBean> findOne(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<BookBean> findAll() {
        return Lists.newArrayList(bookRepository.findAll());
    }

    @Override
    public Page<BookBean> findByAuthor(String author, PageRequest pageRequest) {

        return bookRepository.findByAuthor(author,pageRequest);
    }

    @Override
    public Page<BookBean> findByTitle(String title, PageRequest pageRequest) {
        return bookRepository.findByTitle(title,pageRequest);
    }

    @Override
    public List<BookBean> findByAuthor(String author) {
        return Lists.newArrayList(bookRepository.findByAuthor(author));
    }
}


