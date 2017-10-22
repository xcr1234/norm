package norm.page;

public interface PageSql {
    String buildSql(Page page, String sql);

    String database();
}
