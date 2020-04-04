package norm.core.query;

import norm.core.meta.ColumnMeta;

public class ReturnGenerateId {
    private ColumnMeta idColumn;
    private Object target;

    public ColumnMeta getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(ColumnMeta idColumn) {
        this.idColumn = idColumn;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
