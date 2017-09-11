<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
<#assign className = entity.name>
<#assign classNameLower = className?uncap_first>

package ${entity.basePackage}.${args.moduleName}.dao;
import java.util.List;


import ${entity.basePackage}.${args.moduleName}.domain.${className}Domain;


public interface ${className}Dao  {

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
    @SuppressWarnings("unchecked")
    public List<${className}Domain> getMatched${className}List(${className}Domain ${classNameLower}Domain);


    /**
     * 保存
     * @param ${classNameLower}Domain
     */
    public void save${className}(${className}Domain ${classNameLower}Domain);

    /**
     * 更新
     * @param ${classNameLower}Domain
     */
    public int update${className}(${className}Domain ${classNameLower}Domain);

    /**
     * 删除
     * @param ${classNameLower}Domain
     */
    public int delete${className}(${className}Domain ${classNameLower}Domain);



}
