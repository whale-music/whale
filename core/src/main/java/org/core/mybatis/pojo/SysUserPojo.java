package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户表(SysUser)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
@Schema(name = "SysUser", description = "系统用户表")
public class SysUserPojo extends Model<SysUserPojo> implements Serializable {
    public static final long serialVersionUID = -96556752193973287L;
    
    @Schema(title = "系统用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "登录用户名")
    @TableField("username")
    private String username;
    
    @Schema(title = "登录显示昵称")
    @TableField("nickname")
    private String nickname;
    
    @Schema(title = "用户密码")
    @TableField("password")
    private String password;
    
    @Schema(title = "个性签名")
    @TableField("signature")
    private String signature;
    
    @Schema(title = "账户类型")
    @TableField("account_type")
    private Integer accountType;
    
    @Schema(title = "账户角色")
    @TableField("role_name")
    private String roleName;
    
    @Schema(title = "最后登录IP")
    @TableField("last_login_ip")
    private String lastLoginIp;
    
    @Schema(title = "最后登录时间")
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @Schema(title = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    
}

