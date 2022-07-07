package xyz.fusheng.project.tooldemo.freemarker;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import freemarker.template.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @FileName: FreeMarkerTest
 * @Author: code-fusheng
 * @Date: 2022/7/5 15:21
 * @Version: 1.0
 * @Description:
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreeMarkerTest {

    private static final Logger logger = LoggerFactory.getLogger(FreeMarkerTest.class);

    @Test
    public void testFreeMarkerHelloWord() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        // IDEA 运行无误, Jar 包存在异常
        // File file = new File("src/main/resources/templates/ftl");
        // IDEA 运行无误, Jar 包存在异常
        // File file = ResourceUtils.getFile("classpath:templates/ftl");
        // IDEA 运行无误, Jar 运行无异常
        File file = new ClassPathResource("templates/ftl").getFile();
        configuration.setDirectoryForTemplateLoading(file);
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("test_model.ftl");
        Map<Object, Object> dataModel = new HashMap<>();
        dataModel.put("hello", "hello word! by @code-fusheng.");
        FileWriter out = new FileWriter(Paths.get("hello.html").toString());
        template.process(dataModel, out);
        out.close();
    }

}
