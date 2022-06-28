package xyz.fusheng.project.tooldemo.office.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.fusheng.project.tools.elasticsearch.document.ModelDocument;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @FileName: CsvTest
 * @Author: code-fusheng
 * @Date: 2022/6/22 15:37
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CsvTest {

    @Test
    public void testCsv() throws IOException {
        FileWriter fileWriter = new FileWriter(Paths.get("testCsv.csv").toString());
        CSVPrinter csvPrinter = CSVFormat.EXCEL.print(fileWriter);
        Object[] columns = {"模版ID", "模版名称", "模版值", "创建时间", "更新时间", "排序"};
        csvPrinter.printRecord(columns);
        List<ModelDocument> modelDocumentList = IntStream.rangeClosed(1, 200000).mapToObj(i -> new ModelDocument(i + "")).collect(Collectors.toList());
        for (ModelDocument modelDocument : modelDocumentList) {
            Map<String, Object> map = JSON.parseObject(JSON.toJSONString(modelDocument), new TypeReference<Map<String, Object>>() {
            });
            csvPrinter.printRecord(map.values());
        }
        csvPrinter.flush();
        csvPrinter.close();
    }

}
