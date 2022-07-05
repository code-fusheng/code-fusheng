package xyz.fusheng.project.tools.mongodb.document;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

/**
 * @FileName: TestMongoModel
 * @Author: code-fusheng
 * @Date: 2022/7/5 14:10
 * @Version: 1.0
 * @Description:
 */

@Data
@Accessors(chain = true)
@Document(collection = "test_mongo_model")
public class TestMongoModel implements Serializable {

    @MongoId
    private String id;

    private String modelName;

    private String modelValue;

}
