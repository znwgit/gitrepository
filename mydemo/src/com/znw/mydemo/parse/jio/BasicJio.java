package com.znw.mydemo.parse.jio;

import java.io.Serializable;

public abstract class BasicJio implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
	private String data;
	private String total;
	private String totalPage;
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public String getTotal()
	{
		return total;
	}
	public void setTotal(String total)
	{
		this.total = total;
	}
	public String getTotalPage()
	{
		return totalPage;
	}
	public void setTotalPage(String totalPage)
	{
		this.totalPage = totalPage;
	}
	@Override
	public String toString()
	{
		return "BasicJio [code=" + code + ", message=" + message + ", data=" + data + ", total=" + total + ", totalPage=" + totalPage + "]";
	}
	
	
}
