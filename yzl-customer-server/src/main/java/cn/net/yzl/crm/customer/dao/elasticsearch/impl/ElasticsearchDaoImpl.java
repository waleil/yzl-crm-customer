package cn.net.yzl.crm.customer.dao.elasticsearch.impl;

import cn.net.yzl.crm.customer.config.ESConfig;
import cn.net.yzl.crm.customer.config.RestHighLevelClientConfig;
import cn.net.yzl.crm.customer.dao.elasticsearch.ElasticsearchDao;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author lichanghong
 * @version 1.0
 * @title: ElasticsearchDaoImpl
 * @description todo
 * @date: 2021/2/22 9:12 下午
 */
@ConditionalOnBean(value = RestHighLevelClientConfig.class)
@Component
@Slf4j
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Override
    public String save(String indexName, @Nullable String id, String data) {
        try {
            IndexRequest request = new IndexRequest(indexName);
            if (StringUtils.hasText(id)) {
                request.id(id);
            }
            request.source(data, XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            return response.getId();
        }catch (Exception ex){
            log.error("[ES索引]The exception was thrown in create index. name:{},id:{}. {} ",
                    indexName,
                    id,
                    ex);
        }
        return null;
    }

    @Override
    public List<String> saveBulk(String indexName, Map<String, String> datas) {

        try {
            BulkRequest requests = new BulkRequest();
            datas.forEach((id, data) -> {
                IndexRequest request = new IndexRequest(indexName);
                request.id(id);
                request.source(data, XContentType.JSON);
                requests.add(request);
            });

            BulkResponse bulkResponse = restHighLevelClient.bulk(requests, RequestOptions.DEFAULT);

            List<String> ids = Lists.newArrayList();
            BulkItemResponse[] responses = bulkResponse.getItems();
            for (BulkItemResponse response : responses) {
                ids.add(response.getId());
            }
            return ids;
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in create index bulk. name:{},data:{}. {} ",
                    indexName,
                    datas,
                    ex);
        }
        return null;
    }

    @Override
    public void saveAsync(String indexName, String id, String data, Function<String, Void> onSuccess, Function<String, Void> onFail) {
        try {
            IndexRequest indexRequest = new IndexRequest(indexName);
            indexRequest.id(id);
            indexRequest.source(data, XContentType.JSON);

            restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    if (null != onSuccess) {
                        onSuccess.apply(indexRequest.routing());
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    if (null != onFail) {
                        onFail.apply(indexRequest.routing());
                    }
                    log.error("[ES索引]Build index fail! cause :{}", Throwables.getStackTraceAsString(e));
                }
            });
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in create index async. name:{},id:{}. {} ",
                    indexName,
                    id,
                    ex);
        }
    }

    @Override
    public void saveAsyncBulk(String indexName, Map<String, String> datas, Function<List<String>, Void> onSuccess, Function<String, Void> onFail) {
        if (datas.size() == 0) return;
        try {
            BulkRequest requests = new BulkRequest();
            datas.forEach((id, data) -> {
                IndexRequest request = new IndexRequest(indexName);
                request.id(id);
                request.source(data, XContentType.JSON);
                requests.add(request);
            });
            restHighLevelClient.bulkAsync(requests, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {
                    if (null == onSuccess) {
                        return;
                    }

                    List<String> ids = Lists.newArrayList();
                    BulkItemResponse[] responses = bulkItemResponses.getItems();
                    for (BulkItemResponse response : responses) {
                        ids.add(response.getId());
                    }
                    onSuccess.apply(ids);
                }

                @Override
                public void onFailure(Exception e) {
                    onFail.apply(Throwables.getStackTraceAsString(e));
                }
            });
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in create index async and bulk. name:{},data:{}. ",
                    indexName,
                    datas,
                    ex);
        }
    }

    @Override
    public String update(String indexName, String id, String data) {
        try {
            UpdateRequest request = new UpdateRequest(indexName,  id)
                    .doc(data, XContentType.JSON);

            UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
            return response.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("[ES索引]The exception was thrown in update document method. name:{},id:{}. {} ",
                    indexName,
                    id,
                    ex);
        }
        return null;
    }

    @Override
    public List<String> updateBulk(String indexName, Map<String, String> datas) {
        try {
            BulkRequest requests = new BulkRequest();
            datas.forEach((id, data) -> {
                UpdateRequest request = new UpdateRequest(indexName, id)
                        .doc(data, XContentType.JSON);
                requests.add(request);
            });

            BulkResponse bulkResponse = restHighLevelClient.bulk(requests, RequestOptions.DEFAULT);

            List<String> ids = Lists.newArrayList();
            BulkItemResponse[] responses = bulkResponse.getItems();
            for (BulkItemResponse response : responses) {
                ids.add(response.getId());
            }

            return ids;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("[ES索引]The exception was thrown in bulk update document method. name:{},data:{}. {} ",
                    indexName,
                    datas,
                    ex);
        }
        return null;
    }

    @Override
    public void updateAsyncBulk(String indexName, Map<String, String> datas, Function<List<String>, Void> onSuccess, Function<String, Void> onFail) {
        if (datas.size() == 0) return;

        try {
            BulkRequest requests = new BulkRequest();
            datas.forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String id, Object data) {
                    UpdateRequest request = new UpdateRequest(indexName, id)
                            .doc(data, XContentType.JSON);
                    requests.add(request);
                }
            });

            restHighLevelClient.bulkAsync(requests, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {
                    if (null == onSuccess) {
                        return;
                    }

                    List<String> ids = Lists.newArrayList();
                    BulkItemResponse[] responses = bulkItemResponses.getItems();
                    for (BulkItemResponse response : responses) {
                        ids.add(response.getId());
                    }
                    onSuccess.apply(ids);
                }

                @Override
                public void onFailure(Exception e) {
                    onFail.apply(Throwables.getStackTraceAsString(e));
                }
            });
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in asyncAndBulk update doc method. name:{},data:{}. {} ",
                    indexName,
                    datas,
                    ex);
        }
    }

    @Override
    public boolean delete(String indexName, String docId) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, docId);
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

            return RestStatus.ACCEPTED.getStatus() == response.status().getStatus();
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in delete document method. name:{},docId:{}. {} ",
                    indexName,
                    docId,
                    ex);
        }
        return false;
    }

    @Override
    public boolean deleteBulk(String indexName, List<String> docIds) {
        try {
            BulkRequest requests = new BulkRequest();
            docIds.forEach(docId -> {
                DeleteRequest request = new DeleteRequest(indexName,  docId);
                requests.add(request);
            });

            BulkResponse bulkResponse = restHighLevelClient.bulk(requests, RequestOptions.DEFAULT);
            BulkItemResponse[] responses = bulkResponse.getItems();
            return responses.length > 0;
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in bulk delete document method. name:{},docIds:{}. {} ",
                    indexName,
                    docIds,
                    ex);
        }
        return false;
    }

    @Override
    public void deleteAsync(String indexName, String docId, Function<String, Void> onSuccess, Function<String, Void> onFail) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName, docId);
            restHighLevelClient.deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<DeleteResponse>() {
                @Override
                public void onResponse(DeleteResponse deleteResponse) {
                    if (null != onSuccess) {
                        onSuccess.apply(deleteRequest.routing());
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    if (null != onFail) {
                        onFail.apply(deleteRequest.routing());
                    }
                    log.error("[ES索引]Delete index fail! cause :{}", Throwables.getStackTraceAsString(e));
                }
            });
            return;
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in async delete document method. name:{},docId:{}. {} ",
                    indexName,
                    docId,
                    ex);
        }
    }

    @Override
    public void deleteAsyncBulk(String name, List<String> docIds, Function<List<String>, Void> onSuccess, Function<String, Void> onFail) {
        try {
            BulkRequest requests = new BulkRequest();
            docIds.forEach(docId -> {
                DeleteRequest request = new DeleteRequest(name,  docId);
                requests.add(request);
            });

            restHighLevelClient.bulkAsync(requests, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkItemResponses) {
                    if (null != onSuccess) {
                        List<String> ids = Lists.newArrayList();
                        BulkItemResponse[] responses = bulkItemResponses.getItems();
                        for (BulkItemResponse response : responses) {
                            ids.add(response.getId());
                        }
                        onSuccess.apply(ids);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    if (null != onFail) {
                        onFail.apply(Throwables.getStackTraceAsString(e));
                    }
                    log.error("[ES索引]Delete index fail! cause :{}", Throwables.getStackTraceAsString(e));
                }
            });
            return;
        } catch (Exception ex) {
            log.error("[ES索引]The exception was thrown in asyncAndBulk del doc method. name:{},docIds:{}. {} ",
                    name,
                    docIds,
                    ex);
        }
    }

    @Override
    public SearchResponse search(SearchRequest searchRequest, RequestOptions options) throws IOException {
        return  restHighLevelClient.search(searchRequest, options);
    }

    @Override
    public String searchById(String indexName, String id) {
        GetRequest request = new GetRequest(
                indexName,
                id);

        try {
            GetResponse  getResponse = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            return getResponse.getSourceAsString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(request.toString(), e);
        }
        return null;
    }

    @Override
    public long count(CountRequest request, RequestOptions options) {
        try {
            CountResponse response =restHighLevelClient.count(request, options);
            return response.getCount();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[ES索引]The exception was thrown in bulk update document method. request:{}. ",
                    request.toString());
        }
        return 0;
    }

    @Override
    public boolean indexExist(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest();
            request.indices(index);
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
