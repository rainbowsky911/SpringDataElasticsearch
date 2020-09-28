package com.example.demo.es.controller;


import com.example.demo.es.entity.BookBean;
import com.example.demo.es.service.BookService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ElasticController {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private BookService bookService;

    @RequestMapping("/book/{id}")
    @ResponseBody
    public BookBean getBookById(@PathVariable String id){
        Optional<BookBean> opt =bookService.findById(id);
        BookBean book=opt.get();
        System.out.println(book);
        return book;
    }





    @RequestMapping("/book")
    @ResponseBody
    public  List<BookBean> getBook(){
        List<BookBean> opt =bookService.findAll();
        System.out.println(opt);
        return opt;
    }

    @RequestMapping("/save")
    @ResponseBody
    public void Save(){
        BookBean book=new BookBean("1","积极心理学","张闯","2019-12-30");
        System.out.println(book);
        bookService.save(book);
    }
    @RequestMapping("/bookAuthor")
    @ResponseBody
    public List<BookBean> getBookByAuthor(@RequestParam("author") String author){
        List<BookBean> opt =bookService.findByAuthor(author);
        return opt;
    }

    @RequestMapping("/author/{author}")
    @ResponseBody
    public List<BookBean> getBookAuthor(@PathVariable("author") String author){
        List<BookBean> opt =bookService.findByAuthor(author);
        return opt;
    }

    @RequestMapping("/authorPage")
    @ResponseBody
    public   Page<BookBean>  getBookAuthorPage(/*@PathVariable("author") String author*/){
       PageRequest pageable=PageRequest.of(0,3);
        Page<BookBean> book = bookService.findByAuthor("张", pageable);
        return book;

    }
    @RequestMapping("/native")
    @ResponseBody
    public    List<BookBean> getBookNative(){
        NativeSearchQuery query=new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("张大维").defaultField("author"))
                .withPageable(PageRequest.of(0,5))
                .build();
        //执行查询
        List<BookBean> bookBeans = template.queryForList(query, BookBean.class);
        bookBeans.forEach(a->{
            System.out.println(a);
        });
        return  bookBeans;

    }

}

