package xyz.fusheng.project.tools.office.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @FileName: CompanyWriteExcel
 * @Author: code-fusheng
 * @Date: 2022/7/6 11:08
 * @Version: 1.0
 * @Description:
 */

@Data
public class CompanyWriteExcel {

    @ExcelProperty(index = 0)
    private String companyName;

    @ColumnWidth(35)
    @ExcelProperty(value = "统一社会信用代码（必填）", index = 1)
    private String orgNo;

}
