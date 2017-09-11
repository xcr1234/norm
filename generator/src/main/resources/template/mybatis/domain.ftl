<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
<#assign className = entity.name>
<#assign classNameLower = className?uncap_first>

package ${entity.basePackage}.${args.moduleName}.domain;

import javax.persistence.Entity;
import org.springframework.format.annotation.DateTimeFormat;
import com.foresealife.common.PaginatedHelper;


@Entity
public class ${className}Domain extends PaginatedHelper implements java.io.Serializable{
    //别名，页面中使用
    public static final String TABLE_ALIAS = "${entity.tableName}";
    public static final String ALIAS_ID = "${entity.idColumn.columnName}";
    <#list entity.columns as column>
        public static final String ALIAS_${column.columnName?upper_case} = "${column.columnName}";
    </#list>

    private ${entity.idColumn.javaType} ${entity.idColumn.javaName};

    public ${entity.idColumn.javaType} ${entity.idColumn.getterName}(){
        return this.${entity.idColumn.javaName};
    }

    public void ${entity.idColumn.setterName}(${entity.idColumn.javaType} ${entity.idColumn.javaName}){
        this.${entity.idColumn.javaName} = ${entity.idColumn.javaName};
    }

    <#list entity.columns as column>
    private ${column.javaType} ${column.javaName};

    public ${column.javaType} ${column.getterName}(){
        return this.${column.javaName};
    }

    public void ${column.setterName}(${column.javaType} ${column.javaName}){
        this.${column.javaName} = ${column.javaName};
    }
    </#list>




}