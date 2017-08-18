package cyf.gradle.dao.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author 
 */
public class SysRole implements Serializable {
    private Integer id;

    private String role;

    private String description;

    private Boolean availiable;

    private List<SysPermission> permissions;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailiable() {
        return availiable;
    }

    public void setAvailiable(Boolean availiable) {
        this.availiable = availiable;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }
}