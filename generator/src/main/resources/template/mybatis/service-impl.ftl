<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
<#assign className = entity.name>
<#assign classNameLower = className?uncap_first>
<#assign moduleName = args.moduleName>
<#assign basepackage = entity.basePackage>

package ${basepackage}.${moduleName}.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


import ${basepackage}.${moduleName}.domain.${className}Domain;
import ${basepackage}.${moduleName}.dao.${className}Dao;
import ${basepackage}.${moduleName}.service.${className}Service;


@Service("${classNameLower}Service")
public class ${className}ServiceImpl implements ${className}Service{

    @Autowired
    private ${className}Dao ${classNameLower}Dao;

    /**
     * 总数
     * @param ${classNameLower}Domain
     * @return
     */
    public int getMatched${className}Count(${className}Domain ${classNameLower}Domain){
        return ${classNameLower}Dao.getMatched${className}Count(${classNameLower}Domain);
    }

    /**
     * 列表
     * @param ${classNameLower}Domain
     * @return
     */
    public List<${className}Domain> getMatched${className}List(${className}Domain ${classNameLower}Domain){
        return ${classNameLower}Dao.getMatched${className}List(${classNameLower}Domain);
    }

    /**
     * 保存
     * @param ${classNameLower}Domain
     */
    @Transactional
    public int save(${className}Domain ${classNameLower}Domain){
        ${classNameLower}Dao.save(${classNameLower}Domain);
        return 1;
    }

    /**
     * 更新
     * @param ${classNameLower}Domain
     */
    @Transactional
    public int update(${className}Domain ${classNameLower}Domain){
        return ${classNameLower}Dao.update(${classNameLower}Domain);
    }

    /**
     * 删除
     * @param ${classNameLower}Domain
     */
    @Transactional
    public int delete(${className}Domain ${classNameLower}Domain){
        return ${classNameLower}Dao.delete(${classNameLower}Domain);
    }


}