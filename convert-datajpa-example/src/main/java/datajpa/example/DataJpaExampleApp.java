package datajpa.example;

import com.dekux.spring.EnableConvertEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConvertEnum // 开启枚举自动转换
public class DataJpaExampleApp {
    public static void main(String[] args) {
         SpringApplication.run(DataJpaExampleApp.class, args);
    }
}
