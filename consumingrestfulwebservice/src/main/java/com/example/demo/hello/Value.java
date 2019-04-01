package com.example.demo.hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @ClassName Value
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-04-02 1:39
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {
    private Long id;
    private String quote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Value(Long id, String quote) {
        this.id = id;
        this.quote = quote;
    }

    public Value() {
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
