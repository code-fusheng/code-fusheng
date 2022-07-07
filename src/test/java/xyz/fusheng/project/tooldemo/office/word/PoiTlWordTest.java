package xyz.fusheng.project.tooldemo.office.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.ChartSingleSeriesRenderData;
import com.deepoove.poi.data.Charts;
import com.deepoove.poi.data.Tables;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.BorderStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @FileName: PoiTlWordTest
 * @Author: code-fusheng
 * @Date: 2022/5/23 11:25
 * @Version: 1.0
 * @Description:
 * 参考文档:
 * http://deepoove.com/poi-tl/
 */

public class PoiTlWordTest {

    private static void testCreateTemplate() throws IOException, InvalidFormatException {
        XWPFDocument document = new XWPFDocument();
        // 文本
        XWPFParagraph paragraph1 = document.createParagraph();
        XWPFRun run1 = paragraph1.createRun();
        run1.setText("{{paragraph}} : This is the test word paragraph...");
        // 图片
        XWPFParagraph paragraph2 = document.createParagraph();
        XWPFRun run2 = paragraph2.createRun();
        run2.setText("{{@avatar}}");
        // 表格
        XWPFParagraph paragraph3 = document.createParagraph();
        XWPFRun run3 = paragraph3.createRun();
        run3.setText("{{#table0}}");

        // 图表
        XWPFChart chart = document.createChart();
        chart.setTitleText("demo");   // 图标标题
        chart.setTitleOverlay(false);     // 图例是否覆盖标题
        // 图例设置
        XDDFChartLegend table0Legend = chart.getOrAddLegend();
        table0Legend.setPosition(LegendPosition.TOP);
        // X Y 轴
        String[] xAxisData = {"2021", "2022"};
        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);   // 设置分类数据
        Integer[] yAxisData = {10, 20};
        XDDFNumericalDataSource<Integer> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // 设置值数据
        //
        XDDFPieChartData pieChart = (XDDFPieChartData) chart.createData(ChartTypes.PIE, null, null);
        XDDFChartData.Series series = pieChart.addSeries(xAxisSource, yAxisSource);
        // 绘制图表
        chart.plot(pieChart);

        FileOutputStream out = new FileOutputStream("TestTemplate2.docx");
        document.write(out);
        out.close();
    }

    private static void testCreateWord() throws IOException {
        //ChartMultiSeriesRenderData chart = Charts.ofMultiSeries("ChartTitle", new String[]{"中文", "English"})
        //        .addSeries("countries", new Double[]{15.0, 6.0})
        //        .addSeries("speakers", new Double[]{223.0, 119.0})
        //        .create();
        ChartSingleSeriesRenderData chart = Charts
                .ofSingleSeries("ChartTitle", new String[]{"美国", "中国"})
                .series("countries", new Integer[]{9826675, 9596961})
                .create();
        XWPFTemplate template = XWPFTemplate.compile("TestTemplate2.docx")
                .render(new HashMap<String, Object>() {{
                    put("paragraph", Texts.of("hello!"));
                    put("avatar", "https://aliyun-oss-model.oss-cn-beijing.aliyuncs.com/38C1EE7B-FA6B-44B2-ACE5-74BDDEEC24D9.jpeg");
                    put("table0", Tables.of(new String[][]{new String[]{"00", "01"}, new String[]{"10", "11"}}).border(BorderStyle.DEFAULT).create());
                    //put("chart", chart);
                }});
        template.writeAndClose(new FileOutputStream("TestWord2.docx"));
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        testCreateTemplate();
        testCreateWord();
    }

}
