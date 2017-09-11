package norm.generator;



import java.util.List;
import java.util.Map;

public interface EntityBuilder {
    List<Entity> build(Map<String,String> argMap, Converter converter, String basePackage) throws GenerateException;
    String getMode();
}
