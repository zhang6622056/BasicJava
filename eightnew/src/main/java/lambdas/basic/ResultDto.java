/**
 * 
 */
package lambdas.basic;

import java.io.Serializable;

/**
 * 
 * <P>File name : ResultDto.java </P>
 * <P>Author : zhouchun </P> 
 * <P>Date : 2013-5-5 </P>
 */
public class ResultDto<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long code;

	private String errMsg;
	
	private T object;

	public ResultDto() {
		super();
	}

	public ResultDto(long code, String errMsg) {
		this.code = code;
		this.errMsg = errMsg;
	}

	public ResultDto(long code, String errMsg, T object) {
		this.code = code;
		this.errMsg = errMsg;
		this.object = object;
	}

	/**
	 * 获取code 
	 * @return code long
	 */
	public long getCode() {
		return code;
	}

	/** 
	 * 设置code 
	 * @param code long 
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/** 
	 * 获取errMsg 
	 * @return errMsg String
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/** 
	 * 设置errMsg 
	 * @param errMsg String 
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/** 
	 * 获取object 
	 * @return object T
	 */
	public T getObject() {
		return object;
	}

	/** 
	 * 设置object 
	 * @param object T 
	 */
	public void setObject(T object) {
		this.object = object;
	}

	/**
	 * <P>Author : zhouchun </P>      
	 * <P>Date : 2013-5-5 </P>
	 * @return
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultDto [code=" + code + ", errMsg=" + errMsg + ", object="
				+ object + "]";
	}
}
