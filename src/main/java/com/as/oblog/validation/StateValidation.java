package com.as.oblog.validation;

import com.as.oblog.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/**
 * @author 12aaa-zone
 */
public class StateValidation implements ConstraintValidator<State,String> {

    // 满足条件的判定词集合
    private static final Set<String> VALID_STATUS =
            Set.of("published",
                    "draft",
                    "hidden",
                    "已发布",
                    "草稿",
                    "隐藏");

    /**
     *
     * @param value          //将来要校验的数据
     * @param context       //数据上下文，暂时没用到
     * @return              //返回boolean类型
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        //校验规则
        return VALID_STATUS.contains(value);
    }
}
