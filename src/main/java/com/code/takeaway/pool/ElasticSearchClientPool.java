package com.code.takeaway.pool;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 11:32
 */
@Slf4j
public class ElasticSearchClientPool {
    private static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig(); //对象池配置类，不写采用默认配置

    //采用默认配置maxTotal是8，池中有8个client
    static {
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(200);
        poolConfig.setMinEvictableIdleTimeMillis(1000L * 3L);
    }

    private static ElasticSearchClientPoolFactory clientPoolFactory = new ElasticSearchClientPoolFactory(); //池化的对象的工厂类，实现类
    private static GenericObjectPool<ElasticsearchClient> clientGenericObjectPool = new GenericObjectPool<>(clientPoolFactory, poolConfig);// 利用对象工厂类和配置类生成对象池

    /**
     * 从线程池 获取
     * @return
     * @throws Exception
     */
    public static ElasticsearchClient getClient() throws Exception {
        ElasticsearchClient elasticsearchClient = clientGenericObjectPool.borrowObject();
        log.info("从线程池里面取一个对象 数据 {}", elasticsearchClient);
        return elasticsearchClient;

    }

    /**
     * 归还 线程池
     * @param elasticsearchClient
     */
    public static void backReturnClient(ElasticsearchClient elasticsearchClient) {
        clientGenericObjectPool.returnObject(elasticsearchClient);
    }

}
