package xyz.fusheng.project.tools.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @FileName: ElasticsearchClientConfig
 * @Author: code-fusheng
 * @Date: 2020/5/14 16:05
 * @version: 1.0
 * Description: Elasticsearch配置类
 */

@Profile("!dev")    // 非dev环境才声明该配置
@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @PostConstruct
    public void testPost() {
        logger.info("[Es Bean -- 开始注入]");
    }

    @PreDestroy
    public void testDestroy() {
        logger.info("[Es Bean -- 开始销毁]");
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("42.192.222.62", 9200, "http")
                )
        );
        return client;
    }
}
