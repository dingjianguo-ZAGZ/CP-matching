package com.su.yupao.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 自定义swagger接口文档的配置
 */
@Configuration
@EnableSwagger2 // 开启Swagger2
@Profile({"dev","test"})
public class SwaggerConfig {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //！！！在这里标注控制器的位置
                .apis(RequestHandlerSelectors.basePackage("com.su.yupao.controller"))
                .paths(PathSelectors.any())
                .build().pathMapping("/api");
        return docket;
    }

    /**
     * api 信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("伙伴匹配系统")
                .description("cpMatch接口文档")
                .version("1.0")
                .termsOfServiceUrl("https://github.com/dingjianguo-ZAGZ")
                .contact(new Contact("dingjianguo","https://github.com/dingjianguo-ZAGZ","2934742811@qq.com"))
                .build();
    }


    
}
