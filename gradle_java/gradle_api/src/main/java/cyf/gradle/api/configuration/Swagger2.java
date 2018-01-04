package cyf.gradle.api.configuration;

import cyf.gradle.base.model.HearderForDoc;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2017-11-30 17:40
 *
 **/
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        //文档header参数，未针对具体api是否需要header参数，全部设为false
        List<Parameter> pars = new ArrayList<Parameter>();
        HearderForDoc header = new HearderForDoc();
        Field[] fields = header.getClass().getDeclaredFields();
        for (Field field : fields) {
            ParameterBuilder param = new ParameterBuilder();
            String key = field.getName();
            param.name(key).description(key).modelRef(new ModelRef("string")).parameterType("header").required(false);
            pars.add(param.build());
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cyf.gradle.api.controller"))
                .paths(PathSelectors.any())
                .build();

    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger2构建RESTful APIs")
                .description("Hello Swagger2")
                .termsOfServiceUrl("")
                .contact("程宇飞")
                .version("1.0")
                .build();
       /* Contact contact=new Contact("作者名",
                "http://www.jianshu.com/u/f192766abeab","email地址");
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2")
                .description("Hello Swagger2")
                //.termsOfServiceUrl("http://www.jianshu.com/u/f192766abeab")
                //.contact("作者名")
                .contact(contact)
                .version("1.0")
                .build();*/
    }

}
