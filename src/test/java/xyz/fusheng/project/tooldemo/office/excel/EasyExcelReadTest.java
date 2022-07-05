package xyz.fusheng.project.tooldemo.office.excel;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.fusheng.project.tools.office.excel.listener.CompanyReadListener;
import xyz.fusheng.project.tools.office.excel.model.CompanyExcel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: EasyExcelReadTest
 * @Author: code-fusheng
 * @Date: 2022/7/3 10:08
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class EasyExcelReadTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(value = "restHighLevelClient4GsbZq")
    private RestHighLevelClient restHighLevelClient4GsbZq;

    // 公司宝 营商云 索引
    private static final String COMPANY_INDEX = "gsb_zq_company_index_";

    //@Test
    public void testEasyExcelSimpleRead() throws IOException {
        List<File> fileList = new ArrayList<>();
        File file1 = new File(Paths.get("1-第二届“凤鸣计划”名单-改.xlsx").toString());
        fileList.add(file1);
        File file2 = new File(Paths.get("2-独角兽企业-改.xls").toString());
        fileList.add(file2);
        File file3 = new File(Paths.get("3-高新技术企业-改.xls").toString());
        fileList.add(file3);
        File file4 = new File(Paths.get("4-科技型中小企业-改.xls").toString());
        fileList.add(file4);
        File file5 = new File(Paths.get("5-专精特新企业-改.xls").toString());
        fileList.add(file5);
        File file6 = new File(Paths.get("6-专精特新小巨人企业-改.xls").toString());
        fileList.add(file6);
        File file7 = new File(Paths.get("7-独角兽、未来独角兽企业名单_20220703164604-改.xlsx").toString());
        fileList.add(file7);
        File file8 = new File(Paths.get("8-朝阳园中关村高新技术企业（截止2022年4月）-改.xlsx").toString());
        fileList.add(file8);
        File file9 = new File(Paths.get("9-朝阳区2021年高新企业总表（不含联系方式）-改.xlsx").toString());
        fileList.add(file9);
        fileList.forEach(file -> {
            EasyExcel.read(file, CompanyExcel.class, new CompanyReadListener(COMPANY_INDEX, restHighLevelClient4GsbZq, objectMapper)).sheet().headRowNumber(1).doRead();
        });

    }

}
