package org.norm.anno;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OrderBy {
    String value();

    Type type();

    enum Type{
        ASC,DESC;
    }
}
