package xyz.fusheng.project.test.collection;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @FileName: CollectionTest
 * @Author: code-fusheng
 * @Date: 2022/6/8 15:05
 * @Version: 1.0
 * @Description:
 */

public class CollectionTest {

    private void testJoining() {
        List<String> licenseList = new ArrayList<>();
        licenseList.add("a");
        licenseList.add("b");
        String collect = licenseList.stream().distinct().collect(Collectors.joining(","));
        System.out.println(collect);
    }

    private static void test() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<Date, String> financeRoundList = new HashMap<>();
        financeRoundList.put(format.parse("2022-01-01"), "B");
        financeRoundList.put(format.parse("2021-01-01"), "A");
        financeRoundList.put(new Date(), "C");

        Object[] dateKeys = financeRoundList.keySet().toArray();
        Arrays.sort(dateKeys);
        String round = financeRoundList.get(dateKeys[dateKeys.length - 1]);
        System.out.println(round);
    }

    public static void main(String[] args) throws ParseException {
        test();
    }

}
