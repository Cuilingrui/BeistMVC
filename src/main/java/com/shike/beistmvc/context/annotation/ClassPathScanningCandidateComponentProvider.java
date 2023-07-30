package com.shike.beistmvc.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.shike.beans.factory.config.BeanDefinition;
import com.shike.stereotype.Component;
import com.shike.webmvc.annotation.Controller;
import sun.reflect.annotation.AnnotationType;


import java.lang.annotation.Annotation;
import java.util.*;

public class ClassPathScanningCandidateComponentProvider {

    private final List<Class<? extends Annotation>> includes = new LinkedList<>();

    ClassPathScanningCandidateComponentProvider(){
        includes.add(Component.class);
        includes.add(Controller.class);
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage){

        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> clazzes = new HashSet<>();
        for (Class<? extends Annotation> annotation : includes) {
            clazzes.addAll(ClassUtil.scanPackageByAnnotation(basePackage, annotation));
        }

        clazzes.forEach(clazz -> candidates.add(new BeanDefinition(clazz)));

        return candidates;
    }

    public void addIncludeFilter(Class<? extends Annotation> annotation) {
        this.includes.add(annotation);
    }

}
