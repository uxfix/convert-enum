package mybatis.example;

import com.dekux.spring.EnableConvertEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConvertEnum // 开启自动注解转换
public class MybatisExampleApp {
    public static void main(String[] args) {
        SpringApplication.run(MybatisExampleApp.class, args);
    }
}
