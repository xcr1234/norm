package org.norm.test.beans;

import norm.anno.Column;
import norm.anno.Converter;
import norm.anno.Id;
import norm.anno.Table;
import norm.convert.EnumNumberConverter;

@Table("cars")
public class Car {
    @Id(identity = false)
    private Integer id;
    private String name;
    @Column("descrption")
    private String desc;
    private Type type;
    @Converter(value = EnumNumberConverter.class)
    private Type type2;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType2() {
        return type2;
    }

    public void setType2(Type type2) {
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", type=" + type +
                ", type2=" + type2 +
                '}';
    }
}
