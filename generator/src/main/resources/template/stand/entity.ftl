
<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
package ${entity.basePackage}.entity;

import norm.anno.Id;
import norm.anno.Column;

public class ${entity.name}{


    @Id
    @Column("${entity.idColumn.columnName}")
    private ${entity.idColumn.javaType} ${entity.idColumn.javaName};

    public ${entity.idColumn.javaType} ${entity.idColumn.getterName}(){
        return this.${entity.idColumn.javaName};
    }

    public void ${entity.idColumn.setterName}(${entity.idColumn.javaType} ${entity.idColumn.javaName}){
        this.${entity.idColumn.javaName} = ${entity.idColumn.javaName};
    }

    <#list entity.columns as column>
    @Column("${column.columnName}")
    private ${column.javaType} ${column.javaName};
    public ${column.javaType} ${column.getterName}(){
        return this.${column.javaName};
    }
    public void ${column.setterName}(${column.javaType} ${column.javaName}){
         this.${column.javaName} = ${column.javaName};
    }
    </#list>

}