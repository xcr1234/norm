package norm.generator;

import java.util.List;

public interface Entity {

    String getTableName();

    String getName();

    Column getIdColumn();

    List<Column> getColumns();

    String getBasePackage();

}
