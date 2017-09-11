package norm.generator;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import norm.generator.impl.DatabaseEntityBuilder;
import norm.generator.impl.SelectEntityBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorMain {

    private static Log logger = LogFactory.getLog(GeneratorMain.class);
    private static Map<String,EntityBuilder> entityBuilderMap = new HashMap<String, EntityBuilder>();

    private static void put(EntityBuilder entityBuilder){
        entityBuilderMap.put(entityBuilder.getMode().toLowerCase(),entityBuilder);
    }

    static {
        put(new SelectEntityBuilder());
        put(new DatabaseEntityBuilder());
    }

    public static void main(String[] args) {
        Map<String,String> argMap = new HashMap<String, String>();
        for(String arg:args){
            int i = arg.indexOf('=');
            if(i <= 0){
                logger.error("invalid argument:" + arg);
                return;
            }
            String key = arg.substring(0,i);
            String value = arg.substring(i+1);
            argMap.put(key,value);
        }

        doGenerate(argMap);
    }


    public static void doGenerate(Map<String,String> argMap){
        logger.info("generator startup with config args:" + argMap);
        String o = argMap.get("outputDir");
        if(o == null){
            logger.error("generator run failed,outputDir is null!");return;
        }
        File outDir = new File(o);
        if(!outDir.exists() || !outDir.isDirectory()){
            logger.error("generator run failed,out put is not a dir:" + outDir);
        }

        String basePackage = argMap.get("basePackage");
        if(basePackage == null){
            logger.error("generator run failed,basePackage is null!");return;
        }
        String idColumn = argMap.get("idColumn");
        if(idColumn == null){
            logger.error("generator run failed,idColumn is null!");return;
        }
        String conv = argMap.get("converter");
        Converter converter = null;
        if(conv == null){
            conv = "norm.generator.impl.DefaultConverter";
        }
        try{
            converter = (Converter) Class.forName(conv).newInstance();
        }catch (Exception e){
            logger.error("generator run failed,can't create converter!",e);return;
        }
        logger.info("converter is :" + converter);

        String mode = argMap.get("mode");
        if(mode == null || !entityBuilderMap.containsKey(mode)){
            logger.error("generator run failed,invalid mode!mode should be one of:" + entityBuilderMap.keySet());return;
        }

        String wr = argMap.get("entityWriter");
        EntityWriter entityWriter = null;
        if(wr != null){
            try{
                entityWriter = (EntityWriter) Class.forName(wr).newInstance();
            }catch (Exception e){
                logger.error("failed to create entityWriter :" + wr );
            }
        }

        EntityBuilder entityBuilder = entityBuilderMap.get(mode);
        List<Entity> entityList = null;
        try {
            entityList = entityBuilder.build(argMap,converter,basePackage);
        } catch (GenerateException e) {
            logger.error("create generator failed.",e);
            return;
        }


        for(Entity entity : entityList){
            if(entityWriter != null){
                entity.setWriter(entityWriter);
            }
            try {
                EntityWriter writer = entity.getWriter();
                writer.write(outDir,entity,argMap);
            } catch (Exception e) {
                logger.error("generator error!",e);
            }
        }





        logger.info("generator run successfully.");
    }

}
