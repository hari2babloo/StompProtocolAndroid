package com.androidhari.tambola;

/**
 * Created by b on 17/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class StompResponse {

    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("displayNumber")
    @Expose
    private Boolean displayNumber;
    @SerializedName("validClaim")
    @Expose
    private Boolean validClaim;
    @SerializedName("inValidClaim")
    @Expose
    private Boolean inValidClaim;
    @SerializedName("claimSubmitted")
    @Expose
    private Boolean claimSubmitted;
    @SerializedName("gameCompleted")
    @Expose
    private Boolean gameCompleted;
    @SerializedName("prizeName")
    @Expose
    private Object prizeName;
    @SerializedName("messageList")
    @Expose
    private List<String> messageList = null;
    @SerializedName("message")
    @Expose
    private Object message;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getDisplayNumber() {
        return displayNumber;
    }

    public void setDisplayNumber(Boolean displayNumber) {
        this.displayNumber = displayNumber;
    }

    public Boolean getValidClaim() {
        return validClaim;
    }

    public void setValidClaim(Boolean validClaim) {
        this.validClaim = validClaim;
    }

    public Boolean getInValidClaim() {
        return inValidClaim;
    }

    public void setInValidClaim(Boolean inValidClaim) {
        this.inValidClaim = inValidClaim;
    }

    public Boolean getClaimSubmitted() {
        return claimSubmitted;
    }

    public void setClaimSubmitted(Boolean claimSubmitted) {
        this.claimSubmitted = claimSubmitted;
    }

    public Boolean getGameCompleted() {
        return gameCompleted;
    }

    public void setGameCompleted(Boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    public Object getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(Object prizeName) {
        this.prizeName = prizeName;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

}