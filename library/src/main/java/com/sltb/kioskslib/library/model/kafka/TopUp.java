package com.sltb.kioskslib.library.model.kafka;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "accountid",
        "amount",
        "reply"
})

public class TopUp {
    @JsonProperty("accountid")
    private Long accountId;
    private float amount;
    private String reply;
    private Date time;
    @JsonIgnore
    private Map<String, Object> additionalProperties= new HashMap<>();

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
    public String getReply() {
        return reply;
    }


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties(){
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String key, Object value){
        this.additionalProperties.put(key, value);
    }
}
