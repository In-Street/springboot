package cyf.gradle.dao.model;

import java.io.Serializable;

/**
 * @author 
 */
public class SysUserRole implements Serializable {
    private Integer uid;

    private Integer roleId;

    private static final long serialVersionUID = 1L;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}