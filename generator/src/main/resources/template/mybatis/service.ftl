<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
<#assign className = entity.name>
<#assign classNameLower = className?uncap_first>
<#assign moduleName = args.moduleName>
<#assign basepackage = entity.basePackage>
package ${basepackage}.${moduleName}.service;

import ${basepackage}.${moduleName}.domain.${className}Domain;
import java.util.List;


public interface ${className}Service{
    /**
     * 总数
     * @param ${classNameLower}Domain
     * @return
     */
    public int getMatched${className}Count(${className}Domain ${classNameLower}Domain);

    /**
     * 列表
     * @param ${classNameLower}Domain
     * @return
     */
    public List<${className}Domain> getMatched${className}List(${className}Domain ${classNameLower}Domain);

    /**
     * 保存
     * @param ${classNameLower}Domain
     */
    public int save(${className}Domain ${classNameLower}Domain);

    /**
     * 更新
     * @param ${classNameLower}Domain
     */
    public int update(${className}Domain ${classNameLower}Domain);

    /**
     * 删除
     * @param ${classNameLower}Domain
     */
    public int delete(${className}Domain ${classNameLower}Domain);



}