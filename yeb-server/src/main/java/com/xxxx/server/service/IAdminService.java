package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 2142
 * @since 2023-12-01
 */
public interface IAdminService extends IService<Admin> {
    //登录之后返回token
    RespBean login(String username, String password, HttpServletRequest request);
}
