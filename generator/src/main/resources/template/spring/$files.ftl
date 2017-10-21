<?xml version="1.0" encoding="UTF-8"?>
<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#--$files.ftl是一个很特殊的文件，它指定了目录下这些ftl文件最终生成的文件名路径。如果不指定encoding，默认用Charset.defaultCharset-->
<files encoding="UTF-8">
    <file src="dao.ftl" target="${entity.basePackage?replace(".","/")}/dao/${entity.name}Dao.java"></file>
    <file src="entity.ftl" target="${entity.basePackage?replace(".","/")}/entity/${entity.name}.java"></file>
    <file src="service.ftl" target="${entity.basePackage?replace(".","/")}/service/${entity.name}Service.java"></file>
</files>