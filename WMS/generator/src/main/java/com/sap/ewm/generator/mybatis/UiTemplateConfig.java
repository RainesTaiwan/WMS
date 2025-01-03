package com.sap.ewm.generator.mybatis;

/**
 * @author Ervin Chen
 * @date 2020/7/9 10:52
 */
public class UiTemplateConfig {
    private String view = "/templates/ui.view.xml";
    private String controller = "/templates/ui.controller.js";

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    @Override
    public String toString() {
        return "UiTemplateConfig{" +
                "view='" + view + '\'' +
                ", controller='" + controller + '\'' +
                '}';
    }
}
