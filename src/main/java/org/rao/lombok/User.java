package org.rao.lombok;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
@SuperBuilder
public class User implements Serializable {

    @Builder.Default
    private String name = "0";
}
