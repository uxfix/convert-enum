package jackson.example;

import com.dekux.spring.EnableConvertEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConvertEnum // 开启自动转换枚举
public class JacksonExampleApp {
    public static void main(String[] args) {
        SpringApplication.run(JacksonExampleApp.class,args);
    }
}
