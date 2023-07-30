package com.shike.beistmvc.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.shike.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.shike.beans.factory.config.BeanDefinition;
import com.shike.beans.factory.support.BeanDefinitionRegistry;
import com.shike.stereotype.Component;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{
    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages){
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            candidates.forEach(beanDefinition -> {
                String scope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(scope)){
                    beanDefinition.setScope(scope);
                }

                registry.registerBeanDefinition(determineBeanName(beanDefinition),
                        beanDefinition);
            });
        }
        registry.registerBeanDefinition("com.shike.context.annotation.internalAutowiredAnnotationProcessor",
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    public String resolveBeanScope(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();

        Scope scope = beanClass.getAnnotation(Scope.class);

        return scope == null || StrUtil.isEmpty(scope.value()) ? StrUtil.EMPTY : scope.value();
    }

    public String determineBeanName(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);

        return StrUtil.isEmpty(component.value()) ? StrUtil.lowerFirst(beanClass.getSimpleName()) : component.value();
    }
}
