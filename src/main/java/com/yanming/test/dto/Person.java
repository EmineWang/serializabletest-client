package com.yanming.test.dto;

/**
 * @author yanming
 * @version 1.0.0
 * @description
 * @date 2018/04/17 15:13
 **/
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class Person implements Serializable {

    //通过变更serialVersionUID，分别测试与serializabletest-server中相同和不同的情况
    private static final long serialVersionUID = 6457272772L;

    private String[] address;

    private String name;

    private int phone;

}
