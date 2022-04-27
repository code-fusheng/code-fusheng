package xyz.fusheng.project;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @FileName: ElasticsearchTest
 * @Author: code-fusheng
 * @Date: 2022/4/7 09:26
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
public class ElasticsearchTest {

    private String TM_INDEX = "springboot_project_cli_test_index";

    @Test
    void contextLoads() {
    }

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    void testEs() throws IOException {
        boolean ping = restHighLevelClient.ping(RequestOptions.DEFAULT);
        System.out.println(ping);
    }

}
