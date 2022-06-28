package xyz.fusheng.project.tools.elasticsearch.document;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @FileName: ModelDocument
 * @Author: code-fusheng
 * @Date: 2022/6/20 16:35
 * @Version: 1.0
 * @Description: Model ES文档对象
 */

@Data
@Document(indexName = "model_index_test", createIndex = true)
@AllArgsConstructor
@NoArgsConstructor
public class ModelDocument implements Serializable {

    private static final long serialVersionUID = -7450070865304608368L;

    /**
     * 模版Id
     */
    @Id
    private String id;

    /**
     * 模版名称
     */
    @MultiField(mainField = @Field(type = FieldType.Keyword),
            otherFields = {@InnerField(suffix = "text", type = FieldType.Text, searchAnalyzer = "")})
    private String modelName;

    /**
     *
     */
    @Field(type = FieldType.Keyword)
    private String modelValue;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private Integer sort;

    public ModelDocument(String id) {
        this.id = id;
        this.modelName = "模版名称-" + id;
        this.modelValue = "模版值-" + id;
        this.createdTime = new Date();
        this.sort = Integer.parseInt(id);
    }
}
