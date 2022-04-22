package org.rao.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Rao
 * @Date 2021/11/20
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class A implements Ai {
//    private static final long serialVersionUID = 3624988207631812625L;

    /**
     * 名称
     */
    private String name;

}
