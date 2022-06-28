package xyz.fusheng.project.tools.office.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * @FileName: ModelExcel
 * @Author: code-fusheng
 * @Date: 2022/6/23 11:27
 * @Version: 1.0
 * @Description: Model EasyExcel 模版对象
 */

@Data
public class ModelExcel {

    @ExcelIgnore
    private String id;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "模版名称", index = 0)
    private String modelName;

    @ExcelProperty(value = "模版值")
    private String modelValue;

    private Date createdTime;

    private Date updatedTime;

    private Integer sort;

}
