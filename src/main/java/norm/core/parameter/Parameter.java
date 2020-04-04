package norm.core.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Parameter {
    void setParameter(PreparedStatement ps,int index) throws SQLException;
    Object getValue();
    String getName();
}
