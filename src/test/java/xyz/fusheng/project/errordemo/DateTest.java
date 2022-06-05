package xyz.fusheng.project.errordemo;

import org.assertj.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @FileName: DateTest
 * @Author: code-fusheng
 * @Date: 2022/5/25 00:34
 * @Version: 1.0
 * @Description: 日期时间测试
 * <p>
 * Java8 之前:
 * Date (时间)
 * Calender (日历)
 * SimpleDateFormat (格式化解析)
 * Java8 之后:
 * LocalDateTime (日期时间)
 * ZoneId
 * ZoneOffset
 * ZoneDateTime
 * DateTimeFormatter
 * <p>
 * 1. 时区问题: 全球 24 个时区，同一个时刻不同时区的时间是不一样的。
 * 2. 时间国际化问题
 * 方式一: 以 UTC 保存，保存的时间没有时区属性(是不涉及时区时间差的世界统一时间)。我们通常说的时间戳，或 Java 中的 Date 类就是用的这种方式。
 * 方式二: 以 字面量 保存，比如 年 / 月 / 日 时:分:秒，一定同时保存时区信息。Calender 是有时区概念的。
 * 总结: 使用 Java8 的日期时间类 ———— 使用 {@link ZonedDateTime} 保存时间，然后使用设置了 {@link ZoneId} 的 {@link DateTimeFormatter} 配合 ZonedDateTime
 * 进行时间格式化得到本地时间表示。
 * 3. 没有特殊需求，针对年份的日期格式化，应该一律使用 “y” 而非 “Y”。
 */

public class DateTest {

    private static final Logger logger = LoggerFactory.getLogger(DateTest.class);

