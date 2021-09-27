package com.sltb.kioskslib.library.model;
import com.sltb.kioskslib.library.auth.models.Status;

public class ActionResponse {
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Status status;
    private String description;

    public ActionResponse(Status status, String description){
        this.status = status;
        this.description = description;
    }
}
