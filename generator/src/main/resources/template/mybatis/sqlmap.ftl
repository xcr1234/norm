<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
<#assign className = entity.name>
<#assign classNameLower = className?uncap_first>
<#assign moduleName = args.moduleName>
<#assign basepackage = entity.basePackage>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${basepackage}.${moduleName}.${className}Dao">

    <!-- 查询sql结果与java model映射 -->
    <resultMap id="${className}ResultMap"
               type="${basepackage}.${moduleName}.domain.${className}Domain">
    <#list entity.columns as column>
        <result property="${column.javaName}" column="${column.columnName}" />
    </#list>
    </resultMap>


    <!-- 公用查询sql -->
    <sql id="${className}Query">
        <![CDATA[
	    select
	    <#list table.columns as column>${column.sqlName} <#if column_has_next>,</#if></#list>
	    from ${table.sqlName}
	    ]]>
        <where>
        <#list table.columns as column>
            <if test="${column.columnNameLower} != null and ${column.columnNameLower}!=''">
                and ${column.sqlName} = #${leftParentheses}${column.columnNameLower}${rightParentheses}
            </if>
        </#list>
        </where>
    </sql>


    <!--查询记录数 -->
    <select id="getMatched${className}Count"
            parameterType="${basepackage}.${moduleName}.domain.${className}Domain"
            resultType="java.lang.Integer">
        select count(*) from (
        <include refid="${className}Query" />
        )
    </select>

    <!--分页查询 -->
    <select id="getMatched${className}List"
            parameterType="${basepackage}.${moduleName}.domain.${className}Domain"
            resultMap="${className}ResultMap">
        <if test="pageSize!= null">
            SELECT * FROM ( select rownum r, union_tb.* from (
        </if>
        <include refid="${className}Query" />
        <if test="pageSize!= null">
            <![CDATA[
					) union_tb
				) WHERE r > #${leftParentheses}startIndex${rightParentheses}
					AND r <= #${leftParentheses}startIndex${rightParentheses} + #${leftParentheses}pageSize${rightParentheses}
				]]>
        </if>
    </select>

    <!-- 删除 -->
    <delete id="delete${className}"
            parameterType="${basepackage}.${moduleName}.domain.${className}Domain">
        <![CDATA[
	        DELETE FROM ${table.sqlName} WHERE
	        <#list table.compositeIdColumns as column>
		          ${column.sqlName} = #${leftParentheses}${column.columnNameLower}${rightParentheses} <#if column_has_next> AND </#if>
			</#list>
    	]]>
    </delete>

    <!-- 修改 -->
    <update id="update${className}"
            parameterType="${basepackage}.${moduleName}.domain.${className}Domain">
        UPDATE ${table.sqlName} SET
    <#list table.notPkColumns as column>
        <if test="${column.columnNameLower} != null">
        ${column.sqlName} = #${leftParentheses}${column.columnNameLower}${rightParentheses} <#if column_has_next>,</#if>
        </if>
    </#list>
    <#list table.compositeIdColumns as column>
        ,${column.sqlName} = #${leftParentheses}${column.columnNameLower}${rightParentheses} <#if column_has_next>,</#if>
    </#list>
        WHERE
    <#list table.compositeIdColumns as column>
    ${column.sqlName} = #${leftParentheses}${column.columnNameLower}${rightParentheses} <#if column_has_next>,</#if>
    </#list>
    </update>

    <!-- 保存 -->
    <insert id="save${className}"
            parameterType="${basepackage}.${moduleName}.domain.${className}Domain">
        <![CDATA[
		        INSERT INTO ${table.sqlName} (
		        <#list table.columns as column>
		        	${column.sqlName} <#if column_has_next>,</#if>
		        </#list>
		        ) VALUES (
		        <#list table.columns as column>
		        	#${leftParentheses}${column.columnNameLower}${rightParentheses} <#if column_has_next>,</#if>
		        </#list>
		        )
		    ]]>
    </insert>
</mapper>