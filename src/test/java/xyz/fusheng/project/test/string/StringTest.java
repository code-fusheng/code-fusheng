package xyz.fusheng.project.test.string;

/**
 * @FileName: StringTest
 * @Author: code-fusheng
 * @Date: 2022/6/13 14:28
 * @Version: 1.0
 * @Description:
 */

public class StringTest {

    private static void testMatch() {
        String str1 = "RZ10000001";
        String str2 = "Temp-0-RZ10000001";
        String str3 = "1111";
        System.out.println(str1.indexOf("RZ"));
        System.out.println(str2.indexOf("RZ"));
        System.out.println(str3.indexOf("RZ"));

    }

    public static void main(String[] args) {

        testMatch();

    }

}
