package com.example.demo.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @ClassName Quote
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-04-02 1:28
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private String type;
    private Value value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Quote() {
    }

    public Quote(String type, Value value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
