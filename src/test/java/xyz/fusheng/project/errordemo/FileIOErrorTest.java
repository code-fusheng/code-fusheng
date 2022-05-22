package xyz.fusheng.project.errordemo;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @FileName: FileIOErrorTest
 * @Author: code-fusheng
 * @Date: 2022/5/18 06:53
 * @Version: 1.0
 * @Description: 文件IO - 错误用例测试
 *
 * PS: 文件内容存储说明
 * 1. 在计算机中，不管是什么文字，都是按照一定的"字符集"规则以二进制保存的。字符集枚举了所有支持的字符映射成二进制关系的映射表。
 *    我们在处理文件读写的时候，如果是在「字节」的层面进行操作，那么就不会涉及到字符编码问题；
 *    而如果需要在「字符」层面进行读写的化，就需要明确字符的编码方式(字符集)。
 * 2. FileReader 类默认使用当前机器的字符集来读取目标字符串，如果写入时的字符集规则与机器的不一致，就会导致乱码。
 *    这里我们一般使用 FileInputStream 获取文件流，然后通过 InputStreamReader 读取字符流，并且指定相应的字符集规则。
 * 3. 使用 Files 类进行一些流式处理操作，注意使用 try-with-resources 包装 Stream，确保底层文件资源可以释放，避免 too many open files 问题。
 * 4. 进行文件字节流操作时，一般情况下不考虑进行逐字节操作，使用缓冲区进行批量读写 IO 次数，性能会好很多。
 *    一般来说可以直接使用缓冲输入输出流 BufferedXXXStream，追求性能极限的话可以考虑 FileChannel 进行流转发。
 *
 * 5. Java 的 File 类和 Files 类提供的文件复制、重命名、删除等操作都是 「非原子性」的。
 */

public class FileIOErrorTest {

    private static final Logger logger = LoggerFactory.getLogger(FileIOErrorTest.class);

    /**
     * 操作场景描述: 这里使用 GBK 编码把 "hello 浮生" 写入一个名为 hello_error.txt 的文本文件，然后直接以字节数组的形式读取文件内容，转换成 16 进制字符串输出到日志
     */
    private static void testBaseFileReadWrite() throws IOException {
        Files.deleteIfExists(Paths.get("hello_error.txt"));
        Files.write(Paths.get("hello_error.txt"), "hello 浮生".getBytes(Charset.forName("GBK")));
        // bytes:68656C6C6F20B8A1C9FA
        logger.info("bytes:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get("hello_error.txt"))).toUpperCase());
    }

    /**
     * 错误的文件读取方式
     */
    private static void testErrorFileRead() throws IOException {
        char[] chars = new char[10];
        String content = "";
        try (FileReader fileReader = new FileReader("hello_error.txt")) {
            int count;
            // 使用 FileReader 类以字符方式进行文件读取，会得到乱码 hello ����
            // ? FileReader 是以当前机器的默认字符集来读取文件的，如果希望指定字符集的话，需要直接使用 InputStreamReader 和 FileInputStream
            while ((count = fileReader.read(chars)) != -1) {
                content += new String(chars, 0, count);
            }
        }
        // hello ����
        logger.info("content:{}", content);
    }

    /**
     * 正确的文件读取方式
     */
    private static void testRightFileRead() throws IOException {
        char[] chars = new char[10];
        String content = "";
        try (FileInputStream fileInputStream = new FileInputStream("hello_error.txt");
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("GBK"))) {
            int count;
            while ((count = inputStreamReader.read(chars)) != -1) {
                content += new String(chars, 0, count);
            }
        }
        // content:hello 浮生
        logger.info("content:{}", content);
    }

