package xyz.fusheng.project.tools.pdfbox;

import com.alibaba.fastjson.JSON;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @FileName: PdfboxTest
 * @Author: code-fusheng
 * @Date: 2022/4/28 20:50
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
public class PdfboxTest {

    private static final Logger logger = LoggerFactory.getLogger(PdfboxTest.class);

    @Test
    public void test1() throws IOException {
        PDDocument document = PDDocument.load(new File("/Users/zhanghao/IdeaProjects/fusheng-work/github/springboot-project-cli/source/DOC_V1.pdf"));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int pageCounter = 0;
        // PDF 文字提取器
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        logger.info("text:{}", text);
    }

    @Test
    public void test2() throws IOException {
        PDDocument document = PDDocument.load(new File("/Users/zhanghao/IdeaProjects/fusheng-work/github/springboot-project-cli/source/DOC_V1.pdf"));
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.addOperator(new SetStrokingColorSpace());
        stripper.addOperator(new SetNonStrokingColorSpace());
        stripper.addOperator(new SetStrokingDeviceCMYKColor());
        stripper.addOperator(new SetNonStrokingDeviceCMYKColor());
        stripper.addOperator(new SetNonStrokingDeviceRGBColor());
        stripper.addOperator(new SetStrokingDeviceRGBColor());
        stripper.addOperator(new SetNonStrokingDeviceGrayColor());
        stripper.addOperator(new SetStrokingDeviceGrayColor());
        stripper.addOperator(new SetStrokingColor());
        stripper.addOperator(new SetStrokingColorN());
        stripper.addOperator(new SetNonStrokingColor());
        stripper.addOperator(new SetNonStrokingColorN());
        String text = stripper.getText(document);
        logger.info("text:{}", text);
    }

    @Test
    public void test3() throws IOException {
        File file1 = new File("/Users/zhanghao/IdeaProjects/fusheng-work/github/springboot-project-cli/source/PDF测试模版旧.pdf");
        File file2 = new File("/Users/zhanghao/IdeaProjects/fusheng-work/github/springboot-project-cli/source/DOC_V1.pdf");
        PDDocument pdDocument = PDDocument.load(file2);
        // Document 结构
        COSDocument cosDocument = pdDocument.getDocument();

        // TODO Trailer 字典
        // Trailer 字典 - 提供有关如何阅读其内容的信息文件中的对象
        COSDictionary trailer = cosDocument.getTrailer();
        // Size - 交叉引用表中的条目总数
        COSBase size = trailer.getItem(COSName.SIZE);
        logger.info("size:{}", JSON.toJSONString(size));
        // Root - 文件目录
        COSBase root = trailer.getItem(COSName.ROOT);
        // Info - 文档的文档信息字典
        COSBase info = trailer.getItem(COSName.INFO);
        // Id - 唯一标识工作流程文件,第一个字符串在首次创建时确定;第二个字符串在工作流系统修改文件时修改
        COSBase id = trailer.getItem(COSName.ID);

        // TODO Info 文件信息字典 包含了 Title、Author等在内的 12 项信息

        // TODO Root 文件目录
        // Type : Catalog
        // Pages : 页面树的根节点 --> Kids --> [Contents, Mediabox, Parent, Recources, Type]
        // Outlines : 大纲字典

        // PageTree
        PDPageTree pageTree = pdDocument.getPages();
        // page
        for (PDPage page : pageTree) {
            // 资源 - 包括例如字体
            PDResources resources = page.getResources();
            // 页面内容 - 绘制文本和图形的说明
            InputStream contents = page.getContents();
            //logger.info("contents:{}", contents);
        }

        CustomPDFTextStripper stripper = new CustomPDFTextStripper();
        stripper.setStartPage(2);
        stripper.setEndPage(2);
        String text = stripper.getText(pdDocument);
        logger.info("text:{}", text);
    }

}
