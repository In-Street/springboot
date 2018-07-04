package cyf.gradle.dao.model;

import java.io.Serializable;

/**
 * @author 
 */
public class SysRolePermission implements Serializable {
    private Integer permissionId;

    private Integer roleId;

    private static final long serialVersionUID = 1L;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}