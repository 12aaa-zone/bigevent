package com.as.oblog.anno;

import com.as.oblog.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 12aaa-zone
 */

// 自定义注解state

@Documented         //元注解：让本自定义注解会被包含在JavaDoc生成的文档中
@Target({FIELD})    //元注解：限制注解的适用范围，这里的 ElementType.FIELD 表示该注解只能用于类的字段上
@Retention(RUNTIME)  //元注解：指定本自定义注解的保留策略，这里让本注解保留至运行期
@Constraint(validatedBy = {StateValidation.class})  //元注解:指定提供校验规则的类

public @interface State {

    //错误抛出信息
    String message() default "Invalid state value: must be one of the specified states";


    // 指定分组
    Class<?>[] groups() default {};

    // 负载 获取本自定义注解的附加信息
    Class<? extends Payload>[] payload() default {};
}
