package com.code.takeaway;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.code.takeaway.classes.ClassesCollection;
import com.code.takeaway.entity.Hotel;
import com.code.takeaway.entity.HotelDoc;
import com.code.takeaway.mapper.HotelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.code.takeaway.es.constant.HotelConstants.MAPPING_TEMPLATE;

/*
 *@ClassName TestElasticsearch
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/12 12:22
 *@Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TakeawayApplication.class)
@Slf4j
public class TestElasticsearch {
    public static RestClient restClient;
    public static ElasticsearchTransport  transport;
    public static final String INDEX_HOTEL = "hotel";
    public static final String host = "192.168.0.118";
    @Autowired
    private HotelMapper hotelMapper;

    private ElasticsearchClient create() throws IOException { //创建连接
        restClient = RestClient.builder(new HttpHost("localhost",9200)).build();
        // 使用Jackson映射器创建传输层
        transport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(transport); // 创建ES API 客户端
        return elasticsearchClient;
    }

    @Test
    public void getHotelIndex() throws IOException { //查询索引
        GetIndexResponse lgx = create().indices().get(item -> item.index("lgx"));
        log.info("查询索引 {}",lgx);
        restClient.close();
        transport.close();
    }
    @Test
    public void createHotelIndex() throws IOException {
         ElasticsearchIndicesClient indices = create().indices(); // 获取索引客户端对象
         ExistsRequest existsRequest = new ExistsRequest.Builder()
                 .index(INDEX_HOTEL)
                 .build();
         boolean flag = indices.exists(existsRequest).value();// 判断索引是否存在
         if(flag){
             log.error("索引 【{}】已存在",INDEX_HOTEL);
         }else {
              TypeMapping typeMapping = new TypeMapping.Builder()
                      .build();
             CreateIndexRequest request =new CreateIndexRequest.Builder()
                     .index(INDEX_HOTEL)
                     .mappings(typeMapping)
                     .build();
             indices.create(request);
         }
    }

    @Test
    public void createIndexMapping() throws IOException { // 创建索引 映射
        RestClient restClient = RestClient.builder(new HttpHost(host,9200)).build(); //创建连接
        ElasticsearchTransport transport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(transport); // 使用Jackson映射器创建传输层
        Map<String, Property> documentMap = new HashMap<>();
        documentMap.put("id",Property.of(property-> property.keyword(
                KeywordProperty.of(keywordProperty -> keywordProperty.index(true))

        )));//ID 支持搜索 不分词
        documentMap.put("name",Property.of(
                property -> property.text(
                        TextProperty.of(textProperty ->textProperty
                                .index(true).analyzer("ik_max_word").copyTo("all")))
        )); // 酒店名称 支持搜索 分词
        documentMap.put("address",Property.of(
                property -> property.keyword(KeywordProperty.of(keywordProperty-> keywordProperty.index(false)))

        )); //酒店地址 不支持搜索 不分词
        documentMap.put("price",Property.of(
                property -> property.integer(IntegerNumberProperty.of(integerNumberProperty->integerNumberProperty.index(true)))

        ));// 酒店价格 支持搜索
        documentMap.put("score",Property.of(
                property -> property.integer(IntegerNumberProperty.of(integerNumberProperty->integerNumberProperty.index(true)))
        )); // 酒店排名 支持搜索
        documentMap.put("brand",Property.of(
                property -> property.keyword(KeywordProperty.of(keywordProperty-> keywordProperty.index(true).copyTo("all")))

        )); // 酒店品牌
        documentMap.put("city",Property.of(
                property -> property.keyword(KeywordProperty.of(keywordProperty-> keywordProperty.index(true)))
        )); // 所在城市
        documentMap.put("starName",Property.of(
                property -> property.keyword(KeywordProperty.of(keywordProperty-> keywordProperty.index(true)))
        )); // 酒店星级
        documentMap.put("business",Property.of(
                property -> property.keyword(KeywordProperty.of(keywordProperty-> keywordProperty.index(true).copyTo("all")))
        )); // 商圈
        documentMap.put("location",Property.of(
                property -> property.geoPoint(GeoPointProperty.of(geoPointProperty-> geoPointProperty))

        )); // 酒店经纬度位置
        documentMap.put("pic",Property.of(
                property -> property.keyword(KeywordProperty.of(keywordProperty-> keywordProperty.index(false)))
        )); // 酒店图片
        documentMap.put("all",Property.of(
                property -> property.text(TextProperty.of(textProperty-> textProperty.index(true).analyzer("ik_max_word")))
        ));
        elasticsearchClient.indices()
                .create(createIndexMapping -> createIndexMapping.index(INDEX_HOTEL)
                        .mappings(mapping->mapping.properties(documentMap)));
    }



    @Test
    public void createHotelMapping() throws IOException {
        // keyword类型
        Property keywordProperty = Property.of(o -> o.keyword(kBuilder -> kBuilder));
        // keyword类型CopyTo
        Property keywordPropertyCopyTo = Property.of(o -> o.keyword(kBuilder -> kBuilder.copyTo("all")));
        // keyword类型不创建索引
        Property keywordPropertyFalseIndex = Property.of(o -> o.keyword(kBuilder -> kBuilder.index(false)));
        // text类型分词 搜索分词
        Property textProperty = Property.of(o -> o.text(tBuilder -> tBuilder.analyzer("ik_max_word").searchAnalyzer("ik_max_word")));
        // text类型分词CopyTo 搜索分词
        Property textPropertyCopyTo = Property.of(o -> o.text(tBuilder -> tBuilder.copyTo("all").analyzer("ik_max_word").searchAnalyzer("ik_max_word")));
        // integer类型
        Property integerProperty = Property.of(o -> o.integer(iBuilder -> iBuilder));
        // long类型
        Property longProperty = Property.of(o -> o.long_(lBuilder -> lBuilder));
        // geoPoint类型
        Property geoPointProperty = Property.of(o -> o.geoPoint(lBuilder -> lBuilder));
        // date类型
        Property dateProperty = Property.of(o -> o.date(dBuilder -> dBuilder.format("yyyy-MM-dd HH:mm:ss")));

        Map<String, Property> esDTO = new HashMap<>();
        esDTO.put("id", longProperty);
        esDTO.put("name", textPropertyCopyTo);
        esDTO.put("address", keywordPropertyFalseIndex);
        esDTO.put("price", integerProperty);
        esDTO.put("rating", integerProperty);
        esDTO.put("brand", keywordPropertyCopyTo);
        esDTO.put("city", keywordPropertyCopyTo);
        esDTO.put("starName", keywordPropertyCopyTo);
        esDTO.put("business", keywordProperty);
        esDTO.put("location", geoPointProperty);
        esDTO.put("pic", keywordPropertyFalseIndex);
        esDTO.put("all", textProperty);

        RestClient restClient = RestClient.builder(new HttpHost(host, 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(transport);
        elasticsearchClient.indices()
                .create(createHotelMapping-> createHotelMapping.index("jd").mappings(map->map.properties(esDTO)));

    }


    /**
     * 根据指定的ID 新增文档信息
     */
    @Test
    public void addDocumentInfoById() throws IOException { // POST /hotel/_doc/60223
        final Hotel hotel = hotelMapper.selectById(60223);
        RestClient restClient = RestClient.builder(new HttpHost(host,9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(transport);
        elasticsearchClient.index(
                index-> index.id(hotel.getId().toString())
                        .index(INDEX_HOTEL)
                        .document(hotel)
        );
    }

    /**
     *  根据指定的ID 索引名 查询文档信息
     * @throws IOException
     */
    @Test
    public  void selectDocumentInfoById() throws IOException { // GET /hotel/_doc/60223
        RestClient restClient = RestClient.builder(new HttpHost(host,9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        ElasticsearchClient elasticsearchClient = new ElasticsearchClient(transport);
         HotelDoc hotelDoc = new ClassesCollection<HotelDoc>().getDocumentById(elasticsearchClient, INDEX_HOTEL, String.valueOf(60223), HotelDoc.class);
         log.info("根据指定的ID 索引名 查询文档信息  {}",hotelDoc);

    }






}
