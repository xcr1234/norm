package norm.page;

public interface PageSql {

    PageModel buildSql(Page page, String sql);

    String database();
}
