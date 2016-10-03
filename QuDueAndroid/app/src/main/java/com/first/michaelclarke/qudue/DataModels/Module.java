package com.first.michaelclarke.qudue.DataModels;

/**
 * Created by michaelclarke on 30/08/2016.
 */
public class Module {

    private String moduleId;

    private String moduleTitle;

    // default constructor
    public Module() {

    }

    // constructor with args
    public Module(String moduleId, String moduleTitle) {
        this.moduleId = moduleId;
        this.moduleTitle = moduleTitle;
    }


    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }
}
