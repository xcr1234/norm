<?xml version="1.0" encoding="UTF-8"?>
<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
<files>
    <file src="controller.ftl" target="${entity.basePackageDir}/${args.moduleName}/controller/${entity.name}Controller.java"></file>
    <file src="dao.ftl" target="${entity.basePackageDir}/${args.moduleName}/dao/${entity.name}Dao.java"></file>
    <file src="domain.ftl" target="${entity.basePackageDir}/${args.moduleName}/domain/${entity.name}Domain.java"></file>
    <file src="service.ftl" target="${entity.basePackageDir}/${args.moduleName}/service/${entity.name}Service.java"></file>
    <file src="service-impl.ftl" target="${entity.basePackageDir}/${args.moduleName}/service/impl/${entity.name}ServiceImpl.java"></file>

</files>
