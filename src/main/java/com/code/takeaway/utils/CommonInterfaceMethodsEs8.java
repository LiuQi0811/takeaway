package com.code.takeaway.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.IntegerNumberProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.code.takeaway.pool.ElasticSearchClientPool;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 12:56
 * ElasticSearch 8 接口封装
 */
@Slf4j
public class CommonInterfaceMethodsEs8 {

    /**
     * 创建es索引
     *
     * @param indexName
     */
    public Boolean createIndex(String indexName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(v -> v.index(indexName)); // 创建索引
        Boolean acknowledged = createIndexResponse.acknowledged(); // 返回状态
        log.info("创建es索引 返回状态 {}", acknowledged);
        ElasticSearchClientPool.backReturnClient(elasticsearchClient); //归还线程池
        return acknowledged;
    }

    /**
     * 创建es索引 指定映射
     *
     * @param indexName
     * @param documentMap
     * @return
     */
    public Boolean createIndex(String indexName, Map<String, Property> documentMap) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();//获取客户端连接
        CreateIndexResponse createIndexResponse = elasticsearchClient
                .indices()
                .create(createIndexBuilder -> createIndexBuilder
                        .index(indexName)
                        .mappings(mappings ->
                                mappings.
                                        properties(documentMap)));
        Boolean acknowledged = createIndexResponse.acknowledged(); // 返回状态
        log.info("创建es索引 指定映射 返回状态 {}", acknowledged);
        ElasticSearchClientPool.backReturnClient(elasticsearchClient); //归还线程池
        return acknowledged;

    }

    /**
     * 文档
     *
     * @return
     */
    public Map<String, Property> document() {
        Map<String, Property> documentMap = new HashMap<>();
        documentMap.put("username", Property.of(property -> property.text(
                TextProperty.of(textProperty -> textProperty.index(true).analyzer("ik_max_word"))
        )));
        documentMap.put("age", Property.of(property -> property.integer(
                IntegerNumberProperty.of(integerNumberProperty -> integerNumberProperty.index(true))
        )));
        return documentMap;
    }

    /**
     * 校验索引是否存在
     *
     * @param elasticsearchClient ES客户端对象
     * @param indexName           索引名称
     * @return
     */
    public Boolean existsIndex(ElasticsearchClient elasticsearchClient, String indexName) throws IOException {
        boolean flag = elasticsearchClient
                .indices()
                .exists(ex -> ex.index(indexName)).value();
        return flag;
    }

    /**
     * 校验索引是否存在 批量
     *
     * @param elasticsearchClient
     * @param indexNames
     * @return
     * @throws IOException
     */
    public Boolean existsIndex(ElasticsearchClient elasticsearchClient, List<String> indexNames) throws IOException {
        boolean flag = elasticsearchClient
                .indices()
                .exists(ex -> ex.index(indexNames)).value();
        return flag;
    }

    /**
     * 删除es索引
     *
     * @param indexName
     * @return
     */
    public Boolean deleteIndex(String indexName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient(); //获取客户端连接
        Boolean existsIndex = existsIndex(elasticsearchClient, indexName); //判断当前索引是否存在
        if (!existsIndex) {  // 不存在 返回false
            return false;
        }
        boolean acknowledged = elasticsearchClient.indices().delete(del -> del.index(indexName)).acknowledged();
        log.info("删除es索引  返回状态 {}", acknowledged);
        ElasticSearchClientPool.backReturnClient(elasticsearchClient); //归还线程池
        return acknowledged;
    }

    /**
     * 删除es索引批量
     *
     * @param indexNames
     * @return
     * @throws Exception
     */
    public Boolean deleteIndex(List<String> indexNames) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient(); //获取客户端连接
        Boolean existsIndex = existsIndex(elasticsearchClient, indexNames); //判断当前索引是否存在
        if (!existsIndex) {  // 不存在 返回false
            return false;
        }
        boolean acknowledged = elasticsearchClient.indices().delete(del -> del.index(indexNames)).acknowledged();
        log.info("删除es索引批量  返回状态 {}", acknowledged);
        ElasticSearchClientPool.backReturnClient(elasticsearchClient); //归还线程池
        return acknowledged;
    }

    /**
     * 查看索引信息 根据索引名
     *
     * @param indexName
     * @return
     * @throws Exception
     */
    public Map<String, IndexState> getIndexInfo(String indexName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();//获取客户端连接
        GetIndexResponse getIndexResponse = elasticsearchClient
                .indices()
                .get(info -> info.index(indexName));
        Map<String, IndexState> result = getIndexResponse.result(); //获取信息
        ElasticSearchClientPool.backReturnClient(elasticsearchClient); //归还线程池
        return result;
    }

    /**
     * 查看全部索引信息
     *
     * @return
     */
    public List<IndicesRecord> getAllIndex() throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();//获取客户端连接
        List<IndicesRecord> indicesRecords = elasticsearchClient
                .cat()
                .indices()
                .valueBody();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient); //归还线程池
        return indicesRecords;
    }

}
