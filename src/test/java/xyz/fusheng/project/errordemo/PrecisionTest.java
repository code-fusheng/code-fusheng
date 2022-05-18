package xyz.fusheng.project.errordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @FileName: PrecisionTest
 * @Author: code-fusheng
 * @Date: 2022/5/5 13:41
 * @Version: 1.0
 * @Description: 计算精度问题
 */

public class PrecisionTest {

    private static final Logger logger = LoggerFactory.getLogger(PrecisionTest.class);

    /**
     * 危险的 Double 类型
     * Java 采用了 IEEE 754 标准 实现浮点数的表达和运算 0.1 的 二进制表示为 0.0 0011 0011 ...，
     * 再转换成 10 进制 0.1000000000000000055511151231257827021181583404541015625 这就是浮点数精度问题的根源
     */
    public static void testDouble() {
        logger.info("[0.1 + 0.2 = {}]", 0.1 + 0.2);         // 0.30000000000000004
        logger.info("[1.0 - 0.8 = {}]", 1.0 - 0.8);         // 0.19999999999999996
        logger.info("[4.015 * 100 = {}]", 4.015 * 100);     // 401.49999999999994
        logger.info("[123.3 / 100 = {}]", 123.3 / 100);     // 1.2329999999999999

        double amount1 = 2.15;
        double amount2 = 1.10;
        logger.info("[{} - {} = {}]", amount1, amount2, amount1 - amount2);
    }

    /**
     * 浮点类型的舍入与格式化问题
     * {@link java.util.Formatter} => print => `value = value.setScale(prec, RoundingMode.HALF_UP);`
     */
    public static void testFormat() {
        double num1 = 3.35;
        float num2 = 3.35f;
        // 错误用法
        logger.info("[String.format:{}]", String.format("%.1f", num1));        // 3.4
        logger.info("[String.format:{}]", String.format("%.1f", num2));        // 3.3
        // 错误用法
        DecimalFormat format = new DecimalFormat("#.##");
        format.setRoundingMode(RoundingMode.DOWN);
        logger.info("[DecimalFormat:{}]", format.format(num1));
        format.setRoundingMode(RoundingMode.DOWN);
        logger.info("[DecimalFormat:{}]", format.format(num2));
        // 正确用法
        BigDecimal num3 = new BigDecimal("3.35");
        logger.info("[DOWN:{}]", num3.setScale(1, RoundingMode.DOWN));
        logger.info("[HALF_UP:{}]", num3.setScale(1, RoundingMode.HALF_UP));
    }

    /**
     * BigDecimal 精度类型
     * 使用 BigDecimal 表示和计算浮点数，务必使用字符串的构造方法来初始化 BigDecimal
     * BigDecimal 有 scale 小数位数 和 precision 精度的概念
     */
    public static void testBigDecimal() {
        BigDecimal bigDecimal1 = new BigDecimal(0.1).add(new BigDecimal(0.2));
        logger.info("[bigDecimal1:{}]", bigDecimal1);
        BigDecimal bigDecimal2 = new BigDecimal(1.0).subtract(new BigDecimal(0.8));
        logger.info("[bigDecimal2:{}]", bigDecimal2);
        BigDecimal bigDecimal3 = new BigDecimal(4.015).multiply(new BigDecimal(100));
        logger.info("[bigDecimal3:{}]", bigDecimal3);
        BigDecimal bigDecimal4 = new BigDecimal(123.3).divide(new BigDecimal(100));
        logger.info("[bigDecimal4:{}]", bigDecimal4);

        BigDecimal bigDecimal5 = new BigDecimal("0.1").add(new BigDecimal("0.2"));
        logger.info("[bigDecimal5:{}]", bigDecimal5);
        BigDecimal bigDecimal6 = new BigDecimal("1.0").subtract(new BigDecimal("0.8"));
        logger.info("[bigDecimal6:{}]", bigDecimal6);
        BigDecimal bigDecimal7 = new BigDecimal("4.015").multiply(new BigDecimal("100"));
        logger.info("[bigDecimal7:{}]", bigDecimal7);
        BigDecimal bigDecimal8 = new BigDecimal("123.3").divide(new BigDecimal("100"));
        logger.info("[bigDecimal8:{}]", bigDecimal8);
    }

    /**
     * 测试长度溢出
     */
    private static void testOverflow() {
        long l1 = Long.MAX_VALUE;
        // 默默的溢出了，而且没有发生异常
        logger.info("[l1 + 1 = {}]", l1 + 1);
        logger.info("[l1 + 1 == Long.MIN_VALUE ? {}]", l1 + 1 == Long.MIN_VALUE);

        // 使用 Math 类的 addExact、subtractExact 等 xxExact 方法
        try {
            long l2 = Long.MAX_VALUE;
            logger.info("[Math.addExact(l2, 1) ? {}]", Math.addExact(l2, 1));
        } catch (Exception e) {
            logger.error("[Math.addExact(l2, 1) - 异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage());
        }

        // 使用 BigInteger 类
        BigInteger i = new BigInteger(String.valueOf(Long.MAX_VALUE));
        logger.info("[i.add(BigInteger.ONE).toString() = {}]", i.add(BigInteger.ONE).toString());
        try {
            // 可以加但是不能转
            long l3 = i.add(BigInteger.ONE).longValueExact();
        } catch (Exception e) {
            logger.error("[i.add(BigInteger.ONE).longValueExact() - 异常信息] => type:{}, e:{}", e.getClass().getName(), e.getMessage());
        }
    }

    public static void main(String[] args) {
        //testDouble();
        //testBigDecimal();
        //testFormat();
        testOverflow();
    }

}
