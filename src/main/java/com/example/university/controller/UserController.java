package com.example.university.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.university.common.R;
import com.example.university.entity.User;
import com.example.university.service.UserService;
import com.example.university.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author linyin
 * @since 2023-06-21
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("三大外卖","",phone,code);

            //  qq邮箱
            //EmailUtil.sendAuthCodeEmail(phone,code);

            //需要将生成的验证码保存到Session 将code存储在phone的会话(session)属性中
            session.setAttribute(phone, code);
            return R.success("验证码发送成功");
        }

        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        Object code = map.get("code").toString();
        //从session获取验证码 访问session中 phone键关联的值
        Object codeInSession = session.getAttribute(phone);
        if (codeInSession != null && codeInSession.equals(code)) {
            //验证码比对
            //判断手机号是否注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);

        }

        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        //用于从当前会话 (session) 中移除名为 "user" 的属性。
        request.getSession().removeAttribute("user");
        return R.success("cg");
    }
}

