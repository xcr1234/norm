<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
<#-- @ftlvariable name="args" type="java.util.Map<java.lang.String,java.lang.String>" -->
<#-- @ftlvariable name="args.moduleName" type="java.lang.String" -->
package ${entity.basePackage}.${args.moduleName}.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.foresealife.common.DateJsonValueProcessor;


import ${entity.basePackage}.${args.moudleName}.domain.${entity.name}Domain;
import ${entity.basePackage}.${args.moudleName}.service.${entity.name}Service;


@Controller
public class ${entity.name}Controller {

    @Autowired
    private ${entity.name}Service ${entity.name?cap_first}Service;

    //进入主页面
    @RequestMapping
    public String mainPage(${entity.name}Domain filterMask, Model model) {
        model.addAttribute("filterMask", filterMask);
        return "${args.moduleName}/${entity.name}/main" ;
    }


    @RequestMapping(value = "/${args.moduleName}/${entity.name?cap_first}/query")
    public void getJsonDataGrid(HttpServletRequest request,
                                HttpServletResponse response, ${entity.name}Domain filterMask) throws Exception {
        List<${entity.name}Domain> list = new ArrayList<${entity.name}Domain>();
        int total = ${entity.name?cap_first}Service.getMatched${entity.name}Count(filterMask);
        list = ${entity.name?cap_first}Service.getMatched${entity.name}List(filterMask);
        PrintWriter write = response.getWriter();
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        JSONObject o = JSONObject.fromObject(map, config);

        write.write(o.toString());
        write.flush();
        write.close();
        o.clear();
    }

    /**
     * 进入新增页面
     * @param filterMask
     * @param model
     * @return
     */
    @RequestMapping(value = "/${args.moduleName}/${entity.name?cap_first}/newPage", method = RequestMethod.POST)
    String NewPage(${entity.name}Domain filterMask, Model model) {
        model.addAttribute("filterMask", filterMask);
        return "${args.moduleName}/${entity.name}/new" ;
    }

    /**
     * 新增
     * @param country
     * @param result
     * @return
     */
    @RequestMapping(value = "/${args.moduleName}/${entity.name?cap_first}/save")
    public String save(HttpServletRequest request, Model model,
                       HttpServletResponse response, ${entity.name}Domain filterMask) throws Exception {
        //保存
        ${entity.name?cap_first}Service.save(filterMask);
        model.addAttribute("filterMask", new ${entity.name}Domain());
        return "${args.moduleName}/${entity.name}/main" ;

    }

    /**
     * 进入修改页面
     * @param filterMask
     * @param model
     * @return
     */
    @RequestMapping(value = "/${args.moduleName}/${entity.name?cap_first}/editPage", method = RequestMethod.POST)
    public String editPage(${entity.name}Domain filterMask, Model model) throws Exception {
        model.addAttribute("filterMask", filterMask);
        return "${args.moduleName}/${entity.name}/edit" ;
    }


    /**
     * 修改
     * @param country
     * @param result
     * @return
     */
    @RequestMapping(value = "/${args.moduleName}/${entity.name?cap_first}/edit")
    public String edit(HttpServletRequest request, Model model,
                       HttpServletResponse response, ${entity.name}Domain filterMask) throws Exception {

        ${entity.name?cap_first}Service.update(filterMask);
        model.addAttribute("filterMask", new ${entity.name}Domain());
        return "${args.moduleName}/${entity.name}/main" ;
    }


    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/${args.moduleName}/${entity.name?cap_first}/delete")
    public void delete(${entity.name}Domain filterMask, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ${entity.name?cap_first}Service.delete(filterMask);
        PrintWriter write = response.getWriter();
        write.write("SUCC");
        write.flush();
        write.close();
    }
}