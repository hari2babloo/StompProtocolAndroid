package ua.naiksoftware.tambola;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naik on 24.02.17.
 */
public class EchoModel {
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
    private Object messageList;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("completedNumbers")
    @Expose
    private List<Integer> completedNumbers = null;

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

    public Object getMessageList() {
        return messageList;
    }

    public void setMessageList(Object messageList) {
        this.messageList = messageList;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<Integer> getCompletedNumbers() {
        return completedNumbers;
    }

    public void setCompletedNumbers(List<Integer> completedNumbers) {
        this.completedNumbers = completedNumbers;
    }


}
