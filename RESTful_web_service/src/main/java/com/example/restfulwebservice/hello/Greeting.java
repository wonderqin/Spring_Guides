package com.example.restfulwebservice.hello;

/**
 * @ClassName Hello
 * @Description pojo
 * @Author wonderQin
 * @Date 2019-03-20 1:41
 **/
public class Greeting {
    private long id;
    private String content;

    public Greeting() {
    }

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
