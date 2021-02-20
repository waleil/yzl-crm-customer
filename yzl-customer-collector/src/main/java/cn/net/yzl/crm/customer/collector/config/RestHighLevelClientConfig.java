package cn.net.yzl.crm.customer.collector.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: RestHighLevelClientConfig
 * @description todo
 * @date: 2021/2/19 4:55 下午
 */
@Component
public class RestHighLevelClientConfig {
    @Autowired
    private ESConfig esConfig;

    private RestClientBuilder builder;

    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();


    @Bean("restHighLevelClient")
    public RestHighLevelClient client() {
        List<HttpHost> hostList = initHost();
        builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        setConnectTimeOutConfig();
        setMutiConnectConfig();
        return new RestHighLevelClient(builder);
    }

    private List<HttpHost> initHost() {
        String [] strings = esConfig.getUrl().split(",");
        List<HttpHost> hostList = new ArrayList<>();
        String schema =esConfig.getSchema();
        for (String str : strings) {
           String [] strs = str.split(":");
            HttpHost httpHost = new HttpHost(strs[0], Integer.parseInt(strs[1]), schema);
            hostList.add(httpHost);
        }
        return hostList;
    }

    /**
     * 异步httpclient的连接延时配置
     */
    private void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(esConfig.getConnectTimeOut());
            requestConfigBuilder.setSocketTimeout(esConfig.getSocketTimeOut());
            requestConfigBuilder.setConnectionRequestTimeout(esConfig.getConnectionRequestTimeOut());
            return requestConfigBuilder;
        });
    }

    /**
     * 异步httpclient的连接数配置
     */
    private void setMutiConnectConfig() {
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(esConfig.getUserName(), esConfig.getPassword()));
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setMaxConnTotal(esConfig.getMaxConnectNum());
            httpClientBuilder.setMaxConnPerRoute(esConfig.getMaxConnectPerRoute());
            return httpClientBuilder;
        });
    }
}
