package cn.xiaocai.limit.checked.core.auto;


import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Xiaocai.Zhang
 */
@Data
@ConfigurationProperties(prefix = LimitCheckedProperties.LIMIT_PREFIX)
public class LimitCheckedProperties {

    public final static String LIMIT_PREFIX = "limit.checked";
    @Getter
    private boolean enabled = true;
    private long second = 60;
    private long totalCount = 100;

    public LimitCheckedProperties(){
    }

    public LimitCheckedProperties(long second, long totalCount){
        this(true, second, totalCount);
    }

    public LimitCheckedProperties(boolean enabled, long second, long totalCount){
        this.enabled = enabled;
        this.second = second;
        this.totalCount = totalCount;
    }

}
