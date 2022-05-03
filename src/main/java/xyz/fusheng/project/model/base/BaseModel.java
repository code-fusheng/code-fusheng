package xyz.fusheng.project.model.base;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @FileName: BaseModel
 * @Author: code-fusheng
 * @Date: 2022/5/3 20:56
 * @Version: 1.0
 * @Description: 基础公共模版实体
 * 1. @Data : lombok 插件的声明注解 等价于上面的@Setter、@Getter、@RequiredArgsConstructor、@ToString、@EqualsAndHashCode
 * 2. @JsonInclude(JsonInclude.Include.NON_NULL) : 实体类与json互转的时候 属性值为null的不参与序列化， 可在 application.yml 里面全局配置
 */

@ApiModel("基础公共模版实体")
@Data
public class BaseModel implements Serializable {

    /**
     * 1. File -> setting -> plugins  安装 GenerateSerialVersionUID 插件
     * 2. Win 光标 Serializable Alt+Insert 生成序列化唯一标识
     * 3. Mac Serializable command + N
     */
    private static final long serialVersionUID = 1995911544148641869L;


}
