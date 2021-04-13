package cn.xiaocai.limit.core.v3;


import cn.xiaocai.limit.core.v3.annotation.LimitCheckedScans;
import cn.xiaocai.limit.core.v3.handler.LimitCheckedPathScanHandle;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Set;

/**
 * @description: TODO 功能角色说明：
 * TODO 描述： ImportBeanDefinitionRegistrar 是一个Bean的注册器，ResourceLoaderAware 是标记接口，用于通过ApplicationContext上下文注入ResourceLoader
 * @author: 张小菜
 * @date: 2020/12/4 22:38
 * @version: v1.0
 */
public class LimitCheckedScannerRegistrar implements ImportBeanDefinitionRegistrar , ResourceLoaderAware {

    ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }



    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        //获取所有注解的属性和值
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(LimitCheckedScans.class.getName()));
        //获取到basePackage的值
        String[] basePackages = annoAttrs.getStringArray("basePackage");
        //如果没有设置basePackage 扫描路径,就扫描对应包下面的值
        if(basePackages.length == 0){
            basePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }

        //自定义的包扫描器
        LimitCheckedPathScanHandle scanHandle = new  LimitCheckedPathScanHandle(registry,false);

        if(resourceLoader != null){
            scanHandle.setResourceLoader(resourceLoader);
        }
        //这里实现的是根据名称来注入
        scanHandle.setBeanNameGenerator(new  LimitCheckedBeanGenerator());
        //扫描指定路径下的接口
        Set<BeanDefinitionHolder> beanDefinitionHolders = scanHandle.doScan(basePackages);

    }
}
