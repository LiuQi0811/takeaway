package com.code.takeaway.pool;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 11:09
 */
@Slf4j
public class ElasticSearchClientPoolFactory implements PooledObjectFactory<ElasticsearchClient> {
    @Override
    public void activateObject(PooledObject<ElasticsearchClient> pooledObject) throws Exception {
        ElasticsearchClient elasticsearchClient = pooledObject.getObject();
        log.info("客户端对象被激活了 {}",elasticsearchClient);
    }

    @Override
    public void destroyObject(PooledObject<ElasticsearchClient> pooledObject) throws Exception {
        ElasticsearchClient elasticsearchClient = pooledObject.getObject();
        log.info("客户端对象被销毁了 {}",elasticsearchClient);
    }

    @Override
    public PooledObject<ElasticsearchClient> makeObject() throws Exception {
//        String esServerHost = "ip:port,ip:port"; //定义地址格式
        String esServerHost = "192.168.0.118:9200"; //定义地址格式
        List<HttpHost> httpHosts = new ArrayList<>(); //定义地址空列表
        List<String> ips = Arrays.asList(esServerHost.split(","));
        log.info("IPS {}",ips);
        for (String ip : ips) {
            log.info("IP {}",ip);
            httpHosts.add(new HttpHost(ip.substring(0, ip.indexOf(":")), Integer.parseInt(ip.substring(ip.indexOf(":") + 1)), "http"));
        }
        log.info("HTTPHOSTS {}",httpHosts);
        RestClient restClient = RestClient.builder(httpHosts.toArray(new HttpHost[0])).build(); //创建低级客户端
        ElasticsearchTransport restClientTransport = new RestClientTransport(restClient, new JacksonJsonpMapper());//使用Jackson映射器创建传输层
        ElasticsearchClient client = new ElasticsearchClient(restClientTransport); // 创建Api 客户端
        log.info("客户端对象被创建了 {}", client);
        return new DefaultPooledObject(client);
    }

    @Override
    public void passivateObject(PooledObject<ElasticsearchClient> pooledObject) throws Exception {
        ElasticsearchClient elasticsearchClient = pooledObject.getObject();
        log.info("客户端对象被钝化了 {}" ,elasticsearchClient);
    }

    @Override
    public boolean validateObject(PooledObject<ElasticsearchClient> pooledObject) {
        return true;
    }
}