    private static void testCurrentLocalCharset() throws IOException {
        logger.info("charset:{}", Charset.defaultCharset());
        Files.write(Paths.get("hello_right.txt"), "hello 浮生".getBytes(Charsets.UTF_8));
        // bytes:68656C6C6F20E6B5AEE7949F
        logger.info("bytes:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get("hello_right.txt"))).toUpperCase());
    }

    /**
     * JDK1.7 推出的 Files 类的 readAllLines 方法
     * ? 这种方式存在问题 —— 读取超出内存大小的文件时会出现 OOM。
     * ! 在 {@link Files#readAllLines(Path, Charset)} 方法的源码中，会将读取文件的所有内容存放至 List<String> 中返回，如果内存无法容纳这个 List，就会 OOM
     */
    private static void testFiles_readAllLines() throws IOException {
        String content = Files.readAllLines(Paths.get("hello_error.txt"), Charset.forName("GBK")).stream().findFirst().orElse("");
        logger.info("content:{}", content);
    }

    /**
     * ? 上面的 readAllLines 存在 OOM 的问题，那么是否有办法实现按需的流式读取呢? 比如需要消费某一行数据的时候再读取，而不是一次性读取到内存?
     * ! {@link Files#lines(Path, Charset)}
     * 场景描述: 这里我们尝试读取一个 1 亿 1 万行的文件，占用磁盘空间超过 4G，
     * 如果使用 -Xmx512m -Xms512m 启动 JVM 控制最大堆内存为 512M 的话，肯定是不行的，但是使用 Files.lines 方法是没有问题的！
     *
     * 使用问题: 读取完文件后没有关闭。我们通常会认为静态方法的调用不涉及资源释放，因为方法调用结束自然代表资源使用完成，有 API 释放资源。
     * 但是! 对于 Files 类一些返回 Stream 的方法并不是如此。
     */
    private static void testFiles_lines() throws IOException {
        logger.info("lines:{}", Files.lines(Paths.get("test.txt")).limit(10).collect(Collectors.toList()).size());
    }

    /**
     * 错误的资源释放
     * 使用 lsof 命令查看进程打开了 1万 多个 linesFreed.txt 文件
     */
    private static void testFiles_lines_freedError() throws IOException {
        // 随便写入 10 行数据
        Files.deleteIfExists(Paths.get("linesFreed.txt"));
        Files.write(Paths.get("linesFreed.txt"), IntStream.rangeClosed(1, 10).mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toList()), StandardCharsets.UTF_8);
        // 使用 Files.lines 读取文件 100万 次
        LongAdder longAdder = new LongAdder();
        IntStream.rangeClosed(1, 100000).forEach(i -> {
            try {
                Files.lines(Paths.get("linesFreed.txt")).forEach(lien -> longAdder.increment());
            } catch (IOException e) {
                // linesFreed.txt: Too many open files
                e.printStackTrace();
            }
        });
        logger.info("total:{}", longAdder.longValue());
    }

