package com.bbs;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.enums.CodeEnum;
import com.bbs.exception.BusinessException;
import com.bbs.util.PageUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> {

    @ApiModelProperty("编码")
    private Integer code;

    @ApiModelProperty("数据")
    private T data;

    @ApiModelProperty("消息")
    private String msg;

    public static Result<Boolean> success() {
        return success(200, true, "success");
    }

    public static <T> Result<T> successNull() {
        return success(200, null, "success");
    }

    public static <T> Result<T> success(T data) {
        return success(200, data, "success");
    }


    public static <T> Result<T> success(CodeEnum codeEnum, T data) {
        return success(codeEnum.getCode(), data, codeEnum.getMsg());
    }

    public static <T> Result<T> success(Integer code, String msg) {
        return new Result<>(code, null, msg);
    }

    public static <T> Result<T> success(Integer code, T data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static Result<Boolean> failed() {
        return success(500, false, "failed");
    }

    public static <T> Result<T> failedNull() {
        return success(500, null, "failed");
    }

    public static <T> Result<T> failed(String msg) {
        return success(500, null, msg);
    }

    public static <T> Result<T> failed(Integer code, String msg) {
        return success(code, null, msg);
    }

    public static <T> Result<T> failed(Integer code, T data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static <T> Result<T> failed(CodeEnum codeEnum) {
        return failed(codeEnum.getCode(), null, codeEnum.getMsg());
    }
    public static <T> Result<T> failed(CodeEnum codeEnum, T data) {
        return failed(codeEnum.getCode(), data, codeEnum.getMsg());
    }

    public static <T> Result<T> failed(BusinessException exception) {
        return failed(exception.getCode(), null, exception.getMsg());
    }

    public static Result<Boolean> of(Boolean result) {
        return result ? success() : failed();
    }
}
