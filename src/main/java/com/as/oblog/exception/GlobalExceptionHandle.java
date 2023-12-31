package com.as.oblog.exception;

import com.as.oblog.pojo.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;
import org.springframework.validation.ObjectError;
/**
 * @author 12aaa-zone
 */

@RestControllerAdvice
public class GlobalExceptionHandle {

    // 全局错误
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        // 构造错误消息
        Result errorResult = Result.error("错误: " + (StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "暂无错误信息，请联系管理员"));

        // 返回500状态码
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //验证失败错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        // 构造错误消息
        Result errorResult = Result.error("错误:数据验证异常:"+ errorMessage);
        // 返回400状态码
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // 数据库制约规则错误
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<Result> handleDataIntegrityViolation(DataIntegrityViolationException e) {

        // 提取异常中的自定义消息，或者返回通用错误消息
        Result errorResult = Result.error("错误:违反数据库制约规则");
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

}