    /**
     * 查看 {@link Files#lines(Path)} 源码可以发现，Stream 的 close 注册了一个回调，来关闭 BufferedReader 进行资源释放
     */
    private static void testFiles_lines_freedRight() throws IOException {
        // 随便写入 10 行数据
        Files.deleteIfExists(Paths.get("linesFreed.txt"));
        Files.write(Paths.get("linesFreed.txt"), IntStream.rangeClosed(1, 10).mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toList()), StandardCharsets.UTF_8);
        // 使用 Files.lines 读取文件 100万 次
        LongAdder longAdder = new LongAdder();
        IntStream.rangeClosed(1, 100000).forEach(i -> {
            try (Stream<String> lines = Files.lines(Paths.get("linesFreed.txt"))) {
                lines.forEach(line -> longAdder.increment());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // total:1000000
        logger.info("total:{}", longAdder.longValue());
    }

    /**
     * 测试文件读写缓冲区
     * 场景描述: 创建一个文件随机写入 100万 行数据，文件大小 35M 左右
     */
    private static void testBufferPreBuild() throws IOException {
        Files.deleteIfExists(Paths.get("source_test.txt"));
        Files.write(Paths.get("source_test.txt"),
                IntStream.rangeClosed(1, 1000000).mapToObj(i -> UUID.randomUUID().toString()).collect(Collectors.toList()), StandardCharsets.UTF_8);
    }

    /**
     * 错误的缓冲区使用
     * 场景描述: 使用 FileInputStream 获的一个文件输入流，然后调用其 read 方法每次读取一个字节，
     * 最后通过一个 FileOutputStream 文件输出流把处理后的结果写入另一个文件。
     * 这里为了简化流程，相当于文件复制。
     *
     * ! 显然，每次读取一个字节，写入一个字节都进行一次 IO 操作，代价太大了。需要使用缓冲区！
     */
    private static void testBufferError() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("testBufferError");
        try (FileInputStream fileInputStream = new FileInputStream("source_test.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("target_test.txt")) {
            int i;
            while ((i = fileInputStream.read()) != -1) {
                fileOutputStream.write(i);
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        // 151977215514 ns  ~ 2分钟30秒
    }

    /**
     * 缓冲区 —— 一次性从原文件读取一定数量的数据到缓冲区，一次性写入一定数量的数据到目标文件。
     */
    private static void testBufferRight1() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("testBuffer100");
        try (FileInputStream fileInputStream = new FileInputStream("source_test.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("target_test2.txt")) {
            byte[] buffer = new byte[100];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0 ,len);
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        // 1609781826 ns ~ 1.61s
    }

    /**
     * 通过上面的缓冲区加载数据，可能认为比较麻烦。
     * {@link BufferedInputStream} 和 {@link BufferedOutputStream} 可以实现输入输出流的缓冲处理，内部默认实现了一个 8KB 大小的缓冲区。
     * case1: 直接使用 BufferedInputStream 和 BufferedOutputStream;
     * case2: 额外使用一个 8KB 的缓冲，使用 BufferedInputStream 和 BufferedOutputStream;
     * case3: 直接使用 FileInputStream 和 FileOutputStream
     *
     * result ----------
     * 1371078903  091%  case1 1.37s
     * 071637593  005%  case2 71.64ms
     * 066695201  004%  case3 66.7ms
     * PS: 分析说明 —— 第一种方式虽然使用了缓冲区，但逐字节的操作因为方法调用的次数还是是在太多了，依旧比较慢；
     *     后面两种性能相当，毫秒级别，两者使用的都是 8KB 大小的缓冲区。
     * 这里只是为了演示三种固定大小的缓冲区的使用，实际生产中需要读取的字节数是很可能不固定的，有时候几个字节，有时候几百字节，这个时候如果有一个固定大小的缓冲区作为后背的稳定二次缓冲，是很有意义的。
     *
     */
    private static void testBufferRight2() throws IOException {
        // case1
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("case1");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("source_test.txt"));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("target_test3.txt"))) {
            int i;
            while ((i = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(i);
            }
        }
        stopWatch.stop();
        // case2
        stopWatch.start("case2");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("source_test.txt"));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("target_test4.txt"))) {
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
            }
        }
        stopWatch.stop();
        // case3
        stopWatch.start("case3");
        try (FileInputStream fileInputStream = new FileInputStream("source_test.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("target_test5.txt")) {
            byte[] buffer = new byte[8192];
            int len = 0;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    /**
     * 补充说明:
     * 如果是类似于文件复制的操作，如果希望有更高的性能，可以使用 {@link FileChannel#transferTo(long, long, WritableByteChannel)} 方法进行流的复制。
     * 在某些操作系统，可以实现 DMA(直接内存访问)，也就是数据从磁盘经过总线直接发送到目标文件，无需经过内存和 CPU 进行数据中转。
     */
    private static void testFileChannel_transferTo() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("case4");
        FileChannel in = FileChannel.open(Paths.get("source_test.txt"), StandardOpenOption.READ);
        FileChannel out = FileChannel.open(Paths.get("target_test6.txt"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        in.transferTo(0, in.size(), out);
        stopWatch.stop();
        // 028205492  100%  case4 28.21ms
        System.out.println(stopWatch.prettyPrint());
    }

    public static void main(String[] args) throws IOException {
        // 文件读写字符集规则问题
        //testBaseFileReadWrite();
        //testErrorFileRead();
        //testCurrentLocalCharset();
        //testRightFileRead();

        // 资源释放问题
        // testFiles_readAllLines();
        // testFiles_lines();
        // testFiles_lines_freedError();
        // testFiles_lines_freedRight();

        // 缓冲区问题
        testBufferPreBuild();
        // testBufferError();
        // testBufferRight1();
        //testBufferRight2();
        testFileChannel_transferTo();
    }

}
