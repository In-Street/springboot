package cyf.gradle.dao.model;

import lombok.Builder;

import java.io.Serializable;

/**
 * @author
 */
@Builder
public class SysRole implements Serializable, Cloneable {
    private Integer id;

    private String role;

    private String description;

    private Boolean available;

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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public SysRole clone() throws CloneNotSupportedException {
        return (SysRole)super.clone();
    }
}