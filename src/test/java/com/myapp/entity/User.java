package com.myapp.entity;

import norm.anno.Id;
import norm.anno.Column;

public class User{


    @Id
    private Integer id;

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    @Column("name")
    private String name;
    public String getName(){
        return this.name;
    }
    public void setName(String name){
         this.name = name;
    }

}