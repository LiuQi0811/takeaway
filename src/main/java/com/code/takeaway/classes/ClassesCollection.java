package com.code.takeaway.classes;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import com.code.takeaway.pool.ElasticSearchClientPool;

/*
 *@ClassName ClassesCollection
 *@Description TODO
 *@Author LiuQi
 *@Date 2022/12/13 21:32
 *@Version 1.0
 */
public class ClassesCollection<T>
{

    /**
     * 根据ID 索引名 获取文档信息
     * @param elasticsearchClient
     * @param indexName
     * @param id
     * @param targetClass
     * @param <T>
     * @return
     */
    public <T> T getDocumentById(ElasticsearchClient elasticsearchClient, String indexName, String id, Class<T> targetClass)
    {
        GetResponse<T> response = null;
        try {
           response =  elasticsearchClient.get(
                    val -> val
                            .index(indexName)
                            .id(id),targetClass
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return this.handleResponse(response);
    }

    private <T> T handleResponse(GetResponse<T> response){
        if(response == null){
            return null;
        }
        return response.source();
    }



















}
