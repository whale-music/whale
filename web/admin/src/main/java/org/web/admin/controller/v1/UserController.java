package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.PageUserReq;
import org.api.admin.model.req.SaveOrUpdateUserReq;
import org.api.admin.service.UserApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.R;
import org.core.mybatis.pojo.SysUserPojo;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "UserController")
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    
    private final UserApi userApi;
    
    public UserController(UserApi userApi) {
        this.userApi = userApi;
    }
    
    @GetMapping("/{id}")
    public R getUserInfo(@PathVariable("id") Long id) {
        return R.success(userApi.getUserInfo(id));
    }
    
    @AnonymousAccess
    @GetMapping("/getExampleRoles")
    public R getExampleRoles() {
        return R.success(userApi.getExampleRoles());
    }
    
    @PostMapping("/page")
    public R getUserPage(@RequestBody PageUserReq user) {
        return R.success(userApi.getUserPage(user));
    }
    
    @PostMapping("/")
    public R saveOrUpdateUser(@RequestBody SaveOrUpdateUserReq saveOrUpdateUserReq) {
        return R.success(userApi.saveOrUpdateUser(saveOrUpdateUserReq));
    }
    
    @DeleteMapping("/{id}")
    public R deleteUser(@PathVariable("id") Long id) {
        userApi.deleteUser(id);
        return R.success();
    }
    
    @PostMapping("/update/account")
    public R updateUserPassword(@RequestBody SysUserPojo sysUserPojo) {
        userApi.updateUserInfo(sysUserPojo);
        return R.success();
    }
}
