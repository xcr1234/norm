<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
package ${entity.basePackage}.dao;

import norm.CrudDao;
import ${entity.basePackage}.entity.${entity.name};
public interface ${entity.name}Dao extends CrudDao<${entity.name} , ${entity.idColumn.javaType}> {

}