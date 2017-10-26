package norm.join;

import norm.impl.ColumnMeta;
import norm.impl.Meta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface JoinMeta {

    List<ColumnMeta> getColumns();

    List<Meta> getMetas();

    Map<ColumnMeta,ColumnMeta> getOnMetas();

}
