package com.shike.beistmvc.context;

import com.shike.beans.factory.HierarchicalBeanFactory;
import com.shike.beans.factory.ListableBeanFactory;
import com.shike.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {

}
