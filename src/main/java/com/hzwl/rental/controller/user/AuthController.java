package com.hzwl.rental.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.hzwl.rental.config.Result;
import com.hzwl.rental.constants.ErrorCode;
import com.hzwl.rental.entity.dto.HzwlUser;
import com.hzwl.rental.entity.user.RentalUser;
import com.hzwl.rental.service.user.RentalUserService;
import com.hzwl.rental.utils.EnvironmentUtils;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @Author GA666666
 * @Date 2023/8/09 22:27
 */
@Api(tags = "用户功能接口")
@Validated
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private RentalUserService userService;

    @Autowired
    private EnvironmentUtils environmentUtils;

    @GetMapping("hzwl-user")
    public Result getUserByKey(@RequestParam("key") @NotBlank String key) {
        HzwlUser hzwlUser = userService.getHzwlUserByKey(key);

        if (Objects.isNull(hzwlUser) && environmentUtils.isDev()) {
            hzwlUser = new HzwlUser();
            hzwlUser.setMobile("17604515707");
        }

        if (Objects.isNull(hzwlUser)) {
            return Result.error(ErrorCode.TOKEN_EXPIRATION);
        }
        userService.login(hzwlUser.getMobile());
        return Result.success(StpUtil.getTokenInfo());
    }


    @GetMapping("register")
    public Result login(@RequestParam("phoneNum") @NotBlank String phoneNum, @NotBlank @RequestParam("code") String code, HttpServletRequest request) {

       /* if (!CaptchaUtil.ver(code, request)) {
            CaptchaUtil.clear(request);
            return Result.error(ErrorCode.VERIFY_CODE_FAILED);
        }
        CaptchaUtil.clear(request);*/


        if (environmentUtils.isProd() && !userService.verifySmsCode(phoneNum, code)) {
            return Result.error(ErrorCode.VERIFY_CODE_FAILED);
        }


        RentalUser user = userService.login(phoneNum);
        if (Objects.nonNull(user)) {
            return Result.success();
        }
        return Result.error(ErrorCode.REGISTRATION_FAILED);
    }

    @GetMapping("sendSms")
    public Result sendSms(@RequestParam("phone") String phone) {
        userService.simCode(phone);
        return Result.success();
    }

    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);  // 几位数运算，默认是两位
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();
        CaptchaUtil.out(captcha, request, response);
    }

    @GetMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    @SaCheckLogin
    @GetMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @GetMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
