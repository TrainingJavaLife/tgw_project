package com.tgw360.uip.common;

import org.apache.http.HttpEntity;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.CheckedFunction;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.List;

/**
 * 扩展了RestHighLevelClient类，将parseEntity方法的权限修改为public
 * Created by 邹祥 on 2017/11/28 14:41
 */
public class ExtendRestHighLevelClient extends RestHighLevelClient {

    public ExtendRestHighLevelClient(RestClient restClient) {
        super(restClient);
    }

    protected ExtendRestHighLevelClient(RestClient restClient, List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClient, namedXContentEntries);
    }

    @Override
    public <Resp> Resp parseEntity(HttpEntity entity, CheckedFunction<XContentParser, Resp, IOException> entityParser) throws IOException {
        return super.parseEntity(entity, entityParser);
    }
}
