package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// 토큰이 들어오면(현재는 post 요청시)
		//id,pw정상적으로 들어와서 로그인이 완료되면 토큰을 만들어 응답.
		//요청시마다 header에 Authorization에 value값으로 토큰을 가져오면
		//서버에서 만든 토큰이 맞는지 검증.
		if (req.getMethod().equals("POST")) {
			System.out.println("POST 요청됨");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			System.out.println("필터3");
			// 코스 토큰일 때만 체인을 탄다
			if (headerAuth.equals("cos")) {
				chain.doFilter(request, response);
			} else {
				PrintWriter out = res.getWriter();
				out.println("인증 안됨");
			}
		}

	}

}
