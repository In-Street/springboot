package cyf.gradle.shiro.service;

import cyf.gradle.dao.mapper.*;
import cyf.gradle.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * userservice
 *
 * @author Cheng Yufei
 * @create 2017-08-15 14:57
 **/
@Service
public class UserInfoServie {

    @Autowired
    UserMapper mapper;

    @Autowired
    SysUserRoleMapper userRoleMapper;

    @Autowired
    SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    SysPermissionMapper sysPermissionMapper;

    @Autowired
    SysRoleMapper roleMapper;

    public User findByUsername(String name) {
        UserExample example = new UserExample();
        example.setLimit(1);
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
        List<User> list = mapper.selectByExample(example);
        User user = list.get(0);
        user.setRoleList(getSysRole(user.getId()));
        return list.get(0);
    }

    public List<SysRole> getSysRole(Integer uid) {
        //获取 roleid
        SysUserRoleExample userRoleExample = new SysUserRoleExample();
        SysUserRoleExample.Criteria criteria = userRoleExample.createCriteria();
        criteria.andUidEqualTo(uid);
        List<SysUserRole> sysUserRoles = userRoleMapper.selectByExample(userRoleExample);
        List<Integer> roleIds = new ArrayList<>();
        for (SysUserRole sysUserRole : sysUserRoles) {
            roleIds.add(sysUserRole.getRoleId());
        }

        SysRoleExample sysRoleExample = new SysRoleExample();
        SysRoleExample.Criteria criteria1 = sysRoleExample.createCriteria();
        criteria1.andIdIn(roleIds);
        List<SysRole> sysRoles = roleMapper.selectByExample(sysRoleExample);

        return sysRoles;
    }
}
