package cn.net.yzl.crm.customer.collector.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * @author lichanghong
 * @version 1.0
 * @title: ESConfig
 * @description todo
 * @date: 2021/2/19 4:42 下午
 */
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ESConfig {
    private String url;
    private String schema = "http";
    /**
     * 连接超时时间
     */
    private  int connectTimeOut = 1000;
    /**
     * 连接超时时间
     */
    private  int socketTimeOut = 30000;
    /**
     * 获取连接的超时时间
     */
    private  int connectionRequestTimeOut = 500;
    /**
     * 最大连接数
     */
    private  int maxConnectNum = 100;
    /**
     * 最大路由连接数
     */
    private  int maxConnectPerRoute = 100;

    private String userName;

    private String password;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        if(StringUtils.hasText(schema)){
            this.schema = schema;
        }
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        if(connectTimeOut>0){
            this.connectTimeOut = connectTimeOut;
        }

    }

    public int getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        if(socketTimeOut>0){
            this.socketTimeOut = socketTimeOut;
        }
    }

    public int getConnectionRequestTimeOut() {
        return connectionRequestTimeOut;
    }

    public void setConnectionRequestTimeOut(int connectionRequestTimeOut) {
        if(connectionRequestTimeOut>0){
            this.connectionRequestTimeOut = connectionRequestTimeOut;
        }
    }

    public int getMaxConnectNum() {
        return maxConnectNum;
    }

    public void setMaxConnectNum(int maxConnectNum) {
        if(maxConnectNum>0){
            this.maxConnectNum = maxConnectNum;
        }
    }

    public int getMaxConnectPerRoute() {
        return maxConnectPerRoute;
    }

    public void setMaxConnectPerRoute(int maxConnectPerRoute) {
        if(maxConnectPerRoute>0){
            this.maxConnectPerRoute = maxConnectPerRoute;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
