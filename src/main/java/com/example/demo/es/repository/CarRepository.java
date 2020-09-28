package com.example.demo.es.repository;

import com.example.demo.es.entity.BookBean;
import com.example.demo.es.entity.CarBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarRepository extends ElasticsearchRepository<CarBean, String> {
}
