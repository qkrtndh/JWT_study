package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

//시큐리티가 필터를 가지고 있는데 그 필터 중 BasicAuthenticationFilter가 있다.
//권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 거치게 되어있다.
//권한이나 인증이 필요한 주소가 아니면 이 필터를 거치지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	// 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 거치게 됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//super.doFilterInternal(request, response, chain);
		System.out.println("권한이 필요한 주소가 요청됨");

		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader: " + jwtHeader);

		// JWT 토큰을 검증해서 정상적인 사용인지 확인-header가 있는지 확인
		if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		// JWT 토큰을 검증해서 정상적인 사용인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

		// 서명하여 username 가져옴
		String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username")
				.asString();

		// 서명이 정상적으로 되었다면.
		if (username != null) {
			
			User userEntity = userRepository.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			
			//JWT 토큰 서명을 통해 서명이 정상이면 authentication 객체를 만든다. 인증이 된 상태이므로 비밀번호는 필요하지않다.
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,	 null,principalDetails.getAuthorities());
			
			//강제로 시큐리티의 세션에 접근하여 Authetication 객체를 저장 
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		}
	}

}