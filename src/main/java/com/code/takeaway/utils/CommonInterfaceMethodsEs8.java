package com.code.takeaway.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.IntegerNumberProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.elasticsearch.indices.get_alias.IndexAliases;
import com.code.takeaway.pool.ElasticSearchClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    /**
     * 添加别名
     *
     * @param indexName 索引名称
     * @param aliasName 别名名称
     * @return
     * @throws IOException
     */
    public Boolean addAliases(List<String> indexName, String aliasName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        UpdateAliasesResponse updateAliasesResponse = elasticsearchClient
                .indices()
                .updateAliases(update -> update
                        .actions(action -> action
                                .add(add -> add
                                        .indices(indexName)
                                        .alias(aliasName)
                                )
                        )
                );
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        boolean acknowledged = updateAliasesResponse.acknowledged();
        log.info("添加别名  返回状态 {}", acknowledged);
        return acknowledged;
    }

    /**
     * 移除别名
     *
     * @param indexName
     * @param aliasName
     * @throws IOException
     */
    public Boolean removeAliases(List<String> indexName, String aliasName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        DeleteAliasResponse deleteAliasResponse = elasticsearchClient
                .indices()
                .deleteAlias(del -> del
                        .index(indexName)
                        .name(aliasName)
                );
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        boolean acknowledged = deleteAliasResponse.acknowledged();
        log.info("移除别名  返回状态 {}", acknowledged);
        return acknowledged;
    }


    /**
     * 重命名别名，解除旧索引的别名，填加新索引的别名
     *
     * @param indexName
     * @param newAliasName
     * @param oldAliasName
     * @return
     * @throws Exception
     */
    public Boolean renameAliases(List<String> indexName, String newAliasName, String oldAliasName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        DeleteAliasResponse deleteAliasResponse = null;
        if (StringUtils.isNotBlank(oldAliasName)) {
            deleteAliasResponse = elasticsearchClient.indices().deleteAlias(del -> del
                    .index(indexName)
                    .name(oldAliasName)
            ); //删除别名
        }
        if (!deleteAliasResponse.acknowledged()) return false;
        UpdateAliasesResponse updateAliasesResponse = null;
        if (StringUtils.isNotBlank(oldAliasName)) {
            updateAliasesResponse = elasticsearchClient.indices().updateAliases(update -> update.actions(
                            action -> action.add(add -> add
                                    .indices(indexName)
                                    .alias(newAliasName))
                    )
            ); //修改别名
        }
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        boolean acknowledged = updateAliasesResponse.acknowledged();
        log.info("重命名别名  返回状态 {}", acknowledged);
        return acknowledged;
    }

    /**
     * 根据别名查询索引信息
     *
     * @param aliasName
     * @return
     * @throws Exception
     */
    public Map<String, IndexAliases> getIndexInfoByAlias(String aliasName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        GetAliasResponse getAliasResponse = elasticsearchClient.indices().getAlias(
                alias -> alias.name(aliasName)
        );
        Map<String, IndexAliases> result = getAliasResponse.result();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return result;
    }

    /**
     * 根据别名查询索引名称
     *
     * @param aliasName
     * @return
     * @throws Exception
     */
    public List<String> getIndexNameByAliasName(String aliasName) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        GetAliasResponse getAliasResponse = elasticsearchClient.indices().getAlias(
                alias -> alias.name(aliasName)
        );
        Map<String, IndexAliases> result = getAliasResponse.result();
        List<String> indexList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            indexList = result.keySet().stream().collect(Collectors.toList());
        }
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);

        return indexList;
    }

    /**
     * 新增文档信息
     *
     * @param indexName
     * @param o
     * @return
     * @throws Exception
     */
    public Long createDocument(String indexName, Object o) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        IndexResponse indexResponse = elasticsearchClient.index(item -> item.index(indexName).document(o));
        long version = indexResponse.version();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return version;
    }

    /**
     * 新增文档信息 指定id
     *
     * @param indexName
     * @param id
     * @param o
     * @return
     * @throws Exception
     */
    public Long createDocument(String indexName, String id, Object o) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        IndexResponse indexResponse = elasticsearchClient
                .index(item -> item
                        .index(indexName)
                        .id(id)
                        .document(o));
        long version = indexResponse.version();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return version;
    }

    /**
     * 修改文档自定义属性
     *
     * @param indexName
     * @param id
     * @param o
     * @return
     * @throws Exception
     */
    public Long updateDocument(String indexName, String id, Object o) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        UpdateResponse<Object> updateResponse = elasticsearchClient.update(x -> x
                .index(indexName)
                .id(id)
                .doc(o), Object.class);
        long version = updateResponse.version();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return version;
    }

    /**
     * bulk批量插入
     *
     * @param indexName
     * @param objects
     * @return
     * @throws Exception
     */
    public List<BulkResponseItem> bulkInsert(String indexName, List<Object> objects) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        List<BulkOperation> bulkOperations = new ArrayList<>();//创建BulkOperation列表准备批量插入doc
        //将user中id作为es id，也可不指定id es会自动生成id
        objects.forEach(doc -> bulkOperations
                .add(BulkOperation
                        .of(bulk -> bulk.index(docs -> docs
                                .document(doc)))
                )
        );
        BulkResponse bulkResponse = elasticsearchClient
                .bulk(bulk -> bulk
                        .index(indexName)
                        .operations(bulkOperations)
                );
        List<BulkResponseItem> items = bulkResponse.items();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return items;
    }

    /**
     * bulk批量插入 指定id
     *
     * @param indexName
     * @param ids
     * @param objects
     * @return
     * @throws Exception
     */
    public List<BulkResponseItem> bulkInsert(String indexName, List<String> ids, List<Object> objects) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        List<BulkOperation> bulkOperations = new ArrayList<>(); //创建BulkOperation列表准备批量插入doc
        for (int i = 0; i < objects.size(); i++) {   //将id作为es id，也可不指定id es会自动生成id
            int finalI = i;
            bulkOperations.add(BulkOperation
                    .of(bulk -> bulk
                            .index(index -> index
                                    .id(ids.get(finalI))
                                    .document(objects.get(finalI)
                                    )
                            )
                    )
            );
        }
        BulkResponse bulk = elasticsearchClient
                .bulk(bul -> bul
                        .index(indexName)
                        .operations(bulkOperations)
                );
        List<BulkResponseItem> items = bulk.items();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return items;
    }


    /**
     * bulk批量删除文档记录
     *
     * @param indexName
     * @param ids
     * @return List<BulkResponseItem>
     * @throws Exception
     */
    public List<BulkResponseItem> delDocByIds(String indexName, List<String> ids) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();

        List<BulkOperation> bulkOperations = new ArrayList<>();// 构建批量操作对象BulkOperation的集合

        for (int i = 0; i < ids.size(); i++) {    // 向集合中添加需要删除的文档id信息
            int finalI = i;
            bulkOperations.add(BulkOperation.of(b -> b
                    .delete((del -> del
                            .index(indexName)
                            .id(ids.get(finalI)
                            )
                    ))
            ));
        }
        // 调用客户端的bulk方法，并获取批量操作响应结果
        BulkResponse response = elasticsearchClient
                .bulk(bul -> bul
                        .index(indexName)
                        .operations(bulkOperations));
        return response.items();
    }


    /**
     * bulk批量更新数据
     *
     * @param indexName
     * @param ids
     * @param objects
     * @return List<BulkResponseItem> items
     * @throws Exception
     */
    public List<BulkResponseItem> bulkUpdate(String indexName, List<String> ids, List<Object> objects) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        //创建BulkOperation列表准备批量插入doc
        List<BulkOperation> bulkOperations = new ArrayList<>();
        //将id作为es id，也可不指定id es会自动生成id
        for (int i = 0; i < objects.size(); i++) {
            int finalI = i;
            //TODO 没测试不知对不对
            bulkOperations.add(BulkOperation.of(b -> b
                    .update(u -> u
                            .index(indexName)
                            .id(ids.get(finalI))
                            .action(a -> a
                                    .doc(objects.get(finalI))
                            ))
            ));
        }
        BulkResponse bulk = elasticsearchClient
                .bulk(bul -> bul
                        .index(indexName)
                        .operations(bulkOperations));
        List<BulkResponseItem> items = bulk.items();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return items;
    }


}
