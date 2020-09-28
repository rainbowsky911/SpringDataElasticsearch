package com.example.demo;

import com.example.demo.es.Article;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {


    @Test
    public void creatIndex() throws Exception {
        //创建一个Setting对象，相当于是一个配置信息，主要是配置集群的信息。
        Settings settings = Settings.builder()
                .put("cluster.name", "my-application")
                .build();
        //创建一个客户端Client对象。
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
       /* client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9302));
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9303));*/
        //使用client对对象创建一个索引库。
        client.admin().indices().prepareCreate("test_index").get();
        //关闭client对象
        client.close();
    }

    @Test
    public void setMappigns() throws Exception {
        //创建一个Setting对象，相当于是一个配置信息，主要是配置集群的信息。
        Settings settings = Settings.builder()
                .put("cluster.name", "my-application")
                .build();
        //创建一个客户端Client对象。
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
      /*  client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9302));
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9303));*/
        //创建一个mappings信息
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article")
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .field("store", true)
                .endObject()
                .startObject("title")
                .field("type", "text")
                .field("store", true)
                .field("analyzer", "ik_max_word")
                .endObject()
                .startObject("content")
                .field("type", "text")
                .field("store", true)
                .field("analyzer", "ik_max_word")
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        //使用客户端将mapping信息设置到索引库中
        client.admin().indices()
                //设置坐映射的索引
                .preparePutMapping("index_hello")
                //设置做映射的type
                .setType("article")
                //Mapping信息，可以是XcontentBuilder对象也可以是json对象
                .setSource(builder)
                //执行操作
                .get();
        //关闭连接
        client.close();
    }

    @Test
    public void test3() throws Exception {
        //创建文档(通过XContentBuilder)public void test4() throws Exception{
        // 创建Client连接对象
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        //创建文档信息
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", 2)
                .field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
                .field("content",
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。")
                .endObject();

        // 建立文档对象
        /**
         * 参数一blog1：表示索引对象
         * 参数二article：类型
         * 参数三1：建立id
         */
        client.prepareIndex("blog22", "article", "2").setSource(builder).get();

        //释放资源
        client.close();
    }


    @Test//创建文档(通过实体转json)
    public void test5() throws Exception {
        // 创建Client连接对象
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        // 描述json 数据
        //{id:xxx, title:xxx, content:xxx}
        Article article = new Article();
        article.setId(222);
        article.setTitle("林志颖");
        article.setContent("十七岁的雨季");

        ObjectMapper objectMapper = new ObjectMapper();

        // 建立文档
        client.prepareIndex("hey_jude", "article", article.getId().toString())
                //.setSource(objectMapper.writeValueAsString(article)).get();
                .setSource(objectMapper.writeValueAsString(article).getBytes(), XContentType.JSON).get();
    }

    @Test
    public void testTermQuery() throws Exception {
        //1、创建es客户端连接对象
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        //2、设置搜索条件
        SearchResponse searchResponse = client.prepareSearch("websites")
                .setTypes("blog")
                .setQuery(QueryBuilders.queryStringQuery("ElasticSearch6.x").defaultField("abstract")).get();

        //3、遍历搜索结果数据
        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象
            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
            System.out.println("title:" + searchHit.getFields().get("title").getValue());
        }

        //4、释放资源

        //4、释放资源
        client.close();
    }

    @Test
    public void query() throws UnknownHostException {
//1、指定es集群  cluster.name 是固定的key值，my-application是ES集群的名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //2.创建访问ES服务器的客户端
        TransportClient client = new PreBuiltTransportClient(settings)
                //获取es主机中节点的ip地址及端口号(以下是单个节点案例)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        //实现数据查询(指定_id查询) 参数分别是 索引名，类型名  id
        GetResponse response = client.prepareGet("hey_jude", "article", "222").execute().actionGet();
        //得到查询出的数据
        System.out.println(response.getSourceAsString());//打印出json数据
        client.close();//关闭客户端
    }


    @Test
    public void test9() throws Exception {
        // 创建Client连接对象
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 1; i <= 100; i++) {
            // 描述json 数据
            Article article = new Article();
            article.setId(i);
            article.setTitle(i + "搜索工作其实很快乐");
            article.setContent(i + "我们希望我们的搜索解决方案要快，我们希望有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP的索引数据，我们希望我们的搜索服务器始终可用，我们希望能够一台开始并扩展到数百，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。Elasticsearch旨在解决所有这些问题和更多的问题。");

            // 建立文档
            client.prepareIndex("blog2", "article", article.getId().toString())
                    //.setSource(objectMapper.writeValueAsString(article)).get();
                    .setSource(objectMapper.writeValueAsString(article).getBytes(), XContentType.JSON).get();
        }

        //释放资源
        client.close();
    }


    @Test//分页查询
    public void test10() throws Exception {
        // 创建Client连接对象
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        // 搜索数据
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blog2").setTypes("article")
                .setQuery(QueryBuilders.matchAllQuery());//默认每页10条记录

        // 查询第2页数据，每页20条
        //setFrom()：从第几条开始检索，默认是0。
        //setSize():每页最多显示的记录数。
        searchRequestBuilder.setFrom(0).setSize(2);
        SearchResponse searchResponse = searchRequestBuilder.get();

        SearchHits hits = searchResponse.getHits(); // 获取命中次数，查询结果有多少对象
        System.out.println("查询结果有：" + hits.getTotalHits() + "条");
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next(); // 每个查询对象


            System.out.println(searchHit.getSourceAsString()); // 获取字符串格式打印
            System.out.println("id:" + searchHit.getFields().get("title").getValue());
            System.out.println("title:" + searchHit.getFields().get("title").getValue());

            System.out.println("-----------------------------------------");
        }



        /*SearchResponse response = client.prepareSearch("blog2")
                .setTypes("my-type")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setFetchSource(new String[]{"field1"}, null)
                .setQuery(QueryBuilders.termsQuery("field1", "1234"))
                .execute()
                .actionGet();

        for (SearchHit hit : response.getHits()){
            Map map = hit.getSource();
            map.toString();
        }*/


        //释放资源
        client.close();
    }


    @Test//高亮查询
    public void test11() throws Exception {
        // 创建Client连接对象
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        // 搜索数据
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("blog2").setTypes("article")
                .setQuery(QueryBuilders.queryStringQuery( "搜索"));

        //设置高亮数据
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<font style='color:red'>");
        hiBuilder.postTags("</font>");
        hiBuilder.field("title");
        searchRequestBuilder.highlighter(hiBuilder);

        //获得查询结果数据
        SearchResponse searchResponse = searchRequestBuilder.get();

        //获取查询结果集
        SearchHits searchHits = searchResponse.getHits();
        System.out.println("共搜到:" + searchHits.getTotalHits() + "条结果!");
        //遍历结果
        for (SearchHit hit : searchHits) {
            System.out.println("String方式打印文档搜索内容:");
            System.out.println(hit.getSourceAsString());
            System.out.println("Map方式打印高亮内容");
            System.out.println(hit.getHighlightFields());

            System.out.println("遍历高亮集合，打印高亮片段:");
            Text[] text = hit.getHighlightFields().get("title").getFragments();
            for (Text str : text) {
                System.out.println(str);
            }
        }

        //释放资源
        client.close();
    }

    @Test
    public void testQueryById() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        //client对象为TransportClient对象
        SearchResponse response = client.prepareSearch("blog2")
                .setTypes("article")
                //设置要查询的id
                .setQuery(QueryBuilders.idsQuery().addIds("223","222"))
                //执行查询
                .get();
        //取查询结果
        SearchHits searchHits = response.getHits();
        //取查询结果总记录数
        System.out.println(searchHits.getTotalHits());
        Iterator<SearchHit> hitIterator = searchHits.iterator();
        while (hitIterator.hasNext()) {
            SearchHit searchHit = hitIterator.next();
            //打印整行数据
            System.out.println(searchHit.getSourceAsString());
        }
    }
}
