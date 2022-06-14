package xyz.fusheng.project.errordemo.generics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @FileName: GenericsErrorTest
 * @Author: code-fusheng
 * @Date: 2022/5/30 23:54
 * @Version: 1.0
 * @Description: 范型错误测试
 * 1. getMethods 和 getDeclaredMethods 是有区别的，前者可以查询到父类方法，后者只能查询到当前类。
 * 2. 反射进行方法调用要注意过滤桥接方法。
 */

public class GenericsErrorTest {

    private static final Logger logger = LoggerFactory.getLogger(GenericsErrorTest.class);

    private static void testGenericsUseError1() {
        Child1 child1 = new Child1();
        Arrays.stream(child1.getClass().getMethods())
                .filter(method -> method.getName().equals("setValue"))
                .forEach(method -> {
                    try {
                        method.invoke(child1, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        logger.info("[] => child1:{}", child1.toString());
    }

    private static void testGenericsUseError2() {
        Child1 child2 = new Child1();
        Arrays.stream(child2.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue"))
                .forEach(method -> {
                    try {
                        method.invoke(child2, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        logger.info("[] => child2:{}", child2.toString());
    }

    private static void testGenericsUseError3() {
        Child2 child3 = new Child2();
        Arrays.stream(child3.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue"))
                .forEach(method -> {
                    try {
                        method.invoke(child3, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        logger.info("[] => child3:{}", child3.toString());
    }

    private static void testGenericsUseRight1() {
        Child2 child4 = new Child2();
        Arrays.stream(child4.getClass().getDeclaredMethods())
                .filter(method -> method.getName().equals("setValue") && !method.isBridge())
                .forEach(method -> {
                    try {
                        method.invoke(child4, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        logger.info("[] => child4:{}", child4.toString());
    }

    public static void main(String[] args) {
        testGenericsUseError1();
        logger.info("------------------");
        testGenericsUseError2();
        logger.info("------------------");
        testGenericsUseError3();
        logger.info("------------------");
        testGenericsUseRight1();
    }

}

class Parent<T> {
    private static final Logger logger = LoggerFactory.getLogger(Parent.class);
    AtomicInteger updateCount = new AtomicInteger();
    private T value;

    @Override
    public String toString() {
        return String.format("value: %s updateCount: %d", value, updateCount.get());
    }

    public void setValue(T value) {
        logger.info("[Parent.setValue - called] => value:{}", value);
        this.value = value;
        updateCount.incrementAndGet();
    }
}

class Child1 extends Parent {
    private static final Logger logger = LoggerFactory.getLogger(Parent.class);

    public void setValue(String value) {
        logger.info("[Child1.setValue - called] => value:{}", value);
        super.setValue(value);
    }
}

class Child2 extends Parent<String> {
    private static final Logger logger = LoggerFactory.getLogger(Parent.class);

    @Override
    public void setValue(String value) {
        logger.info("[Child2.setValue - called] => value:{}", value);
        super.setValue(value);
    }
}
