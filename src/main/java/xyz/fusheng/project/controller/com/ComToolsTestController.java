package xyz.fusheng.project.controller.com;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.icepear.echarts.Line;
import org.icepear.echarts.charts.line.LineAreaStyle;
import org.icepear.echarts.charts.line.LineSeries;
import org.icepear.echarts.components.coord.cartesian.CategoryAxis;
import org.icepear.echarts.render.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @FileName: ToolsTestController
 * @Author: code-fusheng
 * @Date: 2022/5/23 13:52
 * @Version: 1.0
 * @Description: 工具集调试控制层
 */

@RestController
@RequestMapping("/com/tools")
public class ComToolsTestController {

    private static final Logger logger = LoggerFactory.getLogger(ComToolsTestController.class);

    @GetMapping("/test1")
    public String index4Option() {
        Line lineChart = new Line()
                .addXAxis(new CategoryAxis()
                        .setData(new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"})
                        .setBoundaryGap(false))
                .addYAxis()
                .addSeries(new LineSeries()
                        .setData(new Number[]{820, 932, 901, 934, 1290, 1330, 1320})
                        .setAreaStyle(new LineAreaStyle()));
        Engine engine = new Engine();
        Handlebars handlebars = new Handlebars();
        String html = "";
        try {
            Template template = handlebars.compile("/templates/test1");
            html = template.apply(engine.renderJsonOption(lineChart));
        } catch (IOException e) {
            logger.info("异常 => e:{}", e.getMessage(), e);
        }
        return html;
    }

}
