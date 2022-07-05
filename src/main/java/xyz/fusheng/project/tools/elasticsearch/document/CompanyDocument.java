package xyz.fusheng.project.tools.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CompanyDocument implements Serializable {

    // 公司id
    private String companyId;

    // 公司名
    private String companyName;

    // 公司来源：用友、公司宝
    private String source;

    // 归属省份
    private String base;

    // 省
    private String province;

    // 地级市
    private String city;

    // 区
    private String county;

    // 园区id
    private List<String> parkIdList;

    // 信用代码
    private String creditCode;

    // 法人姓名
    private String legalPersonName;

    // 联系方式
    private String contactTel;

    // 联系邮箱
    private String contactEmail;

    // 网站
    private String website;

    // 注册号
    private String regNumber;

    // 公司类型
    private String companyOrgType;

    // 公司类型1 (括号外的内容+没括号的) , 比如 有限责任公司(自然人投资)-> 有限责任公司 ，个体工商户->个体工商户
    private String companyOrgType1;

    // 公司类型2 (括号内的内容+没括号的), 比如 有限责任公司(自然人投资)-> 自然人投资 ，个体工商户->个体工商户
    private String companyOrgType2;

    // 注册地址
    private String regLocation;

    // 成立年份
    private Integer establishYear;

    // 成立日期
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date establishTime;

    // 营业期限开始日期
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date fromTime;

    // 营业期限终止日期
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date toTime;

    // 经营范围
    private String businessScope;

    // 登记机关
    private String regInstitute;

    // 核准日期
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvedTime;

    // 企业状态
    private String regStatus;

    // 注册资金
    private BigDecimal regCapital;

    // 注册资本单位
    private String regCapitalUnit;

    // 实缴资金
    private String actualCapital;

    // 实缴资本单位
    private String actualCapitalUnit;

    // 组织机构代码
    private String orgNumber;

    // 股票号
    private String bondNum;

    // 股票名称
    private String bondName;

    // 1级行业
    private String industry1;

    // 2级行业
    private String industry2;

    // 3级行业
    private String industry3;

    // 母公司名
    private String parentCompanyName;

    // 母公司所属省份
    private String parentCompanyProvince;

    // 母公司所属地级市
    private String parentCompanyCity;

    // 创建日期
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // 修改日期
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // 登记机关
    private String registerOffice;

    // 上市代码
    private String listCode;

    // 纳税人识别号
    private String taxpayerNumber;

    // 历史名称
    private String originalName;

    // 参保人数
    private String insuredPeople;

    // 人员规模
    private String peopleScale;

    // 简介
    private String introduction;

    // 英文名
    private String englishName;

    // 20220506 需求新增字段

    // 知识产权

    // 商标信息-数量
    private Long tmFlag;

    // 专利信息-数量
    private Long patentFlag;

    // 专利类型 from: ES聚合结果
    private String patentTypes;

    // 网站备案信息 - 未处理
    private Long websiteFilingFlag;

    // 软件著作权
    private Long crSoftwareFlag;

    // 作品著作权
    private Long crWorkFlag;

    // 资质证书 & 行政许可 from: 类型 ES聚合结果 - 未处理
    private String licenseTypes;

    private List<String> licenseTypeList;

    // 经营信息

    // 上市状态
    private String stockStatus;

    // 上市类型 - 非新增
    private String stockType;

    // 融资轮次
    private String financeRound;

    // 招投标
    private Long bidFlag;

    // 企业标签 数据库标签 + Excel资源标签
    private String companyTags;

    private List<String> companyTagList;

    // 风险信息

    // 失信信息
    private Long dishonestFlag;

    // 破产重组
    private Long bankruptcyFlag;

    // 行政处罚
    private Long punishmentFlag;

    // 法律诉讼
    private Long lawSuitFlag;

    // 动产抵押
    private Long mortgageFlag;

    // 司法拍卖
    private Long judicialSaleFlag;

    // 清算信息
    private Long liquidatingFlag;

    // 经营异常
    private Long operateAbnormalFlag;

}
