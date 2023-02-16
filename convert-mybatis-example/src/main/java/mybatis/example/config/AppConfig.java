package mybatis.example.config;

import com.dekux.core.ConfigTypeEnum;
import com.dekux.core.IOTypeEnum;
import com.dekux.spring.ConvertEnumContainerCustomizer;
import com.dekux.spring.DefaultConvertConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * 实现自定义配置
     */
    @Bean
    public ConvertEnumContainerCustomizer convertEnumContainerCustomizer(){
        return container -> {
            // 设置 Jackson 的输入类型为 code,输出类型为 desc
            container.setConfig(ConfigTypeEnum.JACKSON,
                    new DefaultConvertConfig(IOTypeEnum.CODE,IOTypeEnum.DESC));
            // 设置 Mybatis 的输入和输出类型都为 code
            container.setConfig(ConfigTypeEnum.MYBATIS,
                    new DefaultConvertConfig(IOTypeEnum.CODE,IOTypeEnum.CODE));
        };
    }
}
