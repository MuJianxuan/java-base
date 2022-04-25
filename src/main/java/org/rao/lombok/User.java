package org.rao.lombok;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
//@Data
public class User implements Serializable {


    private String name;


}
