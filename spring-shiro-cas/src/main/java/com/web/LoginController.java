
package com.web;

import com.google.code.kaptcha.servlet.KaptchaExtend;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(String username, String password, HttpServletRequest request){

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

        if(!subject.isAuthenticated()){
            //验证吗
            //KaptchaExtend  kaptchaExtend = new KaptchaExtend();
            //kaptchaExtend.getGeneratedKey(request).equalsIgnoreCase("");

            try {
                subject.login(token);
            } catch (UnknownAccountException uae) {
                return "login";
            } catch (IncorrectCredentialsException ice) {
                return "login";
            } catch (LockedAccountException lae) {
                return "login";
            } catch (AuthenticationException ae) {
                return "login";
            }
        }

        return "success";
    }

    /**
     * 验证码
     */
    @RequestMapping("/captcha")
    @ResponseBody
    public  void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        KaptchaExtend kaptchaExtend =  new KaptchaExtend();
        kaptchaExtend.captcha(request, response);
    }

    @RequestMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "login";
    }
}
