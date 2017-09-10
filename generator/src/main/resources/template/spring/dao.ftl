
<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
package ${entity.basePackage}.entity;


import norm.CrudDao;
import org.springframework.stereotype.Repository;
import ${entity.basePackage}.entity.${entity.name};
@Repository
public interface ${entity.name}Dao extends CrudDao<${entity.name} , ${entity.idColumn.javaType}> {



}