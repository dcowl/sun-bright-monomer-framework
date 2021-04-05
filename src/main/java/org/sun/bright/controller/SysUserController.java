package org.sun.bright.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sun.bright.framework.result.Result;

/**
 * 参数校验常量类
 *
 * @author <a href="mailto:2867665887@qq.com">SunBright</a>
 * @version 1.0
 * @since 2021/03/01
 */
@RestController
@RequestMapping(value = "/api/v1/users")
public class SysUserController {

    @GetMapping(value = "/page")
    public Result userPageInfo(@RequestParam(name = "page", defaultValue = "1") Integer curPage,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                               String account, String username, Integer isDelete, Integer flag) {

        return Result.ok();
    }

}
