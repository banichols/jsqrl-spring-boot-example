package org.jsqrl.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Brent Nichols on 7/10/2016.
 */
@SpringBootApplication
@ComponentScan({"org.jsqrl"})
public class ExampleApp extends SpringBootServletInitializer {

    public static void main(String args[]){
        SpringApplication.run(ExampleApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ExampleApp.class);
    }

}
