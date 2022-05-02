package com.malcolm.imageapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
        scanBasePackages = "com.malcolm.imageapi",
        exclude = { ErrorMvcAutoConfiguration.class }
)
public class ImageAPIApp {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().title("Image API ")
                .version("0.0.1")
                .description("REST API Image Metadata Processor"));
    }

    /*
    We need to have the tenant_id before we can log start/end
    This is an apsect which run around the rest method
    Need to have another way so that logging is always by tenant
    <Logger name="org.springframework.web.filter.CommonsRequestLoggingFilter" additivity="false" level="DEBUG"><AppenderRef ref="STDOUT"/></Logger>
    import org.springframework.web.filter.CommonsRequestLoggingFilter;

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter  = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setBeforeMessagePrefix("START REQUEST: ");
        filter.setAfterMessagePrefix("END REQUEST:");
        return filter;
    }
    */

    public static void main(String[] args) {
        SpringApplication.run(ImageAPIApp.class, args);
    }
}


