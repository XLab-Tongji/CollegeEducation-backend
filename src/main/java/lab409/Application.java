package lab409;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }


}


