package xyz.fusheng.project.model.domain;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.fusheng.project.model.base.BaseModel;

/**
 * @FileName: Model
 * @Author: code-fusheng
 * @Date: 2022/5/3 20:54
 * @Version: 1.0
 * @Description:
 */

@Api(tags = "领域驱动模版模型")
@Data
@AllArgsConstructor
public class Model extends BaseModel {

    private String id;

    private String label;

    private String value;

}
