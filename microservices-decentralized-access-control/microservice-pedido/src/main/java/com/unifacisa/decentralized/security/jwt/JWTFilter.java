package com.unifacisa.decentralized.security.jwt;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JWTFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        else{
            //pega o login e senha do usuário passados no http header
            String username = request.getHeader("Username");
            String password = request.getHeader("Password");

            if(StringUtils.hasText(username) && StringUtils.hasText(password)){

                String url = "http://localhost:8082/api/authenticate?username="+username+"&password="+password;

                try{
                    HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

                    RestTemplate restTemplate = new RestTemplate();
                    String jwt = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

                    if (StringUtils.hasText(jwt.toString()) && this.tokenProvider.validateToken(jwt.toString())) {
                        Authentication authentication = this.tokenProvider.getAuthentication(jwt.toString());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                    return jwt;
                }catch(Exception ex){
                    System.out.println(ex.getStackTrace());
                }
            }
        }
        return null;
    }
}
