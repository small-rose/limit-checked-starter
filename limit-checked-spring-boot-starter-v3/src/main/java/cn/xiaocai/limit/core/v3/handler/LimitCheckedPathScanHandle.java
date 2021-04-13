package cn.xiaocai.limit.core.v3.handler;


import cn.xiaocai.limit.core.v3.annotation.LimitChecked;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @description: TODO 功能角色说明： 在这个扫描器中添加我们的过滤条件。
 * TODO 描述： ClassPathBeanDefinitionScanner bean 扫描的类
 * @author: 张小菜
 * @date: 2020/12/4 22:45
 * @version: v1.0
 */
public class LimitCheckedPathScanHandle extends ClassPathBeanDefinitionScanner {

    public LimitCheckedPathScanHandle(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        //添加过滤条件，这里是只添加了@LimitChecked的注解才会被扫描到
        addIncludeFilter(new AnnotationTypeFilter(LimitChecked.class));
        //调用spring的扫描
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if(beanDefinitionHolders.size() != 0){
            //给扫描出来的接口添加上代理对象
            processBeanDefinitions(beanDefinitionHolders);
        }
        return beanDefinitionHolders;
    }

    /**
     * 给扫描出来的接口添加上代理对象
     * @param beanDefinitions
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            //拿到接口的全路径名称
            String beanClassName = definition.getBeanClassName();
            //把接口的全路径放入ProxyFactoryBean 的构造器中,在构造器中会自动转成 class类型
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            //把扫描出来的接口里面改成一个 生成代理类的工程方法,这个类实现了 factoryBean spring容器在实例化的时候会调用 里面的getObject 方法
            definition.setBeanClass(ProxyFactoryBean.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
