package com.lzdn.note.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.lzdn.note.util.ip.IPSeeker;

@Order(1)
@WebFilter(filterName = "blogFilter", urlPatterns = { "/", "/blog", "/blog/info/*", "/show/*" })
public class BlogFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 获得用户请求的URI
        String path = httpRequest.getRequestURI();
        
        //获得ip
        IPSeeker seeker = IPSeeker.getInstance();
		String ipAddress = IPSeeker.getIp(httpRequest);
        String country = seeker.getCountry(ipAddress);
        
        String province = country.split("省")[0].replace("市", "");
        
        logger.info("请求链接:" + path + ",请求人地址:" + country + ",请求人IP:" + ipAddress);
        
		//showService.updateCityCount(province);
        
        chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
