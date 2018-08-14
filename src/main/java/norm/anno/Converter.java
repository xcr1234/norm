package norm.anno;

import norm.convert.TypeConverter;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Converter {

    Class<? extends TypeConverter<?,?>> value();

    String init() default "";

}
