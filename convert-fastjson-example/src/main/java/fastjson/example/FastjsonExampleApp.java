package fastjson.example;

import com.dekux.spring.EnableConvertEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConvertEnum // 开启自动转换枚举
public class FastjsonExampleApp {
    public static void main(String[] args) {
        SpringApplication.run(FastjsonExampleApp.class, args);
    }
}
