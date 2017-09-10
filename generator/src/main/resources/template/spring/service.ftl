
<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
package ${entity.basePackage}.service;


import norm.page.Page;
import java.util.List;
import org.springframework.stereotype.Service;
import norm.support.spring.SpringService;
import ${entity.basePackage}.entity.${entity.name};
import ${entity.basePackage}.dao.${entity.name}Dao;

@Service
public class ${entity.name}Service extends SpringService<${entity.name} , ${entity.idColumn.javaType}> {

    public ${entity.name}Service(){
        super(${entity.name}Dao.class);
    }

}