package com.example.demo.hello;

import lombok.*;

/**
 * @ClassName Customer
 * @Description TODO
 * @Author wonderQin
 * @Date 2019-04-02 23:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private long id;
    private String firstName,lastName;
}
