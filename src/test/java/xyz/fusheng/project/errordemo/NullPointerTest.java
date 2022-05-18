package xyz.fusheng.project.errordemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @FileName: NullPointerTest
 * @Author: code-fusheng
 * @Date: 2022/5/6 06:58
 * @Version: 1.0
 * @Description:    空指针 问题测试
 * 空指针场景问题:
 * 1. 参数是 Integer 等包装类型，使用时因为自动拆箱出现了空指针异常；
 * 2. 字符串比较出现空指针异常；
 * 3. 诸如 ConcurrentHashMap 这样的容器不支持 Key 和 Value 为 null，强行 put pull 会出现空指针异常；
 * 4. 对象级联&嵌套调用出现空指针异常；
 * 5. 方法或者远程服务RPC返回集合不是空而是 NULL，没有进行判空处理就调用对应的方法；
 */

public class NullPointerTest {

    private static final Logger logger = LoggerFactory.getLogger(NullPointerTest.class);

    private static void testNullPointer() {
        // 1. 参数是 Integer 等包装类型，使用时因为自动拆箱出现了空指针异常；
        Integer i1 = null;
        try {
            int i2 = i1 + 1;
        } catch (Exception e) {
            logger.info("[Case1:Integer-异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }
        // 优化方式
        int i3 = Optional.ofNullable(i1).orElse(0) + 1;
        logger.info("[Case1:Integer-优化结果] => i3:{}", i3);

        // 2. 字符串比较出现空指针异常；
        String s1 = null;
        try {
            s1.equals("OK");
        } catch (Exception e) {
            logger.info("[Case2:String-异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }
        // 优化方式
        logger.info("[Case2:String-优化结果] => \"OK\".equals(s1) = {}", "OK".equals(s1));
        // 优化方式
        logger.info("[Case2:String-优化结果] => Objects.equals(s1, \"OK\") = {}", Objects.equals(s1, "OK"));

        // 3. 诸如 ConcurrentHashMap 这样的容器不支持 Key 和 Value 为 null，强行 put pull 会出现空指针异常；
        try {
            new ConcurrentHashMap<String, String>().put(null, null);
        } catch (Exception e) {
            logger.info("[Case3:ConcurrentHashMap-异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }

        // 4. 对象级联&嵌套调用出现空指针异常；
        @Data
        @AllArgsConstructor
        class B {
            private String value;
        }
        @Data
        @AllArgsConstructor
        class A {
            private B value;
        }

        A a1 = new A(new B("b"));
        logger.info("[a -> b.value ? {}]", a1.getValue().getValue());
        A a2 = new A(null);
        try {
            logger.info("[a -> b.value ? {}]", a2.getValue().getValue());
        } catch (Exception e) {
            logger.info("[Case4:a -> b.value-异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }
        // 优化方式
        String bValue = Optional.ofNullable(a2.getValue()).orElse(new B("new b")).getValue();
        logger.info("[Case4:Object级联引用-优化结果] => a -> b.value() = {}", bValue);

        // 5. 方法或者远程服务RPC返回集合不是空而是 NULL，没有进行判空处理就调用对应的方法；
        List list = null;
        try {
            list.size();
        } catch (Exception e) {
            logger.info("[Case5:list-异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }
        // 优化方式
        int size = Optional.ofNullable(list).orElse(Collections.EMPTY_LIST).size();
        logger.info("[Case5:list-优化结果] => size:{}", size);

    }

    public static void main(String[] args) {
        testNullPointer();
    }

}
