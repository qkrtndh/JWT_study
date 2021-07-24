package com.cos.jwt.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

//스프링 시큐리티에서 usernamepasswordauthenticationFilter 가 있음
// /login 요청시 post 방식으로 username, password 전송시, UsernamePasswordAtuthenticationFilter 동작
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
	// /login 요청시 로그인 시도를 위해 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("로그인 시도중");
		//username, password 받아서 정상이면 로그인 시도, authenticationManager로 로그인 시도하면 principaldetailssservice 가 호출됨 
		//loadUserByUsername이 자동으로 실행됨
		//Principal details를 세션에 담고 jwt토큰을 만들어서 응답한다.
		//세션에 담지 않으면 권한관리가 안되서 세션에 담아야함.
		return super.attemptAuthentication(request, response);
	}
}
