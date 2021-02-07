package org.sun.bright.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sun.bright.framework.result.Result;

/**
 * 鉴权API
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    public Result getCode() {

        return Result.ok();
    }

    @PostMapping(value = "/login")
    public Result login(@RequestParam(name = "account") String loginName,
                        @RequestParam(name = "password") String password,
                        @RequestParam(name = "codeKey") String codeKey,
                        @RequestParam(name = "code") String codeValue) {
        authService.loginValidation(loginName, password, codeKey, codeValue);
        return Result.ok();
    }

}
