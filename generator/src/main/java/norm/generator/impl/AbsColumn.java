package norm.generator.impl;

import norm.generator.Column;
import org.apache.commons.lang.StringUtils;

public abstract class AbsColumn implements Column{

    @Override
    public String getGetterName() {
        if("boolean".equals(getJavaType()) || "java.lang.Boolean".equals(getJavaType())){
            return "is" + StringUtils.capitalize(this.getJavaName());
        }
        return "get" + StringUtils.capitalize(this.getJavaName());
    }

    @Override
    public String getSetterName() {
        return "set" + StringUtils.capitalize(this.getJavaName());
    }




    @Override
    public String getJavaType() {
        String name = getJavaTypeClass().getName();
        if(name.startsWith("java.lang.")){
            return name.substring("java.lang.".length());
        }
        return name;
    }
}
