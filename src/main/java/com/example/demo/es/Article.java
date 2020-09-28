package com.example.demo.es;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Article {
    private Integer id;
    private String title;
    private String content;
}
