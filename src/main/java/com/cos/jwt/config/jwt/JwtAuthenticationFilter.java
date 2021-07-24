package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		
		try {
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			//토큰만들기
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
			
			//로그인시도
			//pricipaldetailsservice의 loadbyusername 함수가 실행됨
			//authenticationManager에 토큰을 넣어 던지면 인증이 되어 authentication으로 받아짐
			//authentication에 로그인한 정보가 담긴다.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			
			//꺼내보기
			//authentication객체가 세션에 저장됨 ->로그인이 되었다는 뜻
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getUsername());
			System.out.println("====================================");
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("====================================");
		
		
		return null;
	}
}
