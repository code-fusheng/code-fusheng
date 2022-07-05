package xyz.fusheng.project.tools.mongodb.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import xyz.fusheng.project.tools.mongodb.document.TestMongoModel;

/**
 * @FileName: TestMongoModelRepository
 * @Author: code-fusheng
 * @Date: 2022/7/5 14:13
 * @Version: 1.0
 * @Description:
 */

@Component
public interface TestMongoModelRepository extends MongoRepository<TestMongoModel, ObjectId> {

}
