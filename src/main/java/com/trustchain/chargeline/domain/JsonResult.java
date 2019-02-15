package com.trustchain.chargeline.domain;

public class JsonResult<T> {
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    private int state = 0;
    private String message;
    private T data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "JsonResult [state=" + this.state + ", message=" + this.message + ", date=" + this.data + "]";
    }
}
