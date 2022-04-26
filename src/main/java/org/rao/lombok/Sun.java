package org.rao.lombok;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/26
 **/
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class Sun extends User implements Serializable {
    private int age;
}
