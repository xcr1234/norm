package norm;

import norm.convert.TypeConverter;

public class ConvertException extends QueryException {

    private TypeConverter typeConverter;
    private Object value;


    public ConvertException(TypeConverter typeConverter,Object value,Throwable t){
        super("failed to convert [" + value + "] by converter [" + typeConverter + "]",t);
        this.typeConverter = typeConverter;
        this.value = value;
    }
}
