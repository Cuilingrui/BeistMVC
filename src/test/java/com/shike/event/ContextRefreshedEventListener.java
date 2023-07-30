package com.shike.event;

import com.shike.context.ApplicationListener;
import com.shike.context.event.ContextRefreshedEvent;
import com.shike.stereotype.Component;

@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("context refresh");
    }
}
