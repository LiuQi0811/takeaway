package com.code.takeaway.classes;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.code.takeaway.pool.ElasticSearchClientPool;

import java.lang.reflect.Field;
import java.util.List;

/*
 *@ClassName ClassesCollection
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/13 21:32
 *@Version 1.0
 */
public class ClassesCollection<T> {

    /**
     * 根据ID 索引名 获取文档信息
     *
     * @param elasticsearchClient
     * @param indexName
     * @param id
     * @param targetClass
     * @param <T>
     * @return
     */
    public <T> T getDocumentById(ElasticsearchClient elasticsearchClient, String indexName, String id, Class<T> targetClass) {
        GetResponse<T> response = null;
        try {
            response = elasticsearchClient.get(
                    val -> val
                            .index(indexName)
                            .id(id), targetClass
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.handleResponse(response);
    }

    private <T> T handleResponse(GetResponse<T> response) {
        if (response == null) {
            return null;
        }
        return response.source();
    }


    /**
     * 根据ID 索引名 获取文档信息
     *
     * @param elasticsearchClient
     * @param indexName
     * @param id
     * @param targetClass
     * @param <T>
     * @return
     */
    public <T> void updateDocumentById(ElasticsearchClient elasticsearchClient, String indexName, String id, Object o, Class<T> targetClass) {
        try {
            elasticsearchClient.update(val -> val
                    .index(indexName)
                    .id(id)
                    .doc(o), targetClass);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *  批量导入文档数据
     * @param elasticsearchClient
     * @param indexName
     * @param list
     * @param <T>
     */
    public <T> void batchImportDocumentBuild(ElasticsearchClient elasticsearchClient, String indexName, List<T> list) {
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();
        try {
            for (T ls : list) {
                // 通过反射 获取id属性
                Field[] declaredFields = ls.getClass().getDeclaredFields();
                // 设置对象的访问权限，保证对private的属性的访问
                declaredFields[0].setAccessible(true);
                String id = declaredFields[0].get(ls).toString();
                bulkRequest.operations(operation -> operation
                        .index(index -> index
                                .index(indexName)
                                .id(id)
                                .document(ls)
                        )
                );

            }
            elasticsearchClient.bulk(bulkRequest.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
