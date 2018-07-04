package cyf.gradle.shiro.controller;

import cyf.gradle.dao.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static cyf.gradle.util.FastJsonUtils.toBean;
import static cyf.gradle.util.FastJsonUtils.toJSONString;


/**
 * @author Cheng Yufei
 * @create 2017-08-15 17:52
 **/
@Controller
@RequestMapping("/user")
public class UserInfoController {

    /**
     * 用户查询.
     *
     * @return
     */
    @RequestMapping("/userList")
    @RequiresPermissions("userInfo:view")//权限管理;
    public String userInfo() {
        return "userInfo";
    }

    /**
     * 用户添加;
     *
     * @return
     */
    @RequestMapping("/userAdd")
    @RequiresPermissions("userInfo:add")//权限管理;
    public String userInfoAdd(HttpServletRequest request) {
        //获取登录用户
        Subject t = SecurityUtils.getSubject();
        String str = toJSONString(t.getPrincipal());

        User user = toBean(str, User.class);
        String name = user.getUsername();
        System.out.println(name);
        return "userInfoAdd";
    }

    /**
     * 用户删除;
     *
     * @return
     */
    @RequestMapping("/userDel")
    @RequiresPermissions("userInfo:del")//权限管理;
    public String userDel() {

        return "userInfoDel";
    }
}
