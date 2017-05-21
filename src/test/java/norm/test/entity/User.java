package norm.test.entity;


import norm.anno.Id;
import norm.anno.JoinColumn;

import java.util.List;

public class User {
    @Id
    private Integer id;
    private String name;

    @JoinColumn(target = Role.class ,mappedBy = "user")
    private List<Role> roleList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}
