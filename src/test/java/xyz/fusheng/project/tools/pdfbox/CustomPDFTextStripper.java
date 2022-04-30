package xyz.fusheng.project.tools.pdfbox;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @FileName: CustomPDFTextStripper
 * @Author: code-fusheng
 * @Date: 2022/4/29 15:07
 * @Version: 1.0
 * @Description:
 */

public class CustomPDFTextStripper extends PDFTextStripper {

    private static final Logger logger = LoggerFactory.getLogger(CustomPDFTextStripper.class);

    public CustomPDFTextStripper() throws IOException {
        super.setSortByPosition(true);
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        super.processTextPosition(text);
        logger.info("x:{}, y:{}, textPosition:{}, fontSize:{}, fontType:{}", text.getEndX(), text.getEndY(), text, text.getFontSize(), text.getFont());
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        super.writeString(text, textPositions);
    }
}
