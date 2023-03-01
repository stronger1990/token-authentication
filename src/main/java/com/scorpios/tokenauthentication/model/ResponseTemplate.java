package com.scorpios.tokenauthentication.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Builder 和 @Data 都是lombok的注解，@Data是节省getter setter的写法，却能用getter setter调用。
 * @Builder 能将该model以建造者形式使用，链式调用赋值，会自动生成构造者。
 */
@Builder
@Data
public class ResponseTemplate {

    public Integer code;

    public String message;

    public Object data;

}
