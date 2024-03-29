package org.api.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.UserReq;
import org.api.admin.model.res.RefreshTokenRes;
import org.api.admin.model.res.UserRes;
import org.core.config.JwtConfig;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.core.utils.RoleUtil;
import org.core.utils.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service(AdminConfig.ADMIN + "LoginApi")
@RequiredArgsConstructor
public class LoginApi {
    private final AccountService accountService;
    
    
    public UserRes login(String phone, String password) {
        SysUserPojo userPojo = accountService.login(phone, password);
        Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        String sign = TokenUtil.signToken(date, userPojo.getUsername(), userPojo);
        
        Date refreshDate = new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime());
        String refreshToken = TokenUtil.refreshSignToken(refreshDate, userPojo.getUsername(), userPojo);
        
        return new UserRes(userPojo.getUsername(), sign, refreshToken, RoleUtil.getRoleNames(userPojo.getRoleName()), date.getTime());
    }
    
    public void createAccount(UserReq req) {
        accountService.createAccount(req);
    }
    
    public RefreshTokenRes refreshUserToken(String refresh) {
        TokenUtil.isJwtExpired(refresh);
        TokenUtil.checkSign(refresh);
        
        SysUserPojo userInfo = TokenUtil.getRefreshUserInfo(refresh);
        Date date = new Date(System.currentTimeMillis() + JwtConfig.getExpireTime());
        Date refreshDate = new Date(System.currentTimeMillis() + JwtConfig.getRefreshExpireTime());
        String token = TokenUtil.signToken(date, userInfo.getUsername(), userInfo);
        String newRefresh = TokenUtil.refreshSignToken(refreshDate, userInfo.getUsername(), userInfo);
        
        return new RefreshTokenRes(token, newRefresh, date.getTime());
    }
}
