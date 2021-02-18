package cn.net.yzl.crm.customer.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedissonProperties {

    @Value("${redisson.password}")
    private String password;
    @Value("${redisson.nodes}")
    private String nodes;
    @Value("${redisson.type:2}")
    private int type;



    public String [] getAddresses(){
        if(this.nodes!=null){
            return this.nodes.split(",");
        }
        return new String[0];
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}