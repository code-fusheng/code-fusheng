package xyz.fusheng.project.tooldemo.office.word;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @FileName: ApachePoiWordTest
 * @Author: code-fusheng
 * @Date: 2022/5/23 10:36
 * @Version: 1.0
 * @Description: ApachePoiWordTest
 * 参考文档:
 * https://www.tutorialspoint.com/apache_poi_word/index.htm
 * https://www.w3cschool.cn/apache_poi_word/apache_poi_word_overview.html
 */

public class ApachePoiWordTest {

    /**
     * 创建 Word 文档
     */
    private static void testCreateWord() throws IOException {
        // create blank document
        XWPFDocument document = new XWPFDocument();

        // create a blank paragraph 段落
        XWPFParagraph paragraph = document.createParagraph();
        // set border 边界
        paragraph.setBorderBottom(Borders.BASIC_BLACK_DASHES);
        paragraph.setBorderLeft(Borders.BASIC_BLACK_DASHES);
        paragraph.setBorderRight(Borders.BASIC_BLACK_DASHES);
        paragraph.setBorderTop(Borders.BASIC_BLACK_DASHES);

        // create run
        XWPFRun run = paragraph.createRun();
        // setText 文本
        run.setText("{{paragraph}} : This is the test word paragraph...");

        // write the document in file
        FileOutputStream out = new FileOutputStream("TestWord1.docx");
        document.write(out);
        out.close();
    }

    public static void main(String[] args) throws IOException {
        testCreateWord();
    }

}
