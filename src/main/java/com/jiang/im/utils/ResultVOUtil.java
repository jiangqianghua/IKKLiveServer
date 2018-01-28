package com.jiang.im.utils;

import com.jiang.im.enums.ResultEnum;
import com.jiang.im.vo.ResultVo ;

public class ResultVOUtil {

	public static ResultVo success(Object data){
		ResultVo resultVo = new ResultVo();
		resultVo.setCode(0);
		resultVo.setMsg("success");
		resultVo.setData(data);
		return resultVo ;
	}
	
	
	public static ResultVo success(){
		return success(null);
	}

	
	public static ResultVo error(Integer code , String msg){
		ResultVo resultVo = new ResultVo();
		resultVo.setCode(code);
		resultVo.setMsg(msg);
		resultVo.setData(null);
		return resultVo ;
	}

	public static  ResultVo error(ResultEnum resultEnum){
		ResultVo resultVo = new ResultVo();
		resultVo.setCode(resultEnum.getCode());
		resultVo.setMsg(resultEnum.getMessage());
		resultVo.setData(null);
		return resultVo ;
	}
}
