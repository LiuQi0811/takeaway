package com.code.takeaway.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import com.code.takeaway.pool.ElasticSearchClientPool;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LiuQi
 * @version 1.0
 * @data 2022/12/13 15:08
 */
public class SearchEs8
{
    /**
     * 查询全部数据
     *
     * @param indices
     * @return Object
     * @throws Exception
     */
    public Object advancedQueryFromAllData(String indices) throws Exception {
        ElasticsearchClient elasticsearchClient = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = elasticsearchClient.search(s -> s.index(indices).query(q -> q.matchAll(m -> m)),
                Object.class);
        HitsMetadata<Object> hitsMetadata = searchResponse.hits();
        hitsMetadata.total();
        List<Hit<Object>> hits = hitsMetadata.hits();
        Object source = hits.get(0).source();
        ElasticSearchClientPool.backReturnClient(elasticsearchClient);
        return source;
    }

    /**
     * term匹配 多次匹配
     *
     * @param index
     * @param field
     * @param fieldValues
     * @return Object
     * @throws Exception
     */
    public Object advancedQueryByTerm(String index, String field,  List<FieldValue> fieldValues) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> user_test = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .terms(t -> t
                                        .field("field")
                                        .terms(terms -> terms
                                                .value(fieldValues)
                                        )
                                )
                        )
                        .query(q -> q
                                .matchAll(m -> m))
                , Object.class);
        HitsMetadata<Object> hits = user_test.hits();
        TotalHits total = hits.total();
        List<Hit<Object>> hits1 = hits.hits();
        Object source = hits1.get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * term匹配 单次匹配
     *
     * @param index
     * @param field
     * @param value
     * @return Object
     * @throws Exception
     */
    public Object advancedQueryByTerm(String index, String field, long value) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> user_test = client.search(e -> e
                        .index(index)
                        .query(q -> q.term(t -> t.field(field).value(value)))
                        .query(q -> q
                                .matchAll(m -> m))
                , Object.class);
        HitsMetadata<Object> hits = user_test.hits();
        TotalHits total = hits.total();
        List<Hit<Object>> hits1 = hits.hits();
        Object source = hits1.get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * 分页查询
     *
     * @param index
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object advancedQueryByPage(String index, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .matchAll(m -> m)
                        )
                        .from(from)
                        .size(size)
                , Object.class);
