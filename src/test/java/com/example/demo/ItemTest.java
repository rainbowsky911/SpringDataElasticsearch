package com.example.demo;

import com.example.demo.es.entity.BookBean;
import com.example.demo.es.entity.Item;
import com.example.demo.es.repository.ItemRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *      Spring Data Elasticsearch基本使用
 *
 * Link  https://www.cnblogs.com/ifme/p/12005026.html
 */
@SpringBootTest

public class ItemTest {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testCreate() {
        // 创建索引，会根据Item类的@Document注解信息来创建
        elasticsearchTemplate.createIndex(Item.class);
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        elasticsearchTemplate.putMapping(Item.class);
    }


    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();


        list.add(new Item(51L, "蓝牙耳机", " 耳机", "小米", 399.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(53L, "蓝牙耳机", " 耳机", "深海", 4799.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(54L, "蓝牙耳机", " 耳机", "三星", 4799.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(55L, "蓝牙耳机", " 耳机", "苹果", 4799.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(56L, "蓝牙耳机", " 耳机", "Beatles", 1799.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(57L, "蓝牙耳机", " 耳机", "森海塞尔", 499.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(58L, "蓝牙耳机", " 耳机", "索尼", 499.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(59L, "蓝牙耳机", " 耳机", "铁三角", 47599.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(60L, "蓝牙耳机", " 耳机", "AKG", 479449.00, "http://image.leyou.com/3.jpg"));


        list.add(new Item(60L, "半入耳式耳机", " 耳机", "Beatles", 1299.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(61L, "半入耳式耳机", " 耳机", "森海塞尔", 299.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(62L, "半入耳式耳机", " 耳机", "索尼", 422.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(63L, "半入耳式耳机", " 耳机", "铁三角", 599.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(64L, "半入耳式耳机", " 耳机", "AKG", 445.00, "http://image.leyou.com/3.jpg"));

        list.add(new Item(50L, "不入耳耳机", " 耳机", "小米", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(65L, "不入耳耳机", " 耳机", "Beatles", 1299.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(66L, "不入耳耳机", " 耳机", "森海塞尔", 299.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(67L, "不入耳耳机", " 耳机", "索尼", 422.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(68L, "不入耳耳机", " 耳机", "铁三角", 599.00, "http://image.leyou.com/3.jpg"));
        list.add(new Item(69L, "不入耳耳机", " 耳机", "AKG", 445.00, "http://image.leyou.com/3.jpg"));

        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }



    @Test
    public  void nativeQuery(){
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本的分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery("category", "手机"));

        // 初始化分页参数
        int page = 0;
        int size = 3;
        // 设置分页参数
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 执行搜索，获取结果
        Page<Item> items = this.itemRepository.search(queryBuilder.build());

        System.out.println(items.getContent().toString());
        // 打印总条数
        System.out.println(items.getTotalElements());
        // 打印总页数
        System.out.println(items.getTotalPages());
        // 每页大小
        System.out.println(items.getSize());
        // 当前页
        System.out.println(items.getNumber());
        items.forEach(System.out::println);

    }


    /**
     * 对应DSL
     * GET /item/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "group_by_brand": {
     *       "terms": {
     *         "field": "brand",
     *         "size": 10
     *       }
     *     }
     *   }
     * }
     */
    @Test
    public void testAgg(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand

        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand"));
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        // 3、解析
        // 3.1、从结果中取出名为brands的那个聚合，
        // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        // 3.2、获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        // 3.3、遍历
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key，即品牌名称
            System.out.println(bucket.getKeyAsString());
            // 3.5、获取桶中的文档数量
            System.out.println(bucket.getDocCount());
        }

    }


    /**
     * DSL
     *  GET /item/_search
     * {
     *   "size": 0,
     *   "aggs": {
     *     "group_by_brand": {
     *       "terms": {
     *         "field": "brand",
     *         "size": 10
     *       },
     *       "aggs": {
     *         "avg_price": {
     *           "avg": {
     *             "field": "price"
     *           }
     *         }
     *       }
     *     }
     *
     *   }
     * }
     *
     */
    @Test
    public void testSubAgg(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 不查询任何结果
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand")
                        .subAggregation(AggregationBuilders.avg("priceAvg").field("price")) // 在品牌聚合桶内进行嵌套聚合，求平均值
        );
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) this.itemRepository.search(queryBuilder.build());
        // 3、解析
        // 3.1、从结果中取出名为brands的那个聚合，
        // 因为是利用String类型字段来进行的term聚合，所以结果要强转为StringTerm类型
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        // 3.2、获取桶
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        // 3.3、遍历
        for (StringTerms.Bucket bucket : buckets) {
            // 3.4、获取桶中的key，即品牌名称  3.5、获取桶中的文档数量
            System.out.println(bucket.getKeyAsString() + "，共" + bucket.getDocCount() + "台");

            // 3.6.获取子聚合结果：
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
            System.out.println("平均售价：" + avg.getValue());
        }

    }




}
