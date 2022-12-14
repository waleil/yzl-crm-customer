package cn.net.yzl.crm.customer.collector.sys;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.GeneralResult;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;


/**
 * @Description 统一的异常处理
 * @Author jingweitao
 * @Date 16:48 2020/12/1
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BizException.class)
    public GeneralResult handleRRException(BizException e) {
        log.error(e.getMessage(), e);
        return GeneralResult.errorWithMessage(e.getCode(), e.getMessage());
    }

//    /**
//     * 方法参数校验
//     * （Bean 校验异常）
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public GeneralResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        log.error(e.getMessage(), e);
//        return GeneralResult.errorWithMessage(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),e.getBindingResult().getFieldError().getDefaultMessage());
//    }


    /**
     * Bean 校验异常 Validate
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //400
    @ResponseBody
    public Object methodArgumentValidationHandler(HttpServletRequest request, MethodArgumentNotValidException exception)  {
        BindingResult bindingResult = exception.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(", ");
        }

        return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(), sb.toString());
    }


    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public GeneralResult handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return GeneralResult.errorWithMessage(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     * （方法参数校验异常）如实体类中的@Size注解配置和数据库中该字段的长度不统一等问题
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public GeneralResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return GeneralResult.errorWithMessage(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),e.getMessage());
    }


    /**
     * @Description 500
     * @Author jingweitao
     * @Date 9:02 2020/12/2
     * @Param [e]
     * @return cn.net.yzl.common.entity.GeneralResult
     **/
    @ExceptionHandler(Exception.class)
    public GeneralResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return GeneralResult.errorWithMessage(ResponseCodeEnums.ERROR.getCode(),"内部错误");
    }


}