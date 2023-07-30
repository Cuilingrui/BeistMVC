package com.shike.event;

import com.shike.context.ApplicationEvent;
import com.shike.context.event.ApplicationContextEvent;

public class CustomEvent extends ApplicationContextEvent {

    private String msg;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CustomEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CustomEvent{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
