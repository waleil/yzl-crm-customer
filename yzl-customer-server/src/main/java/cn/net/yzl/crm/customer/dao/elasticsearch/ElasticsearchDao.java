package cn.net.yzl.crm.customer.dao.elasticsearch;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.CountRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lichanghong
 * @version 1.0
 * @title: ElasticsearchDao
 * @date: 2021/2/22 9:07 下午
 */
public interface ElasticsearchDao {
    /*---------- index / indexAsync / indexBulk / indexAsyncBulk --------*/

    String save(String indexName, String id, String data);


    List<String> saveBulk(String indexName, Map<String, String> datas);

    void saveAsync(String indexName, String id, String data,
                    Function<String, Void> onSuccess,
                    Function<String, Void> onFail);

    void saveAsyncBulk(String indexName,  Map<String, String> datas,
                        Function<List<String>, Void> onSuccess,
                        Function<String, Void> onFail);


    /*---------- update / updateAsync / updateBulk / updateAsyncBulk --------*/

    String update(String indexName,  String id, String data);


    List<String> updateBulk(String indexName, Map<String, String> datas);

    void updateAsyncBulk(String indexName, Map<String, String> datas,
                         Function<List<String>, Void> onSuccess,
                         Function<String, Void> onFail);

    /*---------- delete / deleteAsync / deleteBulk / deleteAsyncBulk --------*/

    boolean delete(String indexName, String docId);

    boolean deleteBulk(String indexName, List<String> docIds);


    void deleteAsync(String indexName, String docId,
                     Function<String, Void> onSuccess,
                     Function<String, Void> onFail);

    void deleteAsyncBulk(String name,  List<String> docIds,
                         Function<List<String>, Void> onSuccess,
                         Function<String , Void> onFail);

    SearchResponse search(SearchRequest searchRequest, RequestOptions options) throws IOException;

    String searchById(String indexName, String id);
    long count(CountRequest request,RequestOptions options);

    boolean indexExist(String index);
}
