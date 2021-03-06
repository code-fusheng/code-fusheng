package xyz.fusheng.project.tooldemo.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import xyz.fusheng.project.tools.elasticsearch.document.CompanyDocument;
import xyz.fusheng.project.tools.elasticsearch.document.ModelDocument;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @FileName: ElasticsearchTest
 * @Author: code-fusheng
 * @Date: 2022/6/20 14:07
 * @Version: 1.0
 * @Description: ES  ?????????
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    @Qualifier(value = "restHighLevelClient4GsbZq")
    private RestHighLevelClient restHighLevelClient4GsbZq;

    private static final String CODE_FUSHENG_INDEX_TEST = "code_fusheng_index_test";

    private static final String MODEL_INDEX = "model_index_test";

    // ????????? ????????? ??????
    private static final String COMPANY_INDEX = "gsb_zq_company_index_test";

    @Test
    public void testEsPing() throws IOException {
        boolean pingState = restHighLevelClient.ping(RequestOptions.DEFAULT);
        logger.info("[??????ES????????????] => ping#state:{}", pingState);
    }

    /**
     * ????????????
     */
    @Test
    public void testEsIndexCreate() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(CODE_FUSHENG_INDEX_TEST);
        CreateIndexResponse createIndexResponse1 = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        logger.info("[??????ES????????????] => createIndexResponse:{}", createIndexResponse1);

        CreateIndexResponse createIndexResponse2 = restHighLevelClient4GsbZq.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        logger.info("[??????ES????????????] => createIndexResponse:{}", createIndexResponse2);
    }


    /**
     * ????????????
     */
    @Test
    public void testEsIndexExist() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(CODE_FUSHENG_INDEX_TEST);
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        logger.info("[??????ES????????????] => exists:{}", exists);
    }

    /**
     * ????????????
     */
    @Test
    public void testEsIndexDelete() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(CODE_FUSHENG_INDEX_TEST);
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        logger.info("[??????ES????????????] => acknowledgedResponse#isAcknowledged:{}", acknowledgedResponse.isAcknowledged());
    }

    /**
     * ????????????
     *
     * @throws IOException
     */
    @Test
    public void testEsSearchScroll() throws IOException {
        StopWatch stopWatch = new StopWatch("????????????");
        stopWatch.start();
        FileWriter fileWriter = new FileWriter(Paths.get("modelCsv.csv").toString());
        CSVPrinter csvPrinter = CSVFormat.EXCEL.print(fileWriter);
        Object[] columns = {"??????ID", "????????????", "?????????", "????????????", "????????????", "??????"};
        csvPrinter.printRecord(columns);
        SearchRequest searchRequest = new SearchRequest(MODEL_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(5000);
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.sort("sort", SortOrder.ASC);
        searchRequest.scroll(TimeValue.timeValueMinutes(1L));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String scrollId = searchResponse.getScrollId();
        try {
            while (searchHits != null && searchHits.length > 0) {
                logger.info("?????????");
                SearchHits hits = searchResponse.getHits();
                List<ModelDocument> resultList = Arrays.stream(hits.getHits()).map(item -> {
                    String jsonStr = item.getSourceAsString();
                    ModelDocument modelDocument = null;
                    try {
                        modelDocument = objectMapper.readValue(jsonStr, ModelDocument.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return modelDocument;
                }).collect(Collectors.toList());
                logger.info("{}", resultList.size());
                for (ModelDocument modelDocument : resultList) {
                    Map<String, Object> map = JSON.parseObject(JSON.toJSONString(modelDocument), new TypeReference<Map<String, Object>>() {
                    });
                    csvPrinter.printRecord(map.values());
                }
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
                searchScrollRequest.scroll(TimeValue.timeValueMinutes(1L));
                searchResponse = restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
                searchHits = searchResponse.getHits().getHits();
            }
            csvPrinter.flush();
            csvPrinter.close();
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
            logger.info("??????");
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***********************************????????????*******************************************/

    @Test
    public void testEsDocumentBulkAdd() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int count = 1;
        while (count <= 20) {
            for (int i = 1; i <= 10000; i++) {
                ModelDocument document = new ModelDocument(atomicInteger.incrementAndGet() + "");
                bulkRequest.add(new IndexRequest(MODEL_INDEX)
                        .id(document.getId())
                        .source(objectMapper.writeValueAsString(document), XContentType.JSON));
            }
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            logger.info("[??????ES??????????????????] => ??????{}?????? bulkResponse#hasFailures:{}", count, bulkResponse.hasFailures());
            count++;
        }
    }

    /***********************************?????????????????? V6.8.14*******************************************/

    @Test
    public void testEsDocumentSearch(String companyName, String tag) throws IOException {
        SearchRequest searchRequest = new SearchRequest().indices(COMPANY_INDEX).types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("companyName", "????????????????????????????????????"));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient4GsbZq.search(searchRequest);
        if (searchResponse.getHits() != null) {
            for (SearchHit doc : searchResponse.getHits()) {
                logger.info("[doc:{}]", doc.getSourceAsString());
                CompanyDocument companyDocument = objectMapper.readValue(doc.getSourceAsString(), CompanyDocument.class);
                List<String> companyTagList = CollectionUtils.isNotEmpty(companyDocument.getCompanyTagList()) ? companyDocument.getCompanyTagList() : new ArrayList<>();
                companyTagList.add("??????");
                List<String> finalTagList = companyTagList.stream().distinct().collect(Collectors.toList());
                companyDocument.setCompanyTagList(finalTagList);
                companyDocument.setCompanyTags(StringUtils.join(finalTagList, ","));
                UpdateRequest updateRequest = new UpdateRequest(COMPANY_INDEX, "_doc", companyDocument.getCompanyId());
                updateRequest.doc(objectMapper.writeValueAsString(companyDocument), XContentType.JSON);
                UpdateResponse updateResponse = restHighLevelClient4GsbZq.update(updateRequest);
                logger.info("[??????Excel??????-??????ES????????????????????????] => update.status:{}, finalTagList:{}", updateResponse.status(), finalTagList);
            }
        }
    }

}
