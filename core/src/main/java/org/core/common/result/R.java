package org.core.common.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.core.common.exception.BaseErrorInfoInterface;

@Setter
@Getter
@ToString
public class R {
    /**
     * 响应代码
     */
    private String code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应结果
     */
    private Object data;
    
    public R() {
    }
    
    public R(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getCode();
        this.message = errorInfo.getResultMsg();
    }
    
    /**
     * 成功
     */
    public static R success() {
        return success(null);
    }
    
    /**
     * 成功
     */
    public static R success(Object data) {
        R rb = new R();
        rb.setCode(ResultCode.SUCCESS.getCode());
        rb.setMessage(ResultCode.SUCCESS.getResultMsg());
        rb.setData(data);
        return rb;
    }
    
    /**
     * 失败
     */
    public static R error(BaseErrorInfoInterface errorInfo) {
        R rb = new R();
        rb.setCode(errorInfo.getCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setData(null);
        return rb;
    }
    
    /**
     * 失败
     */
    public static R error(String code, String message) {
        R rb = new R();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setData(null);
        return rb;
    }
    
    /**
     * 失败
     */
    public static R error(String message) {
        R rb = new R();
        rb.setCode("-1");
        rb.setMessage(message);
        rb.setData(null);
        return rb;
    }
}
