<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
package ${entity.basePackage}.service;

import ${entity.basePackage}.entity.${entity.name};
import ${entity.basePackage}.dao.${entity.name}Dao;

public class ${entity.name}Service extends norm.Service<${entity.name}, ${entity.idColumn.javaType}> {

    public ${entity.name}Service(){
        super(${entity.name}Dao.class);
    }

}