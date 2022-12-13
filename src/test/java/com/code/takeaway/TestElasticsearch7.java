package com.code.takeaway;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.Transport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.code.takeaway.pool.ElasticSearchClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 10:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TakeawayApplication.class)
@Slf4j
public class TestElasticsearch7 {
    private static final String INDEX_HOTEL = "hotel";

    private void createClient() throws IOException { //创建低级客户端
        RestClient restClient = RestClient.builder(new HttpHost("172.16.100.26", 9200)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());// 使用Jackson映射器创建传输层
        ElasticsearchClient client = new ElasticsearchClient(transport);//创建API客户端
        restClient.close();
        transport.close();
    }

    @Test
    public void testCreate() throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient(); //获取连接对象
        Boolean acknowledged = client.indices().create(val -> val.index(INDEX_HOTEL)).acknowledged();
        log.info("创建索引返回状态 {}",acknowledged);
        ElasticSearchClientPool.backReturnClient(client);//归还连接对象
    }


}

