package xyz.fusheng.project.test.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Optional;

/**
 * @FileName: JsonTest
 * @Author: code-fusheng
 * @Date: 2022/6/13 14:50
 * @Version: 1.0
 * @Description:
 */

public class JsonTest {

    private static void testNullJson() {
        JSONObject memoJson1 = JSON.parseObject("", JSONObject.class);
        System.out.println(memoJson1);

        JSONObject memoJson2 = Optional.ofNullable(JSON.parseObject(null, JSONObject.class)).orElse(new JSONObject());
        System.out.println(memoJson2);
    }

    public static void main(String[] args) {
        testNullJson();
    }

}
