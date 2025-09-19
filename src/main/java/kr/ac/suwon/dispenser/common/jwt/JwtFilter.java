package kr.ac.suwon.dispenser.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(JwtConstants.AUTH_HEADER);
        log.info("[JWT FILTER] URI=[{}], HEADER =[{}]", request.getRequestURI(), header);

        String token = resolveToken(header);
        log.info("[JWT FILTER] TOKEN=[{}]", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            SecurityContextHolder.getContext()
                    .setAuthentication(jwtTokenProvider.getAuthentication(token));
            log.info("[JWT FILTER] AUTHENTICATION SUCCESS URI=[{}]", request.getRequestURI());
        } else {
            SecurityContextHolder.clearContext();
            log.info("[JWT FILTER] ANONYMOUS URI=[{}]", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String header) {
        if (header != null && header.startsWith(JwtConstants.TOKEN_TYPE + " ")) {
            return header.substring((JwtConstants.TOKEN_TYPE + " ").length());
        }
        return null;
    }
}