package com.shike.beistmvc.webmvc;


import cn.hutool.http.HttpStatus;

public class ModelAndView {
    private Object view;
    private Model model = new ExtendedModelMap();
    private HttpStatus status;

    public void setView(Object view) {
        this.view = view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Object getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

}
