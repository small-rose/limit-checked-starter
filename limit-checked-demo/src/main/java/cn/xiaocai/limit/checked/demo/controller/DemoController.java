package cn.xiaocai.limit.checked.demo.controller;

import cn.xiaocai.limit.checked.core.annotation.LimitChecked;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xiaocai.Zhang
 */
@Slf4j
@RestController
public class DemoController {
    /**
     * use @LimitChecked
     * 访问该接口限制：10秒钟内只能访问2次
     * @return String
     */
    @RequestMapping("/test")
    @LimitChecked(name = "test", second = 10, totalCount = 2)
    public String test(){
        return "ok";
    }

    /**
     * use @LimitChecked just limit on call success
     * when exception happened ,the limit numbers not count
     * 访问该接口限制：10秒内，限制成功访问2次
     * @return String
     */
    @RequestMapping("/onSuccess")
    @LimitChecked(name = "success", second = 10, totalCount = 2, onlySuccessCount = true)
    public String success(){
        return "ok";
    }

    /**
     * when no params
     * use global application.properties
     * 该接口的限制参数都没有配置，则使用 application.properties 全局配置
     * @return String
     */
    @RequestMapping("/test2")
    @LimitChecked(name = "global")
    public String global(){
        return "ok";
    }
}
