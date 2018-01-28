package com.jiang.im.vo;
/**
 * http请求返回给最外层对象
 * @author jiangqianghua
 *
 */
public class ResultVo<T> {

	private Integer code ; 
	
	private String msg ; 
	
	private T data ;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
	
}
