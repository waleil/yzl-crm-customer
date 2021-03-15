package cn.net.yzl.crm.customer.config.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean(value = ESConfig.class)
@Component
public class RestHighLevelClientConfig {

    private RestClientBuilder builder;

    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    @Bean("restHighLevelClient")
    public RestHighLevelClient restHighLevelClient(ESConfig esConfig) {
        List<HttpHost> hostList = initHost(esConfig);
        builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        setConnectTimeOutConfig(esConfig);
        setMutiConnectConfig(esConfig);
        return new RestHighLevelClient(builder);
    }

    private List<HttpHost> initHost(ESConfig esConfig) {
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
    private void setConnectTimeOutConfig(ESConfig esConfig) {
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
    private void setMutiConnectConfig(ESConfig esConfig) {
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
