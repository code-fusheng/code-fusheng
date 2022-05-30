package xyz.fusheng.project.errordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @FileName: ReflectErrorTest
 * @Author: code-fusheng
 * @Date: 2022/5/30 22:44
 * @Version: 1.0
 * @Description: 反射错误测试
 * 1.
 */

public class ReflectErrorTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectErrorTest.class);

    // 常规使用
    private void testNormalUse() {
        ReflectionIssueApplication application = new ReflectionIssueApplication();
        // [ReflectionIssueApplication#age(int age)] => age:36
        application.age(36);
        // [ReflectionIssueApplication#age(Integer age)] => age:36
        application.age(Integer.valueOf("36"));
    }

    /**
     * 错误使用反射 Reflect
     * 反射调用方法不是以传参决定重载
     */
    private void testReflectUseError() throws Exception {
        Class<ReflectionIssueApplication> application = ReflectionIssueApplication.class;
        // [ReflectionIssueApplication#age(int age)] => age:36
        // 即便参数是 Integer，走的方法还是 age(int age)
        // getDeclaredMethod("age", Integer.TYPE) 中的 Integer.TYPE 代表参数是 int
        application.getDeclaredMethod("age", Integer.TYPE).invoke(new ReflectionIssueApplication(), Integer.valueOf("36"));

        // getDeclaredMethod("age", Integer.class) 中的 Integer.class 代表参数是 Integer
        application.getDeclaredMethod("age", Integer.class).invoke(new ReflectionIssueApplication(), Integer.valueOf("36"));
        application.getDeclaredMethod("age", Integer.class).invoke(new ReflectionIssueApplication(), 36);
    }

    public static void main(String[] args) throws Exception {
        ReflectErrorTest test = new ReflectErrorTest();
        test.testNormalUse();
        test.testReflectUseError();
    }

}
