package org.norm.test.beans;

import norm.anno.Column;
import norm.anno.Id;
import norm.anno.Table;

@Table("cars")
public class Car {
    @Id(identity = false)
    private Integer id;
    private String name;
    @Column("descrption")
    private String desc;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
