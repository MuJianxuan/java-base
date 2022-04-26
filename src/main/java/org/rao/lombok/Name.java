package org.rao.lombok;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
// 感觉会导致 某些解析存在问题
//@Accessors(chain = true)
@Data
public class Name {
    private String of;

    private String value;
}
