package cn.xiaocai.limit.checked.demo;

import cn.xiaocai.limit.core.v3.annotation.LimitCheckedScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Xiaocai.Zhang
 */

@SpringBootApplication
// @EnableLimitChecked // use style v2
@LimitCheckedScans(basePackage = "cn.xiaocai.limit.checked.demo") // use style v3
public class LimitCheckedDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LimitCheckedDemoApplication.class, args);
    }

}
