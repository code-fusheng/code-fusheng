package xyz.fusheng.project.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @FileName: JudgmentTest
 * @Author: code-fusheng
 * @Date: 2022/5/3 21:19
 * @Version: 1.0
 * @Description: 判等问题
 * 1. equals() 方法
 * 2. '==' 操作符
 */

public class JudgmentTest {

    private static final Logger logger = LoggerFactory.getLogger(JudgmentTest.class);

    private static void test1() {

        /**
         * 符合预期
         * PS: 编译器会将 Integer a = 127 转换为 Integer.valueOf(127)，转换的内部做了缓存 {@link Integer.IntegerCache} ，使得两个 Integer 指向同一个对象
         */
        Integer a = 127;    // Integer.valueOf(127)
        Integer b = 127;    // Integer.valueOf(127)
        logger.info("[a:{}, b:{}] => a == b ? {}", a, b, a == b);   // true

        // 不符合预期
        Integer c = 128;    // Integer.valueOf(128)
        Integer d = 128;    // Integer.valueOf(128)
        logger.info("[c:{}, d:{}] => c == d ? {}", c, d, c == d);   // false

        // 不符合预期
        Integer e = 127;    // Integer.valueOf(127)
        Integer f = new Integer(127);   // new instance
        logger.info("[e:{}, f:{}] => e == f ? {}", e, f, e == f);   // false

        // 不符合预期
        Integer g = new Integer(127);   // new instance
        Integer h = new Integer(127);   // new instance
        logger.info("[g:{}, h:{}] => g == h ? {}", g, h, g == h);   // false

        /**
         * 符合预期
         * Integer 会先进行拆箱操作，然后比较的是「基本类型」不是对象，固然相等
         */
        Integer i = 128;    // unbox
        int j = 128;
        logger.info("[i:{}, j:{}] => i == j ? {}", i, j, i == j);   // true
    }

    /**
     * 枚举类 与 请求入参 中的 Integer 包装类型比较务必使用 equals() 方法
     */
    private static void test2() {
        Test2Query query1 = new Test2Query(1, "query1");
        logger.info("[Test2Query.status:{}, Test2Enum.status:{}] => Is Judgment:{}", query1.getStatus(), Test2Enum.TRUE.getStatus(), query1.getStatus() == Test2Enum.TRUE.getStatus());
        Test2Query query2 = new Test2Query(128, "query2");
        logger.info("[Test2Query.status:{}, Test2Enum.status:{}] => Is Judgment:{}", query2.getStatus(), Test2Enum.ERROR.getStatus(), query2.getStatus() == Test2Enum.ERROR.getStatus());
        // 使用 equals() 方法是正确的
        Test2Query query3 = new Test2Query(128, "query2");
        logger.info("[Test2Query.status:{}, Test2Enum.status:{}] => Is Judgment:{}", query2.getStatus(), Test2Enum.ERROR.getStatus(), Test2Enum.ERROR.getStatus().equals(query3.getStatus()));
    }

    /**
     * 自定义类的 equals() 比较需要重写 equals() 方法，默认使用 Object 基类的引用比较方式
     * PS: lombok 的 @Data 注解能够解决 重写 equals() 和 hashCode 以及 toString()
     */
    private static void test3() {
        Test3Point p1 = new Test3Point(1, 2, "a");
        Test3Point p2 = new Test3Point(1, 2, "b");
        Test3Point p3 = new Test3Point(1, 2, "a");
        logger.info("[p1.equals(p2) ? {}]", p1.equals(p2));
        // 问题
        logger.info("[p1.equals(p3) ? {}]", p1.equals(p3));

        HashSet<Test3Point> points = new HashSet<>();
        points.add(p1);
        // 问题 涉及到重写 hashCode
        logger.info("[points.contains(p1) ? {}, points.contains(p3) ? {}]", points.contains(p1), points.contains(p3));

        // TODO 待具体分析 问题 涉及 CompareTo 的重写
        Test3Point p4 = new Test3Point(1, 2, "testCompare1");
        Test3Point p5 = new Test3Point(1, 2, "testCompare2");
        Test3Point p6 = new Test3Point(1, 2, "testCompare3");
        List<Test3Point> list = new ArrayList<>();
        list.add(p4); list.add(p5);
        logger.info("[list:{}]", list);
        int index1 = list.indexOf(p6);
        int index2 = Collections.binarySearch(list, p6);
        logger.info("[index1:{}, index2:{}]", index1, index2);
    }

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

}

@Getter
enum Test2Enum {
    TRUE(1, "正确"),
    ERROR(128, "错误");

    private Integer status;
    private String desc;

    Test2Enum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

@Data
@AllArgsConstructor
class Test2Query {
    private Integer status;
    private String name;
}

@Data
//@EqualsAndHashCode
class Test3Point implements Comparable<Test3Point> {
    private Integer x;
    private Integer y;
    private final String desc;

    public Test3Point(Integer x, Integer y, String desc) {
        this.x = x;
        this.y = y;
        this.desc = desc;
    }

    @Override
    public int compareTo(Test3Point other) {
        return Comparator.comparing(Test3Point::getDesc)
                .thenComparingInt(Test3Point::getY)
                .thenComparingInt(Test3Point::getX)
                .compare(this, other);
    }
}
