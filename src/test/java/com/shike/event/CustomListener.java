package com.shike.event;

import com.shike.context.ApplicationListener;

public class CustomListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println(event);
    }
}
