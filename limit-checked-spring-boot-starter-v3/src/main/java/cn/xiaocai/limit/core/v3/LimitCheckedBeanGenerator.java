package cn.xiaocai.limit.core.v3;


import cn.xiaocai.limit.core.v3.annotation.LimitChecked;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * @description: TODO 功能角色说明：
 * TODO 描述：
 * @author: 张小菜
 * @date: 2020/12/4 22:49
 * @version: v1.0
 */
public class LimitCheckedBeanGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        //从自定义注解中拿name
        String name = getNameByServiceFindAnnotation(definition,registry);
        if(name != null && !"".equals(name)){
            return name;
        }
        //走父类的方法
        return super.generateBeanName(definition, registry);
    }

    private String getNameByServiceFindAnnotation(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanClassName = definition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            LimitChecked annotation = aClass.getAnnotation(LimitChecked.class);
            if(annotation == null){
                return null;
            }
            //获取到注解name的值并返回
            return annotation.name();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
