package xyz.fusheng.project.errordemo;

import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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
 *
 */

public class FileIOErrorTest {

    private static final Logger logger = LoggerFactory.getLogger(FileIOErrorTest.class);

    /**
     * 操作场景描述: 这里使用 GBK 编码把 "hello 浮生" 写入一个名为 hello.txt 的文本文件，然后直接以字节数组的形式读取文件内容，转换成 16 进制字符串输出到日志
     */
    private static void testBaseFileReadWrite() throws IOException {
        Files.deleteIfExists(Paths.get("hello.txt"));
        Files.write(Paths.get("hello.txt"), "hello 浮生".getBytes(Charset.forName("GBK")));
        // bytes:68656C6C6F20B8A1C9FA
        logger.info("bytes:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get("hello.txt"))).toUpperCase());
    }

    /**
     * 错误的文件读取代码
     */
    private static void testErrorFileRead() throws IOException {
        char[] chars = new char[10];
        String content = "";
        try (FileReader fileReader = new FileReader("hello.txt")) {
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

    private static void testCurrentLocalCharset() throws IOException {
        logger.info("charset:{}", Charset.defaultCharset());
        Files.write(Paths.get("hello_right.txt"), "hello 浮生".getBytes(Charsets.UTF_8));
        // bytes:68656C6C6F20E6B5AEE7949F
        logger.info("bytes:{}", Hex.encodeHexString(Files.readAllBytes(Paths.get("hello_right.txt"))).toUpperCase());
    }

    public static void main(String[] args) throws IOException {
        //testBaseFileReadWrite();
        //testErrorFileRead();
        testCurrentLocalCharset();
    }

}
