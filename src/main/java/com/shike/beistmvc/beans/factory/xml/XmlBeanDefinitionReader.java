package com.shike.beistmvc.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.shike.beans.BeansException;
import com.shike.beans.PropertyValue;
import com.shike.beans.factory.config.BeanDefinition;
import com.shike.beans.factory.config.BeanReference;
import com.shike.beans.factory.support.AbstractBeanDefinitionReader;
import com.shike.beans.factory.support.BeanDefinitionRegistry;
import com.shike.context.annotation.ClassPathBeanDefinitionScanner;
import com.shike.core.io.Resource;
import com.shike.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream is = resource.getInputStream()) {
                doLoadBeanDefinitions(is);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    public void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);


        Element root = doc.getDocumentElement();
        NodeList componentScanList = root.getElementsByTagName("component-scan");
        if (componentScanList.getLength() != 0){
            Element componentScan = (Element) componentScanList.item(0);

            String basePackage = componentScan.getAttribute("base-package");

            if (StrUtil.isEmpty(basePackage)){
                throw new BeansException("The value of basepackage attribute can not be empty or null");
            }
            scanPackage(basePackage);
        }

        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {

            if (!(childNodes.item(i) instanceof Element)) continue;


            if (!"bean".equals(childNodes.item(i).getNodeName())) continue;


            Element bean = (Element) childNodes.item(i);

            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            String initMethod = bean.getAttribute("init-method");
            String destroyMethod = bean.getAttribute("destroy-method");
            String scope = bean.getAttribute("scope");

            Class<?> clazz = Class.forName(className);
            String beanName = StrUtil.isNotEmpty(id) ? id : name;

            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "]");
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethod);

            if (StrUtil.isNotEmpty(scope)){
                beanDefinition.setScope(scope);
            }

            NodeList nodes = bean.getChildNodes();

            for (int j = 0; j < nodes.getLength(); j++) {
                if (!(nodes.item(j) instanceof Element)) continue;

                if (!"property".equals(nodes.item(j).getNodeName())) continue;
//                System.out.println(nodes.item(i));
                Element property = (Element) nodes.item(j);

                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");

                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;

                PropertyValue propertyValue = new PropertyValue(attrName, value);

                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }

            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }

    private void scanPackage(String scanPath) {
        String[] basePackages = StrUtil.splitToArray(scanPath, ',');
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner
                (getRegistry());
        scanner.doScan(basePackages);
    }

}
