package xyz.fusheng.project.demo;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @FileName: CollectionTest
 * @Author: code-fusheng
 * @Date: 2022/5/5 17:30
 * @Version: 1.0
 * @Description: 集合问题测试
 */

public class CollectionTest {

    private static final Logger logger = LoggerFactory.getLogger(CollectionTest.class);

    /**
     * 1. 不能使用 Arrays.asList() 来转换基本类型数组
     * 2. Arrays.asList() 返回的 List 不支持增删操作 UnsupportedOperationException
     */
    private static void arraysConvertList() {
        int[] arr1 = {1, 2, 3};
        List list1 = Arrays.asList(arr1);
        /**
         * 问题：通过日志可以发现: 这个 List 包含的是一个 int 数组，整个 List 的元素个数是 1，元素类型是 整数数组
         * 原因：Arrays.asList() 方法 传入的是一个范型 T 类型可变参数，只能把 int 装箱为 Integer，不能把 int 数组装箱成 Integer 数组
         *      最终 int 数组整体作为了一个对象的范型类型 T，可以看到通过 Integer[] 包装类型声明的数组是没有问题的
         */
        logger.info("[Arrays.asList(arr)] => list1:{} size:{} class:{}", list1, list1.size(), list1.get(0).getClass());
        // 问题的 int[] 类型数组 arr1 可以通过 Java8 以上的 Arrays.stream 方法来转换 其中 boxed() 方法就做个装箱
        List<Integer> integerList1 = Arrays.stream(arr1).boxed().collect(Collectors.toList());
        logger.info("[Arrays.stream...integerList1] => integerList1:{} size:{} class:{}", integerList1, integerList1.size(), integerList1.get(0).getClass());


        Integer[] arr2 = {1, 2, 3};
        List<Integer> list2 = Arrays.asList(arr2);
        logger.info("[Arrays.asList(arr)] => list2:{} size:{} class:{}", list2, list2.size(), list2.get(0).getClass());

        logger.info("**************************************************");

        String[] arr3 = {"1", "2", "3"};
        /**
         * 问题所在
         * Arrays.asList() 方法返回的 ArrayList 并不是我们期望的 java.util.ArrayList
         * 而是 Arrays 的内部类 ArrayList，其内部类继承自 AbstractList 类，没有覆写父类的 add 方法，而父类的实现就是抛出 UnsupportedOperationException 异常
         */
        List<String> list3 = Arrays.asList(arr3);    // 字符串类型的数组不是基本类型，不存在转换问题
        logger.info("[list3:{}]", list3);
        arr3[1] = "4";  // 修改
        try {
            list3.add("5");
        } catch (Exception e) {
            logger.info("[list3.add() - 异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }
        try {
            list3.remove(0);
        } catch (Exception e) {
            logger.info("[list3.remove() - 异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage(), e);
        }
        logger.info("[list3:{}]", list3);

        arr3[0] = "0";
        /**
         * 问题 修改操作会相互影响
         * Arrays.asList() 返回的 List 数据直接引用了原始数组，会共享数据
         * 修复方式重新 new 一个 ArrayList 接收
         */
        logger.info("[list3:{}]", list3);
        list3.set(2, "2");
        logger.info("[arr3:{}]", Arrays.toString(arr3));
        // 修复
        List list = new ArrayList(Arrays.asList(arr3));
    }

    /**
     * 问题 OOM
     * 循环中有 1000 个具有 10万元素的 List 始终得不到回收，因为他们始终被 subList 方法返回的 List 强引用
     * 可以理解为 subList 返回的 list 就是原始数据的视图
     */
    private static void listSubListOOM() {
        List<List<Integer>> data = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            List<Integer> rawList = IntStream.rangeClosed(1, 100000).boxed().collect(Collectors.toList());
            data.add(rawList.subList(0, 1));
            //logger.info("{}", data);
        }
    }

    /**
     * 一定要让合适的数据结构做合适的事情
     * 误区一: 使用数据结构不考虑平衡 时间和空间
     */

    @Data
    @AllArgsConstructor
    static class Order {
        private int orderId;
    }


    private static Object testListSearch(int elementCount, int loopCount) {
        List<Order> list = IntStream.rangeClosed(1, elementCount).mapToObj(i -> new Order(i)).collect(Collectors.toList());
        IntStream.rangeClosed(1, loopCount).forEach(i -> {
            int search = ThreadLocalRandom.current().nextInt(elementCount);
            Order result = list.stream().filter(order -> order.getOrderId() == search).findFirst().orElse(null);
            Assert.assertTrue(result != null && result.getOrderId() == search);
        });
        return list;
    }

    private static Object testMapSearch(int elementCount, int loopCount) {
        Map<Integer, Order> map = IntStream.rangeClosed(1, elementCount).boxed().collect(Collectors.toMap(Function.identity(), i -> new Order(i)));
        IntStream.rangeClosed(1, loopCount).forEach(i -> {
            int search = ThreadLocalRandom.current().nextInt(elementCount);
            Order result = map.get(search);
            Assert.assertTrue(result != null && result.getOrderId() == search);
        });
        return map;
    }

    private static void testListAndMapSearch() {
        int elementCount = 1000000;
        int loopCount = 1000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("listSearch");
        Object list = testListSearch(elementCount, loopCount);
        stopWatch.stop();
        logger.info("[list] => size:{}", ObjectSizeCalculator.getObjectSize(list));
        stopWatch.start("mapSearch");
        Object map = testMapSearch(elementCount, loopCount);
        stopWatch.stop();
        logger.info("[map] => size:{}", ObjectSizeCalculator.getObjectSize(map));
        System.out.println(stopWatch.prettyPrint());
    }

    public static void main(String[] args) {
        //arraysConvertList();
        //listSubListOOM();
        testListAndMapSearch();
    }

}
