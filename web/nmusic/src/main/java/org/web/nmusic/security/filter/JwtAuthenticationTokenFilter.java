package org.web.nmusic.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.CookieConstant;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.RoleUtil;
import org.core.utils.UserUtil;
import org.core.utils.token.TokenUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.web.nmusic.security.config.NeteaseCloudMusicPermitAllUrlProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * token过滤器 验证token有效性，然后注入到线程变量中
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    private final NeteaseCloudMusicPermitAllUrlProperties permitAllUrlProperties;
    
    private final TokenUtil tokenUtil;
    
    public JwtAuthenticationTokenFilter(NeteaseCloudMusicPermitAllUrlProperties permitAllUrlProperties, TokenUtil tokenUtil) {
        this.permitAllUrlProperties = permitAllUrlProperties;
        this.tokenUtil = tokenUtil;
    }
    
    
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 放行运行匿名访问的地址
        // 注意: 如果匿名访问地址中的接口中，获取用户属性则会报错
        if (permitAllUrlProperties.getUrls().contains(request.getRequestURI())
                || PatternMatchUtils.simpleMatch(permitAllUrlProperties.getUrls().toArray(String[]::new), request.getRequestURI())
        ) {
            filterChain.doFilter(request, response);
        } else {
            // 获取cookie
            Optional<String> first = Arrays.stream(request.getCookies() == null ? new Cookie[]{} : request.getCookies())
                                           .filter(cookie -> StringUtils.equalsIgnoreCase(CookieConstant.COOKIE_NAME_MUSIC_U, cookie.getName()))
                                           .map(Cookie::getValue)
                                           .filter(StringUtils::isNotBlank)
                                           .findFirst();
            // cookie中没有则从其他参数中获取
            String token = null;
            if (first.isPresent()) {
                token = first.get();
            } else {
                try {
                    // get parameters
                    String cookie = request.getParameterMap().get("cookie")[0];
                    if (StringUtils.isBlank(cookie)) {
                        // post body
                        cookie = IOUtils.toString(request.getReader());
                    }
                    token = cookie.split("=")[1];
                } catch (Exception e) {
                    log.error("Failed to parse cookie, origin msgs: {}", e.getMessage());
                }
            }
            try {
                if (StringUtils.isNotBlank(token)) {
                    tokenUtil.checkSign(token);
                    // 校验token, 并获取信息
                    SysUserPojo userPojo = tokenUtil.getUserInfo(token);
                    if (Objects.nonNull(userPojo)) {
                        UserUtil.setUser(userPojo);
                        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(userPojo,
                                null,
                                AuthorityUtils.createAuthorityList(RoleUtil.getRoleNames(userPojo.getRoleName())));
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
            } finally {
                UserUtil.removeUser();
            }
        }
    }
}