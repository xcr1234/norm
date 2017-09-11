package norm.generator;


import java.io.File;
import java.util.Map;

public interface EntityWriter {

    void write(File out, Entity entity,Map<String,String> argMap)throws Exception;


}
