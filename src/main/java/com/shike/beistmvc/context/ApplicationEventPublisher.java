package com.shike.beistmvc.context;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
