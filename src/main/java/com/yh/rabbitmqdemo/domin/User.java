package com.yh.rabbitmqdemo.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : yh
 * @date : 2021/9/9 21:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 3598933662381154926L;

    private Long id;

    private String name;

    private String remark;
}