//        HitsMetadata<Object> hits = searchResponse.hits();
//        List<Hit<Object>> hits1 = hits.hits();
//        Object source = hits1.get(0).source();
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;

    }
    /**
     * 排序查询
     *
     * @param index
     * @param field
     * @param order SortOrder.Desc/SortOrder.Asc
     * @return
     * @throws Exception
     */
    public Object advancedQueryBySort(String index, String field, SortOrder order) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .matchAll(m -> m)
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(field)
                                        .order(order)
                                )
                        )
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * must should匹配
     *
     * @param matchQueryBuilderList
     * @param indices
     * @return
     * @throws Exception
     */
    /**
     * @param index
     * @param field
     * @param order
     * @return
     * @throws Exception
     */
    public Object advancedQueryByShould(String index, String field, SortOrder order) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .bool(b -> b
                                        .must(must -> must
                                                .match(m -> m
                                                        .field("age")
                                                        .query(30)
                                                )
                                        )
                                        .must(must -> must
                                                .match(m -> m
                                                        .field("sex")
                                                        .query("男")
                                                )
                                        )
                                        .should(should -> should
                                                .match(m -> m
                                                        .field("age")
                                                        .query(30)
                                                )
                                        )
                                        .should(should -> should
                                                .match(m -> m
                                                        .field("age")
                                                        .query(40)
                                                )
                                        )
                                )
                        )
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }
    /**
     * filter过滤查询
     *
     * @param index
     * @param field
     * @param includes includes代表白名单，只返回指定的字段
     * @param excludes excludes代表黑名单，不返回指定的字段
     * @param order
     * @return
     * @throws Exception
     */
    public Object advancedQueryByFilter(String index, String field, String includes, String excludes, SortOrder order) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .matchAll(m -> m)
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(field)
                                        .order(order)
                                )
                        )
                        .source(source -> source
                                .filter(f -> f
                                        .includes(includes)
                                        .excludes(excludes)
                                )
                        )
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }


    /**
     * 模糊查询
     *
     * @param index
     * @param field
     * @param value
     * @param fuzziness fuzziness代表可以与关键词有误差的字数，可选值为0、1、2这三项
     * @return
     * @throws Exception
     */
    public Object advancedQueryByLike(String index, String field, String value, String fuzziness) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field(field)
                                        .value(value)
                                        .fuzziness(fuzziness))
                        )
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }


    /**
     * 高亮 默认黄色
     *
     * @param index
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    public Object advancedQueryByHighLight(String index, String field, String value) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .term(t -> t
                                        .field(field)
                                        .value(value)
                                )
                        )
                        .highlight(h -> h
                                .fields(field, f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                , Object.class);

        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * 高亮 自定义颜色
     *
     * @param index
     * @param field
     * @param value
     * @param color
     * @return
     * @throws Exception
     */
    public Object advancedQueryByHighLight(String index, String field, String value, Color color) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .term(t -> t
                                        .field(field)
                                        .value(value)
                                )
                        )
                        .highlight(h -> h
                                .fields(field, f -> f
                                        .preTags("<font color='" + color + "'>")
                                        .postTags("</font>")
                                )
                        )
                , Object.class);

        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * 自定义高亮
     *
     * @param index
     * @param field
     * @param value
     * @param highlightMap
     * @return
     * @throws Exception
     */
    public Object advancedQueryByHighLight(String index, String field, String value, Map<String, HighlightField> highlightMap) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .query(q -> q
                                .term(t -> t
                                        .field(field)
                                        .value(value)
                                )
                        )
                        .highlight(h -> h
                                .fields(highlightMap)
                        )
                , Object.class);

        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * 极值聚合查询
     *
     * @param index
     * @param field
     * @param maxField
     * @param highlightMap
     * @return
     * @throws Exception
     */
    public Object advancedQueryByMax(String index, String field, String maxField, Map<String, HighlightField> highlightMap) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .aggregations(maxField, a -> a
                                .max(m -> m
                                        .field(field)
                                )
                        )
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        for (Map.Entry<String, Aggregate> entry : searchResponse.aggregations().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().max().value());
        }
        return source;
    }

    /**
     * 分组查询
     *
     * @param index
     * @param field
     * @return
     * @throws Exception
     */
    public Aggregate searchStationLineChart(String index, String field) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(index)
                        .size(100)
                        .aggregations("group", a -> a
                                .terms(t -> t
                                        .field(field)
                                )
                        )
                , Object.class);
        System.out.println(searchResponse.took());
        System.out.println(searchResponse.hits().total().value());
        searchResponse.hits().hits().forEach(e -> {
            System.out.println(e.source().toString());
        });
        Aggregate aggregate = searchResponse.aggregations().get("group");
        LongTermsAggregate lterms = aggregate.lterms();
        Buckets<LongTermsBucket> buckets = lterms.buckets();
        for (LongTermsBucket b : buckets.array()) {
            System.out.println(b.key() + " : " + b.docCount());
        }

        //Object source = searchResponse.hits().hits().get(0).source();
        //ElasticSearchClientPool.backReturnClient(client);

        return aggregate;
    }

    /**
     * Term匹配
     * @param indexName
     * @param field
     * @param value
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryTerm(String indexName, String field, Object value, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .term(t -> t
                                        .field(field)
                                        .value(String.valueOf(value))
                                )
                        )
                        .highlight(h -> h
                                .fields(String.valueOf(value), f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(field)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * Terms匹配
     * @param indexName
     * @param field
     * @param values
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryTerms(String indexName, String field, List<FieldValue> values, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .terms(t -> t
                                        .field("field")
                                        .terms(terms -> terms
                                                .value(values)
                                        )
                                )
                        )
                        .highlight(h -> h
                                .fields(field, f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(field)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * matchAll全部
     * @param indexName
     * @param field
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryMatchAll(String indexName, String field, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .matchAll(m -> m)
                        )
                        .highlight(h -> h
                                .fields(field, f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(field)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * multiMatch多field匹配
     * @param indexName
     * @param fields
     * @param query
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryMultiMatch(String indexName, List<String> fields, String query, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .multiMatch(m -> m
                                        .fields(fields)
                                        .query(query)
                                )
                        )
                        .highlight(h -> h
                                .fields(query, f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * matchPhrase短语匹配
     * @param indexName
     * @param field
     * @param query
     * @param slop 间隔字符数量
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryMatchPhrase(String indexName, String field, String query, Integer slop, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .matchPhrase(m -> m
                                        .field(field)
                                        .query(query)
                                        .slop(slop)
                                )
                        )
                        .highlight(h -> h
                                .fields(query, f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * must必须匹配
     * @param indexName
     * @param field
     * @param query
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryBoolMust(String indexName, String field, Object query, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .bool(bool -> bool
                                        .must(must -> must
                                                .match(match -> match
                                                        .field(field)
                                                        .query(String.valueOf(query))
                                                )
                                        )
                                )
                        )
                        .highlight(h -> h
                                .fields(String.valueOf(query), f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * mustNot必须不匹配
     * @param indexName
     * @param field
     * @param query
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryBoolMustNot(String indexName, String field, Object query, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .bool(bool -> bool
                                        .mustNot(mustNot -> mustNot
                                                .match(match -> match
                                                        .field(field)
                                                        .query(String.valueOf(query))
                                                )
                                        )
                                )
                        )
                        .highlight(h -> h
                                .fields(String.valueOf(query), f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * should选择性匹配
     * @param indexName
     * @param field
     * @param query
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryBoolShould(String indexName, String field, Object query, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .bool(bool -> bool
                                        .should(should -> should
                                                .match(match -> match
                                                        .field(field)
                                                        .query(String.valueOf(query))
                                                )
                                        )
                                )
                        )
                        .highlight(h -> h
                                .fields(String.valueOf(query), f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * filter过滤子句，必须匹配
     * @param indexName
     * @param field
     * @param query
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryBoolFilter(String indexName, String field, Object query, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .bool(bool -> bool
                                        .filter(filter -> filter
                                                .match(match -> match
                                                        .field(field)
                                                        .query(String.valueOf(query))
                                                )
                                        )
                                )
                        )
                        .highlight(h -> h
                                .fields(String.valueOf(query), f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * Range范围匹配
     * @param indexName
     * @param field
     * @param gt 大于
     * @param lt 小于
     * @param gte 大于等于
     * @param lte 小于等于
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryBoolRange(String indexName, String field, Object gt, Object lt, Object gte, Object lte, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .filter(f -> f
                                                .range(r -> r
                                                        .field(field)
                                                        .gt((Objects.isNull(gt) ? null : JsonData.of(gt)))
                                                        .lt((Objects.isNull(lt) ? null : JsonData.of(lt)))
                                                        .gte((Objects.isNull(gte) ? null : JsonData.of(gte)))
                                                        .lte((Objects.isNull(lte) ? null : JsonData.of(lte)))
                                                )
                                        )
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * filter过滤
     * @param indexName
     * @param excludes
     * @param includes
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryFilter(String indexName, List<String> excludes, List<String> includes, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .matchAll(m -> m)
                        )
                        .source(s -> s
                                .filter(f -> f
                                        .excludes(excludes)
                                        .includes(includes)
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * fuzzy模糊匹配
     * @param indexName
     * @param field
     * @param value
     * @param fuzziness
     * @param sortField
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryFuzzy(String indexName, String field, String value, String fuzziness, String sortField, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(q -> q
                                .fuzzy(f -> f
                                        .field(field)
                                        .value(value)
                                        .fuzziness(fuzziness))
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(sortField)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * SearchRequest原生查询
     * @param searchRequest
     * @return
     * @throws Exception
     */
    public Object queryOriginal(SearchRequest searchRequest) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        //Query of = Query.of(q -> q.bool(b -> b.must(m -> m.match(match -> match.field("").query("")))));
        SearchResponse<Object> searchResponse = client.search(searchRequest, Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }

    /**
     * Query原生查询
     * @param indexName
     * @param field
     * @param query
     * @param order
     * @param from
     * @param size
     * @return
     * @throws Exception
     */
    public Object queryOriginal(String indexName, String field, Query query, SortOrder order, Integer from, Integer size) throws Exception {
        ElasticsearchClient client = ElasticSearchClientPool.getClient();
        //Query of = Query.of(q -> q.bool(b -> b.must(m -> m.match(match -> match.field("").query("")))));
        SearchResponse<Object> searchResponse = client.search(e -> e
                        .index(indexName)
                        .query(query)
                        .highlight(h -> h
                                .fields(field, f -> f
                                        .preTags("<font color='yellow'>")
                                        .postTags("</font>")
                                )
                        )
                        .sort(sort -> sort
                                .field(f -> f
                                        .field(field)
                                        .order(order)
                                )
                        )
                        .from(from)
                        .size(size)
                , Object.class);
        Object source = searchResponse.hits().hits().get(0).source();
        ElasticSearchClientPool.backReturnClient(client);
        return source;
    }



}
