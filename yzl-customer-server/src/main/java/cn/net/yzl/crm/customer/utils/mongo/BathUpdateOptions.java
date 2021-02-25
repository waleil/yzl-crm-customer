package cn.net.yzl.crm.customer.utils.mongo;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author lichanghong
 * @version 1.0
 * @title: BathUpdateOptions
 * @description 批量更新
 * @date: 2021/2/20 4:40 下午
 */
public class BathUpdateOptions {
    private Query query;
    private Update update;
    private Boolean upsert = true;
    private Boolean multi = false;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public Boolean getUpsert() {
        return upsert;
    }

    public void setUpsert(Boolean upsert) {
        this.upsert = upsert;
    }

    public Boolean getMulti() {
        return multi;
    }

    public void setMulti(Boolean multi) {
        this.multi = multi;
    }
}
