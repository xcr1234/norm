package norm.generator.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import norm.generator.Entity;
import norm.generator.EntityWriter;
import norm.generator.GenerateException;
import norm.generator.util.FileTask;
import norm.generator.util.FileTaskItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultEntityWriter implements EntityWriter {

    private Log logger = LogFactory.getLog(DefaultEntityWriter.class);

    private static final DefaultEntityWriter defaultEntityWriter = new DefaultEntityWriter();

    public static DefaultEntityWriter getInstance() {
        return defaultEntityWriter;
    }


    @Override
    public void write(File out, Entity entity, Map<String, String> argMap) throws IOException, TemplateException, GenerateException {
        String template = argMap.get("template");
        if (template == null) {
            throw new GenerateException("generator run failed,template is null!");
        }


        Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
        configuration.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(), "template/" + template);

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entity", entity);
        root.put("args",argMap);

        Template fileTemplate = configuration.getTemplate("$files.ftl");
        StringWriter stringWriter = new StringWriter();

        fileTemplate.process(root, stringWriter);

        String filesXml = stringWriter.toString();
        XStream xStream = new XStream(new Dom4JDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{FileTask.class,FileTaskItem.class});
        xStream.autodetectAnnotations(true);
        xStream.alias("files", FileTask.class);
        FileTask fileTask = (FileTask) xStream.fromXML(filesXml);
        List<FileTaskItem> fileTaskItemList = fileTask.getFileTaskItemList();

        String encoding = fileTask.getEncoding();
        if(encoding == null || encoding.isEmpty()){
            encoding = Charset.defaultCharset().displayName();
        }


        for (FileTaskItem fileTaskItem : fileTaskItemList) {
            File file = new File(out, fileTaskItem.getTarget());
            write(configuration, file, root, fileTaskItem.getSrc(),encoding);
        }

    }

    protected void write(Configuration configuration, File file, Map<String, Object> root, String templateName,String encoding) throws IOException, TemplateException {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("failed to make dir:" + parent);
            }
        }
        Template daoTemplate = configuration.getTemplate(templateName);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            daoTemplate.process(root, new OutputStreamWriter(outputStream, encoding));
            logger.info("generate file:" + file);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

}