    /**
     * 如果正确的初始化时间?
     */
    private static void initDate() {
        // 原生 Date 方式
        Date date_error = new Date(2022, 5, 25, 24, 40, 1);
        // Sun Jun 25 00:40:01 CST 3922
        logger.info("[初始化日期时间-错误方式] => date:{}", date_error);
        // Thu May 26 00:40:01 CST 2022 PS: 年份应该是和 1900 的差值，月份应该是从 0 ～ 11
        Date date_right = new Date(2022 - 1900, 4, 25, 24, 40, 1);
        logger.info("[初始化日期时间-相对正确方式] => date:{}", date_right);

        // Calender 日历方式
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2022, 4, 25, 24, 50, 1);
        logger.info("[初始化日期时间-日历方式(当前时区)] => calendar1.getTime() : {}", calendar1.getTime());

        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        calendar2.set(2022, Calendar.MAY, 25, 24, 50, 1);
        logger.info("[初始化日期时间-日历方式(纽约时区)] => calendar2.getTime() : {}", calendar2.getTime());

    }

    /**
     * {@link Date} 类
     * 1. Date 并没有时区问题，Date 中存储的是 UTC 时间，UTC 是以原子钟为基础的统一时间，不以太阳为参照记时，并不区分时区。
     */
    private static void testJavaDate() {
        logger.info("[原子钟时间] => date:{}", new Date(0));
        logger.info("[时区] => TimeZone:{}, TimeOffset:{}", TimeZone.getDefault().getID(), TimeZone.getDefault().getRawOffset() / 3600000);
    }

    /**
     * 这正是 UTC 的意义，并不是时间错乱。对于同一个本地时间的表示，不同时区的人解析得到的 UTC 时间一定是不同的，反过来不同的本地时间可能对应同一个 UTC。
     */
    private static void testDiffTimeZone() throws ParseException {
        // 字面量时间
        String strDate = "2022-05-25 22:38:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 默认时区解析时间表示
        Date date1 = format.parse(strDate);
        logger.info("[默认时区解析时间] => date1:{}", date1);
        // 纽约时区解析时间表示
        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        Date date2 = format.parse(strDate);
        logger.info("[纽约时区解析时间] => date2:{}", date2);
    }

    private static void testDefaultTimeZone() throws ParseException {
        // 字面量时间
        String strDate = "2022-05-25 22:38:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(strDate);
        // 默认时区解析时间表示
        String date1 = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date);
        logger.info("[默认时区解析时间] => date1:{}", date1);
        // 纽约时区解析时间表示
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        String date2 = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss Z]").format(date);
        logger.info("[纽约时区解析时间] => date2:{}", date2);
    }

    private static void testJavaNewDate() {
        /**
         * 初始化上海、纽约、东京三个时区
         * 1. 使用 {@link ZoneId#of(String)} 初始化一个标准的时区;
         * 2. 使用 {@link ZoneOffset#ofHours(int)} 初始化一个具有指定时间差的自定义时区。
         */
        ZoneId timeZoneSH = ZoneId.of("Asia/Shanghai");
        ZoneId timeZoneNY = ZoneId.of("America/New_York");
        ZoneOffset timeZoneJST = ZoneOffset.ofHours(0);

        /**
         * 时间日期表示
         * 1. LocalDateTime 不带有时区属性，所以命名为本地时区的日期时间。只能认为一个时间表示。
         * 2. ZonedDateTime = LocalDateTime + ZoneId 具有时区属性。是一个有效的时间。
         */
        // 使用东京时间解析
        String strDate = "2022-05-25 22:59:00";
        // 格式化器
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime date = ZonedDateTime.of(LocalDateTime.parse(strDate, dateTimeFormatter), timeZoneJST);

        // 分别以 上海、纽约、东京三个时区来格式化时间输出。
        DateTimeFormatter zoneFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        logger.info("[上海时区] => zoneId:{} format:{}", timeZoneSH.getId(), zoneFormat.withZone(timeZoneSH).format(date));
        logger.info("[纽约时区] => zoneId:{} format:{}", timeZoneNY.getId(), zoneFormat.withZone(timeZoneNY).format(date));
        logger.info("[东京时区] => zoneId:{} format:{}", timeZoneJST.getId(), zoneFormat.withZone(timeZoneJST).format(date));

    }

    /**
     * No.1 日期时间格式化解析问题  ———— SimpleDateFormat 格式化跨年问题
     * 场景: 初始化一个 Calendar，设置日期时间为 2019 年 12 月 29 日，使用大写的 YYYY 来初始化 SimpleDateFormat
     */
    private static void testFormatAndParseError1() {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        // 法国
        //Locale.setDefault(Locale.FRANCE);
        logger.info("[默认语言环境] => defaultLocale:{}", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        // 2019 年 12 月 29 日
        calendar.set(2019, Calendar.DECEMBER, 29, 0, 0, 0);
        SimpleDateFormat error1Format = new SimpleDateFormat("YYYY-MM-dd");
        logger.info("[格式化] => formatDate:{}", error1Format.format(calendar.getTime()));
        logger.info("[所在周属于的年分] => weekYear:{}", calendar.getWeekYear());
        logger.info("[第一年的第一周] => firstDayOfWeek:{}", calendar.getFirstDayOfWeek());
        logger.info("[] => minimalDaysInFirstWeek:{}", calendar.getMinimalDaysInFirstWeek());
    }

    // 线程问题处理
    private static ThreadLocal<SimpleDateFormat> threadSafeFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * SimpleDateFormat 线程不安全问题
     * PS:
     * 1. SimpleDateFormat 继承了 DateFormat，DateFormat 有一个字段 Calendar;
     * 2. SimpleDateFormat 的 parse 方法调用了 CalendarBuilder 的 establish 方法，来构建 Calendar;
     * 3. establish 方法内部先清空 Calendar 再构建 Calendar，操作未加锁。
     * <p>
     * 处理方式: 见上方 —— 线程问题处理
     */
    private static void testSimpleDateFormatThreadUnSafe() throws InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 20; i++) {
            // 提交20个并发解析时间的任务到线程池。模拟并发环境
            threadPool.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    try {
                        // error
                        logger.info("[并发时间格式化] => date:{}", format.parse("2022-05-25 23:26:00"));
                        // right
                        logger.info("[并发时间格式化] => date:{}", threadSafeFormat.get().parse("2022-05-25 23:26:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
    }

    /**
     * SimpleDateFormat 字符串与格式不匹配问题
     * PS: 0506 当成了月份 506 / 12 = 42
     */
    private static void testSimpleDateFormatUnMatchError() throws ParseException {
        String dateString = "20220506";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        logger.info("[格式化结果] => result:{}", dateFormat.parse(dateString));
    }

    private static DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR).appendLiteral("/")
            .appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral("/")
            .appendValue(ChronoField.DAY_OF_MONTH).appendLiteral(" ")
            .appendValue(ChronoField.HOUR_OF_DAY).appendLiteral(":")
            .appendValue(ChronoField.MINUTE_OF_HOUR).appendLiteral(":")
            .appendValue(ChronoField.SECOND_OF_MINUTE).appendLiteral(".")
            .appendValue(ChronoField.MILLI_OF_SECOND).toFormatter();

    private static void testDateTimeFormatter() {
        LocalDateTime localDateTime = LocalDateTime.parse("2022/5/26 00:10:00.101", dateTimeFormatter);
        // 解析成功
        logger.info("[DateTimeFormatter解析结果] => localDateTime:{}", localDateTime.format(dateTimeFormatter));
        // 解析失败
        String dt = "20220526";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        logger.info("[DateTimeFormatter解析结果] => dt:{}", dateTimeFormatter.parse(dt));
    }

    /**
     * 日期时间的计算
     * 时间戳 int 溢出问题
     */
    public static void testDateCalculate() {

        // 测试时间计算类型精度溢出 int 类型用于毫秒计算非常容易溢出(PS:大概「24.85」天左右就溢出了)
        // Integer.MAX_VALUE 2147483647
        Date today = new Date();
        logger.info("[时间计算调试-错误-精度溢出] => today:{}", today);
        // 30 天之后
        Date nextMonth = new Date(today.getTime() + 24 * 1000 * 60 * 60 * 24);
        logger.info("[时间计算调试-错误-精度溢出] => today:{}", nextMonth);
        // nextMonth 本应是 today 晚 30 天之后，但是这里反而还早了
        logger.info("[时间计算调试-错误-精度溢出] => result:{}", nextMonth.after(today));

        // 正确结果
        // 30 天之后
        Date nextMonthTrue = new Date(today.getTime() + 30L * 1000 * 60 * 60 * 24);
        logger.info("[时间计算调试-正确-精度溢出LONG处理] => result:{}", nextMonthTrue.after(today));

        // Java8 之前的优化方案 ———— Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        logger.info("[时间计算调试-正确-Calender] => calender:{}", calendar.getTime());

        // Java8 之后的解决方案 ———— LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();
        logger.info("[时间计算调试-正确-LocalDateTime] => result:{}", localDateTime.plusDays(30));

        LocalDateTime localDateTime1 = LocalDateTime.now();
        logger.info("[时间计算调试-plus和minus日期加减] => localDateTime1:{}", localDateTime1);
        LocalDateTime resultTime1 = localDateTime1.minus(Period.ofDays(1))
                .plus(1, ChronoUnit.DAYS)
                .minusMonths(1)
                .plus(Period.ofMonths(1));
        logger.info("[时间计算调试-plus和minus日期加减] => resultTime1:{}", resultTime1);

        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = nowLocalDateTime.minusMonths((int) (12 * 10f));
        LocalDateTime endDateTime = nowLocalDateTime.minusMonths((int) (12 * 0.5f));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        logger.info("[时间计算调试] => startDateTime:{}, endDateTime:{}", dateTimeFormatter.format(startDateTime), dateTimeFormatter.format(endDateTime));

    }

    /**
     * 快捷时间调节测试
     * {@link LocalDateTime#with(TemporalAdjuster)}
     */
    public static void testLocalDate_temporalAdjusters() {
        logger.info("[函数调试-本月的第一天] => result:{}", LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()));
        logger.info("[函数调试-今天的第一百天] => result:{}", LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).plusDays(100));
        logger.info("[函数调试-今天之前的一个周六] => result:{}", LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SATURDAY)));
        logger.info("[函数调试-本月最后一个工作日] => result:{}", LocalDateTime.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));

        // 随机添加
        LocalDateTime time = LocalDateTime.now().with(temporal -> temporal.plus(ThreadLocalRandom.current().nextInt(100), ChronoUnit.DAYS));
        logger.info("[函数调试-随机增加100以内天数] => result:{}", time);

    }

    /**
     * 时间访问器
     */
    public static void testLocalDate_temporalAccessor() {
        LocalDateTime nowDate = LocalDateTime.now();
        int month = nowDate.get(ChronoField.MONTH_OF_YEAR);
        int day = nowDate.get(ChronoField.DAY_OF_MONTH);
        if (month == Month.APRIL.getValue() && day == 1) {
            logger.info("[{}]", Boolean.TRUE);
        } else {
            logger.info("[{}]", Boolean.FALSE);
        }
    }

    /**
     * 日期间隔
     */
    public static void testLocalDate_dayBetween() {
        LocalDate today = LocalDate.of(2019, 12, 12);
        LocalDate specifyDate = LocalDate.of(2019, 10, 1);
        logger.info("[计算日期差-错误-Period.between] => result:{}", Period.between(specifyDate, today).getDays());
        //System.out.println(Period.between(specifyDate, today));
        logger.info("[计算日期差-正确-ChronoUnit.DAYS.between] => result:{}", ChronoUnit.DAYS.between(specifyDate, today));
    }

    /**
     * 在把 Date 转换为 LocalDateTime 的时候，需要通过 Date 的 toInstant 方法得到一个 UTC 时间戳进行转换，并需要提供当前的时区，这样才能把 UTC 时间转换为本地日期时间（的表示）。
     * 反过来，把 LocalDateTime 的时间表示转换为 Date 时，也需要提供时区，用于指定是哪个时区的时间表示，也就是先通过 atZone 方法把 LocalDateTime 转换为 ZonedDateTime，然后才能获得 UTC 时间戳
     */
    public static void testDateConvert() {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) throws ParseException, InterruptedException {
        //initDate();
        //testJavaDate();
        //testDiffTimeZone();
        //testDefaultTimeZone();
        //testJavaNewDate();

        //testFormatAndParseError1();
        //testSimpleDateFormatThreadUnSafe();
        //testSimpleDateFormatUnMatchError();
        //testDateTimeFormatter();

        // 日期时间的计算调试
        testDateCalculate();

        //testLocalDate_temporalAdjusters();
        //testLocalDate_temporalAccessor();
        testLocalDate_dayBetween();
    }


}
