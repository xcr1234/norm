<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<?xml version="1.0" encoding="UTF-8"?>
<files encoding="UTF-8">
    <file src="dao.ftl" target="${entity.basePackageDir}/dao/${entity.name}Dao.java"></file>
    <file src="entity.ftl" target="${entity.basePackageDir}/entity/${entity.name}.java"></file>
    <file src="service.ftl" target="${entity.basePackageDir}/service/${entity.name}Service.java"></file>
</files>