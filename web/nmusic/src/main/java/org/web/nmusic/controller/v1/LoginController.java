package org.web.nmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.Header;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.login.status.LoginStatusRes;
import org.api.nmusic.model.vo.user.UserVo;
import org.api.nmusic.service.LoginApi;
import org.api.nmusic.service.UserApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.constant.CookieConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.NeteaseResult;
import org.core.common.result.ResultCode;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.utils.GlobeDataUtil;
import org.core.utils.UserUtil;
import org.core.utils.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web.nmusic.controller.BaseController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * NeteaseCloudMusicApi 登录控制器
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@RestController(NeteaseCloudConfig.NETEASECLOUD + "LoginController")
@RequestMapping("/")
@Slf4j
public class LoginController extends BaseController {
    
    @Autowired
    private UserApi user;
    
    @Autowired
    private LoginApi loginApi;
    
    @Autowired
    private TokenUtil tokenUtil;
    
    /**
     * 登录接口, 邮箱
     *
     * @return 返回登录结果
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult loginMail(HttpServletResponse response, @RequestParam Map<String, String> req) {
        if (CollUtil.isEmpty(req)) {
            return new NeteaseResult().success();
        }
        String email = req.get("email");
        String password = req.get("password");
        String md5Password = req.get("md5_password");
        UserConvert userPojo = user.login(email, password, md5Password);
        UserVo userVo = getUserVo(userPojo);
        // 生成sign
        NeteaseResult r = getNeteaseResult(response, tokenUtil, userPojo);
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    /**
     * 登录接口, 手机
     *
     * @return 返回登录结果
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @AnonymousAccess
    @RequestMapping(value = "/login/cellphone", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult loginPhone(HttpServletResponse response,
                                    @RequestParam(value = "phone", required = false) String phone,
                                    @RequestParam(value = "password", required = false) String password,
                                    @RequestParam(value = "md5_password", required = false) String md5Password
    ) {
        UserConvert userPojo = user.login(phone, password, md5Password);
        UserVo userVo = getUserVo(userPojo);
        // 生成sign
        NeteaseResult r = getNeteaseResult(response, tokenUtil, userPojo);
        r.putAll(BeanUtil.beanToMap(userVo));
        return r.success();
    }
    
    /**
     * 登录接口
     *
     * @return 返回登录结果
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/cellphone/existence/check", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult cellphoneCheck(@RequestParam("phone") Long phone, @RequestParam(value = "countrycode", required = false, defaultValue = "86") String countrycode) {
        UserConvert userPojo = user.checkPhone(phone, countrycode);
        
        boolean b = userPojo == null;
        userPojo = b ? new UserConvert() : userPojo;
        NeteaseResult r = new NeteaseResult();
        r.put("exist", b ? -1 : 1);
        r.put("nickname", userPojo.getNickname());
        r.put("hasPassword", b);
        r.put("avatarUrl", userPojo.getAvatarUrl());
        r.put("hasSnsBinded", !b);
        r.put("countryCode", "86");
        r.put("cellphone", userPojo.getId());
        return r.success();
    }
    
    private static void getQrUrl(HttpServletRequest request, String uuidString, Map<String, Object> r) {
        String localhost = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String value = "/login-key/index.html?key=" + uuidString;
        String qrUrl = localhost + value;
        r.put("qrurl", qrUrl);
        r.put("qrimg", Base64.encode(qrUrl));
    }
    
    /**
     * 创建二维码登录uuid
     * 在原有基础上增加返回qr url, 用于获取二维码登录地址
     *
     * @param request HttpServlet
     * @return NeteaseResult
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/login/qr/key", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult qrKey(HttpServletRequest request) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        GlobeDataUtil.setData(uuidString, uuidString);
        
        NeteaseResult r = new NeteaseResult();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("unikey", uuidString);
        
        getQrUrl(request, uuidString, r);
        return r.success(map);
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @GetMapping("/login/qr/create")
    @AnonymousAccess
    public NeteaseResult qrCreate(HttpServletRequest request, @RequestParam("key") String key) {
        NeteaseResult r = new NeteaseResult();
        Map<String, Object> map = new HashMap<>();
        String data = GlobeDataUtil.getData(key);
        if (data == null) {
            return r.error(ResultCode.QR_ERROR);
        }
        getQrUrl(request, data, map);
        return r.success(map);
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @GetMapping("/login/sure")
    @AnonymousAccess
    public NeteaseResult qrSure(@RequestParam("codekey") String codekey, String phone, String password, @RequestParam(value = "md5_password", required = false) String md5Password) throws JsonProcessingException {
        String data = GlobeDataUtil.getData(codekey);
        NeteaseResult r = new NeteaseResult();
        if (data == null) {
            r.put(CookieConstant.COOKIE_NAME_MUSIC_U, "");
            return r.error("800", "二维码不存在或已过期");
        }
        SysUserPojo userPojo = user.login(phone, password, md5Password);
        ObjectMapper objectMapper = new ObjectMapper();
        GlobeDataUtil.setData(codekey, objectMapper.writeValueAsString(userPojo));
        return r.success();
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @GetMapping("/login/qr/check")
    @AnonymousAccess
    public NeteaseResult qrCreate(HttpServletResponse response, @RequestParam("key") String key) throws JsonProcessingException {
        String data = GlobeDataUtil.getData(key);
        if (data == null) {
            NeteaseResult r = new NeteaseResult();
            r.put(CookieConstant.COOKIE_NAME_MUSIC_U, "");
            return r.error("800", "二维码不存在或已过期");
        }
        if (Objects.equals(key, data)) {
            NeteaseResult r = new NeteaseResult();
            r.put(CookieConstant.COOKIE_NAME_MUSIC_U, "");
            return r.error("801", "等待扫码");
        }
        SysUserPojo userPojo = new ObjectMapper().readValue(data, SysUserPojo.class);
        String sign = tokenUtil.neteasecloudmusicSignToken(userPojo.getUsername(), userPojo);
        GlobeDataUtil.remove(key);
        
        Cookie cookie = new Cookie(Header.COOKIE.getValue(), sign);
        response.addCookie(cookie);
        
        NeteaseResult r = new NeteaseResult();
        r.put("code", 803);
        r.put("message", "授权登陆成功");
        r.put(CookieConstant.COOKIE_NAME_COOKIE, CookieConstant.COOKIE_NAME_MUSIC_U + "=" + sign);
        
        return r;
    }
    
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/login/status", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult loginStatus(@RequestParam(value = "uid", required = false) Long uid) {
        uid = uid == null ? UserUtil.getUser().getId() : uid;
        LoginStatusRes res = loginApi.status(uid);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    /**
     * 注册接口
     *
     * @param account  账号
     * @param password 密码
     * @param nickname 昵称
     * @return 返回成功信息
     */
    @AnonymousAccess
    @WebLog(LogNameConstant.N_MUSIC)
    @GetMapping("/register/account")
    public NeteaseResult addUser(String account, String password, String nickname) {
        SysUserPojo userPojo = new SysUserPojo();
        userPojo.setUsername(account);
        userPojo.setNickname(nickname);
        userPojo.setPassword(password);
        user.createAccount(userPojo);
        return new NeteaseResult().success();
    }
    
    /**
     * 登录刷新
     *
     * @param response servlet response
     * @return 返回token and Cookie
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @GetMapping("/login/refresh")
    public NeteaseResult refresh(HttpServletResponse response) {
        SysUserPojo userPojo = UserUtil.getUser();
        if (userPojo == null) {
            log.warn(ResultCode.USER_NOT_EXIST.getResultMsg());
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        return getNeteaseResult(response, tokenUtil, userPojo);
    }
    
    
    /**
     * 登出接口
     *
     * @param allParam 所有方法
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult userLogout(HttpServletResponse response, @RequestParam Map<String, String> allParam) {
        return this.logout(response);
    }
    
}
