/**
 * 一、[@SpringBootApplication] : 是一个复合注解，包括 @ComponentScan、@SpringBootConfiguration、@EnableAutoConfiguration
 * 1. @Target(ElementType.TYPE): 这个枚举类型常量提供了一个简单的分类 注释可能出现在Java程序中的语法位置
 *                             （这些常量与元注释类型(@Target)一起指定在何处写入注释的合法位置）
 * 2. @Retention(RetentionPolicy.RUNTIME): 这个枚举类型的常量保留了注释的各种策略，
 *                                        它们与元注释(@Retention)一起指定注释要保留多长时间
 * 3. @Documented 注解表明这个注释是由 javadoc 记录的，在默认情况下也有类是的记录工具，如果一个类型声明被注释了文档化，它的注释称为公共API的一部分
 * 4. @ComponentScan: 扫描当前包以及其子包下被 @Component、@Controller、@Service、@Repository 注解标记的类并纳入 Spring 容器中进行管理。
 *                    => <context:component-scan> 等价于xml使用标签配置
 * 5. @SpringBootConfiguration: 继承 @Configuration, 二者功能一致，标注当前类是配置类，
 *                             并且将当前类内部声明的一个或者多个以 @Bean 标注的方法实例纳入到 Spring 容器中，并且实例名就是方法名
 * 6. EnableAutoConfiguration: 作用启动自动配置，注解的意思是说 spring boot 根据添加的jar包来配置项目的默认配置，
 *                            比如根据 spring-boot-starter-web 来判断项目是否需要webmvc/tomcat
 *
 * 二、[@EnableTransactionManagement] : 开启事务支持注解，然后可以在 Service 业务逻辑的方法上添加注解 @Transactional
 * 事务管理器: 不管是 JPA 还是 JDBC 都是事先子接口 PlatformTransactionManager
 * 1. spring-boot-starter-jdbc 依赖，默认注入 DataSourceTransactionManager 实例
 * 2. spring-boot-starter-data-jpa 依赖，默认注入 JpaTransactionManager 实例
 * 3、@Import: 导入第三方资源（普通的java类） 以 Bean 的形式注入到 Spring 容器
 *
 * 事务生效原则:
 * 1. 除非特殊配置，否则只能在 public 方法上加 @Transactional 注解；
 * PS: Spring 默认通过动态代理的方式实现 AOP，对目标方法进行增强，private 修饰的方法无法被代理到。
 * 2. 必须通过代理过的类，从外部调用目标方法才能生效。
 * 3. 只有异常传播出了标记 @Transactional 注解的方法，事务才能回滚。
 * 4. 默认情况下 @Transactional 只处理 RuntimeException(非受检异常)或 Error，可以通过 rollbackFor 和 noRollbackFor 属性覆盖默认配置
 * PS: DefaultTransactionAttribute public boolean rollbackOn(Throwable ex) { return ex instanceof RuntimeException || ex instanceof Error; }
 * 5. 如果方法涉及多次数据库操作，并且希望作为单独的事务处理，可以通过 Propagation 属性做事务传播隔离
 *
 *
 * 三、[@MapperScan] : mapper 包扫描 注解原理 --> @MapperScan 是根据其注解上的 @Import({MapperScannerRegistrar.class}) 进行自定配置的
 * 注解思路: 首先根据标注的 @MapperScan 获取 basePackage 或者 @Mapper 或者所在的 package,之后通过 ClassPathMapperScanner 去扫描包，
 *          获取所有 Mapper 接口类的 BeanDefinition，之后具体配置，设置 beanClass 为 MapperFactoryBean,设置 MapperFactoryBean 的构造器
 *          参数为实际的 Mapper 接口类，通过 ClassPathBeanDefinitionScanner 父类进行 bean 注册，自动注入的时候，就会调用 MapperFactoryBean
 *          的 getObject 方法获取实际类型的实例
 * 简而言之: 1.@Mapper --> 把 mapper 接口类注册成 spring 的 bean
 *          2.@MapperScan --> 扫描包下的接口，批量注册成 bean
 *          作用一致，写一个就行
 *
 * 1、Spring Boot 源码解析 : 关键点 spring 与 spring boot 技术衔接点 refresh
 * 2、重点 ？ 准备工作 自动装配 tomcat的内嵌 监听器listener 整个启动流程
 */
package xyz.fusheng.project;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author code-fusheng
 */
@SpringBootApplication
@MapperScan("xyz.fusheng.project.core.mapper")
@EnableTransactionManagement
public class ProjectApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProjectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerReady(WebServerInitializedEvent event) {
        logger.info("当前 WebServer 实现类: " + event.getWebServer().getClass().getName());
    }

}
