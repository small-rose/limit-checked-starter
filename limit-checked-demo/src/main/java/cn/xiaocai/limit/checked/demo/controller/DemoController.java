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
     * @return
     */
    @RequestMapping("/test")
    @LimitChecked(name = "test", second = 10, totalCount = 2)
    public String test(){
        return "ok";
    }

    /**
     * when no params
     * use global application.properties
     * @return
     */
    @RequestMapping("/test2")
    @LimitChecked(name = "global")
    public String global(){
        return "ok";
    }
}
