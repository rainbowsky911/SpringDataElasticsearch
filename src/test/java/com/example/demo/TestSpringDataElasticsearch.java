package com.example.demo;

import com.example.demo.es.entity.BookBean;
import com.example.demo.es.entity.CarBean;
import com.example.demo.es.repository.BookRepository;
import com.example.demo.es.repository.CarRepository;
import com.example.demo.es.service.BookService;
import jdk.nashorn.internal.objects.NativeStrictArguments;
import net.minidev.json.JSONUtil;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestSpringDataElasticsearch {

    @Autowired
    private CarRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService service;

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public  void testBook() {
        Optional<BookBean> opt = service.findById("1");
        BookBean book = opt.get();
        System.out.println(book);

    }
    @Test
    public  void testCreateIndex(){
            template.createIndex(CarBean.class);
            //template.putMapping(CarBean.class);
    }

    @Test
    public  void nativeQuery(){
        NativeSearchQuery query=new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("张大维").defaultField("author"))
                .withPageable(PageRequest.of(0,5))
                .build();
        //执行查询
        List<BookBean> bookBeans = template.queryForList(query, BookBean.class);
        bookBeans.forEach(a->{
            System.out.println(a);
        });

    }

}
