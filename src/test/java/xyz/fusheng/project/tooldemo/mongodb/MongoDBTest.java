package xyz.fusheng.project.tooldemo.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.fusheng.project.tools.mongodb.document.TestMongoModel;

import javax.annotation.Resource;
import java.util.List;

/**
 * @FileName: MongoDBTest
 * @Author: code-fusheng
 * @Date: 2022/7/4 18:14
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDBTest {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBTest.class);

    @Autowired(required = true)
    private MongoTemplate mongoTemplate;

    @Test
    public void testMongoDBPing() {
        List<TestMongoModel> all = mongoTemplate.findAll(TestMongoModel.class);
        System.out.println(all);
    }

}
