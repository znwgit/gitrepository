package com.znw.mydemo.net.httpclient.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.znw.mydemo.net.constants.HttpConstants;
import com.znw.mydemo.net.httpclient.INet;
import com.znw.mydemo.utils.debug.DebugUtils;
import com.znw.mydemo.utils.debug.LogUtils;
import com.znw.mydemo.utils.sp.SharedPrefHelper;





abstract class NetAdapter implements INet, HttpConstants
{
	protected StringBuffer getBaseUri()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(BASE_URL);
		return sb;
	}

	private String netPost(HttpUrl httpUrl, List<NameValuePair> nameValuePairs, int timeout)
	{
		String rtn = null;
		String URI = httpUrl.toString();
		DebugUtils.print("URL==" + URI);
		DebugUtils.print("NameValuePair==" + nameValuePairs);
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout * 1000);
		HttpConnectionParams.setSoTimeout(params, timeout * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		HttpClientParams.setRedirecting(params, true);
		DefaultHttpClient httpclient = new DefaultHttpClient(params);
		HttpPost request = new HttpPost(URI);
		try
		{	//往请求头中写
			if (null != SharedPrefHelper.getInstance().getCookie())
			{
				request.setHeader("Cookie", SharedPrefHelper.getInstance().getCookie());
				LogUtils.info(this.getClass(),"Set>>>Cookie=" +SharedPrefHelper.getInstance().getCookie());
			}
			if (nameValuePairs != null)
			{
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			}

			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				request.abort();
			}
			//存本地
			Header header = response.getFirstHeader("Set-Cookie");
			if (header != null)
			{
				SharedPrefHelper.getInstance().setCookie(header.getValue());
				//SoftApplication.cookie = header.getValue();
				LogUtils.info(this.getClass(),"Get<<<Cookie=" + SharedPrefHelper.getInstance().getCookie());
			}

			if (response.getStatusLine().getStatusCode() == 200)
			{
				rtn = EntityUtils.toString(response.getEntity());
			}
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (java.net.SocketException s)
		{
			s.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return rtn;
	}

	protected String netPost(HttpUrl httpUrl, List<NameValuePair> nameValuePairs)
	{
		return netPost(httpUrl, nameValuePairs, 30);
	}

	private String netGet(HttpUrl httpUrl, int timeout)
	{
		String rtn = null;
		String URI = httpUrl.toString();
		DebugUtils.print("URL==" + URI);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(URI);

		try
		{
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				rtn = EntityUtils.toString(httpResponse.getEntity());
			}
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (java.net.SocketException s)
		{
			s.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return rtn;
	}

	protected String netGet(HttpUrl httpUrl)
	{
		return netGet(httpUrl, 30);
	}

	private String netPostFile(HttpUrl httpUrl, List<NameValuePair> nameValuePairs, String paramName,File file, int timeout)
	{
		String rtn = null;
		String URI = httpUrl.toString();
		DebugUtils.print("URL==", URI);
		DebugUtils.print("NameValuePair==" + nameValuePairs);
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout * 1000);
		HttpConnectionParams.setSoTimeout(params, timeout * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		HttpClientParams.setRedirecting(params, true);
		DefaultHttpClient httpclient = new DefaultHttpClient(params);
		HttpPost request = new HttpPost(URI);
		try
		{
			
			if (null != SharedPrefHelper.getInstance().getCookie())
			{
				request.setHeader("Cookie", SharedPrefHelper.getInstance().getCookie());
				LogUtils.info(this.getClass(),"Set>>>Cookie=" + SharedPrefHelper.getInstance().getCookie());
			}
			
	
			// File file = new File(uploadFile); // DEBUG
			MultipartEntity mpEntity = new MultipartEntity();

			mpEntity.addPart(paramName, new FileBody(file));// 上传文件
			for (NameValuePair nvp : nameValuePairs)
			{
				mpEntity.addPart(nvp.getName(), new StringBody(nvp.getValue()));
			}
			//mpEntity.addPart("api_key", new StringBody("circle_1120"));
			mpEntity.toString();
			request.setEntity(mpEntity); // DEBUG
			
//			if (nameValuePairs != null)
//			{
//				request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			}
			
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				request.abort();
			}

			Header header = response.getFirstHeader("Set-Cookie");
			if (header != null)
			{
				SharedPrefHelper.getInstance().setCookie(header.getValue());
				//SoftApplication.cookie = header.getValue();
				LogUtils.info(this.getClass(),"Get<<<Cookie=" + SharedPrefHelper.getInstance().getCookie());
			}
			
			if (response.getStatusLine().getStatusCode() == 200)
			{
				rtn = EntityUtils.toString(response.getEntity());
			}
		
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (java.net.SocketException s)
		{
			s.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return rtn;
	}

	protected String netPostFile(HttpUrl httpUrl, List<NameValuePair> nameValuePairs, String paramName,File file)
	{
		return netPostFile(httpUrl, nameValuePairs,paramName, file, 30);
	}
}
