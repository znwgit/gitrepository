package com.znw.mydemo.net.httpclient.impl;

public class HttpUrl
{
	public String scheme;
	public String authority;
	public String path;
	public String query;
	public String fragment;
	public String host;
	public int port = -1;
	public String machine;
	public String domain;

	public HttpUrl(String url)
	{
		int afterScheme = 0;
		int length;
		int endOfScheme;

		if (url == null)
		{
			return;
		}

		length = url.length();
		if (length == 0)
		{
			return;
		}

		endOfScheme = url.indexOf(':');
		if (endOfScheme != -1)
		{
			if (endOfScheme == length - 1)
			{
				scheme = url.substring(0, endOfScheme);
				return;
			}

			if (endOfScheme < length - 2 && url.charAt(endOfScheme + 1) == '/' && url.charAt(endOfScheme + 2) == '/')
			{
				scheme = url.substring(0, endOfScheme);
				afterScheme = endOfScheme + 1;
			}
		}

		parseAfterScheme(url, afterScheme, length);
	}

	public HttpUrl(String theScheme, String partialUrl)
	{
		int length;

		scheme = theScheme;

		if (partialUrl == null)
		{
			return;
		}

		length = partialUrl.length();
		if (length == 0)
		{
			return;
		}

		parseAfterScheme(partialUrl, 0, length);
	}

	private void parseAfterScheme(String url, int afterScheme, int length)
	{
		int start;
		int startOfAuthority;
		int endOfUrl;
		int endOfAuthority;
		int endOfPath;
		int endOfQuery;
		int endOfHost;
		int startOfPort;
		int endOfPort;
		int startOfDomain;

		if (url.indexOf(' ') != -1)
		{
			throw new IllegalArgumentException("Space character in URL");
		}

		endOfUrl = length;
		endOfAuthority = endOfUrl;
		endOfPath = endOfUrl;
		endOfQuery = endOfUrl;

		if (url.startsWith("//", afterScheme))
		{
			startOfAuthority = afterScheme + 2;
		}
		else
		{
			startOfAuthority = afterScheme;
		}

		start = url.indexOf('#', startOfAuthority);
		if (start != -1)
		{
			endOfAuthority = start;
			endOfPath = start;
			endOfQuery = start;

			start++;

			if (start < endOfUrl)
			{
				fragment = url.substring(start, endOfUrl);
			}
		}

		start = url.indexOf('?', startOfAuthority);
		if (start != -1 && start < endOfQuery)
		{
			endOfAuthority = start;
			endOfPath = start;

			start++;

			if (start < endOfQuery)
			{
				query = url.substring(start, endOfQuery);
			}
		}

		if (startOfAuthority == afterScheme)
		{
			start = afterScheme;
		}
		else
		{
			start = url.indexOf('/', startOfAuthority);
		}

		if (start != -1 && start < endOfPath)
		{
			endOfAuthority = start;

			path = url.substring(start, endOfPath);
		}

		if (startOfAuthority >= endOfAuthority)
		{
			return;
		}

		authority = url.substring(startOfAuthority, endOfAuthority);
		endOfPort = authority.length();

		start = authority.indexOf(']');
		if (start == -1)
		{
			startOfPort = authority.indexOf(':');
		}
		else
		{
			startOfPort = authority.indexOf(':', start);
		}

		if (startOfPort != -1)
		{
			endOfHost = startOfPort;

			startOfPort++;

			if (startOfPort < endOfPort)
			{
				try
				{
					port = Integer.parseInt(authority.substring(startOfPort, endOfPort));

					if (port <= 0)
					{
						throw new IllegalArgumentException("invalid port format");
					}

					if (port == 0 || port > 0xFFFF)
					{
						throw new IllegalArgumentException("port out of legal range");
					}
				}
				catch (NumberFormatException nfe)
				{
					throw new IllegalArgumentException("invalid port format");
				}
			}
		}
		else
		{
			endOfHost = endOfPort;
		}

		if (endOfHost < 1)
		{
			return;
		}

		host = authority.substring(0, endOfHost);

		if (Character.isDigit(host.charAt(0)) || host.charAt(0) == '[')
		{
			return;
		}

		startOfDomain = host.indexOf('.');
		if (startOfDomain != -1)
		{
			domain = host.substring(startOfDomain + 1, host.length());
			machine = host.substring(0, startOfDomain);
		}
		else
		{
			machine = host;
		}
	}

	public String toString()
	{
		StringBuffer url = new StringBuffer();

		if (scheme != null)
		{
			url.append(scheme);
			url.append(':');
		}

		if (authority != null)
		{
			url.append('/');
			url.append('/');
			url.append(authority);
		}

		if (path != null)
		{
			url.append(path);
		}

		if (query != null)
		{
			url.append('?');
			url.append(query);
		}

		if (fragment != null)
		{
			url.append('#');
			url.append(fragment);
		}

		return url.toString();
	}
}