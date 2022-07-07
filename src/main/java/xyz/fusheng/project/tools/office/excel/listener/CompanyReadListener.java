package xyz.fusheng.project.tools.office.excel.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.fusheng.project.tools.elasticsearch.document.CompanyDocument;
import xyz.fusheng.project.tools.office.excel.model.CompanyReadExcel;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @FileName: CompanyReadListener
 * @Author: code-fusheng
 * @Date: 2022/7/3 12:41
 * @Version: 1.0
 * @Description:
 */

public class CompanyReadListener implements ReadListener<CompanyReadExcel> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyReadListener.class);

    public static final ThreadLocal<AtomicInteger> count = ThreadLocal.withInitial(() ->
            new AtomicInteger(0));

    public static final ThreadLocal<List<CompanyReadExcel>> noMatchCompanyList = ThreadLocal.withInitial(ArrayList::new);

    private String COMPANY_INDEX;

    private RestHighLevelClient restHighLevelClient4GsbZq;

    private ObjectMapper objectMapper;

    public CompanyReadListener(String COMPANY_INDEX, RestHighLevelClient restHighLevelClient4GsbZq, ObjectMapper objectMapper) {
        this.COMPANY_INDEX = COMPANY_INDEX;
        this.restHighLevelClient4GsbZq = restHighLevelClient4GsbZq;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public void invoke(CompanyReadExcel data, AnalysisContext context) {
        try {
            logger.info("[解析Excel数据-第「{}」条]  => CompanyExcel:{}", count.get().incrementAndGet(), JSON.toJSONString(data));
            SearchRequest searchRequest = new SearchRequest().indices(COMPANY_INDEX).types("_doc");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.termQuery("companyName", data.getCompanyName()));
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = restHighLevelClient4GsbZq.search(searchRequest);
            if (searchResponse.getHits() != null && searchResponse.getHits().getTotalHits() > 0) {
                for (SearchHit doc : searchResponse.getHits()) {
                    logger.info("[doc:{}]", doc.getSourceAsString());
                    CompanyDocument companyDocument = objectMapper.readValue(doc.getSourceAsString(), CompanyDocument.class);
                    List<String> companyTagList = CollectionUtils.isNotEmpty(companyDocument.getCompanyTagList()) ? companyDocument.getCompanyTagList() : new ArrayList<>();
                    companyTagList.add(data.getCompanyTag());
                    List<String> finalTagList = companyTagList.stream().distinct().collect(Collectors.toList());
                    companyDocument.setCompanyTagList(finalTagList);
                    companyDocument.setCompanyTags(StringUtils.join(finalTagList, ","));
                    UpdateRequest updateRequest = new UpdateRequest(COMPANY_INDEX, "_doc", companyDocument.getCompanyId());
                    updateRequest.doc(objectMapper.writeValueAsString(companyDocument), XContentType.JSON);
                    UpdateResponse updateResponse = restHighLevelClient4GsbZq.update(updateRequest);
                    logger.info("[解析Excel数据-更新ES企业标签数据结果] => update.status:{}, finalTagList:{}", updateResponse.status(), finalTagList);
                }
            } else {
                logger.info("[解析Excel数据-未能匹配到「{}」企业名称]", data.getCompanyName());
                noMatchCompanyList.get().add(data);
            }
        } catch (Exception e) {
            logger.error("[异常:{}]", e.getMessage(), e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        logger.info("[解析Excel数据-完成所有]");
        File file = new File(Paths.get("未匹配企业-" + new Date().getTime() + ".xlsx").toString());
        EasyExcel.write(file.getPath(), CompanyReadExcel.class).sheet("test").doWrite(noMatchCompanyList.get());
    }
}
