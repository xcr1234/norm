package norm.page;

import norm.page.impl.*;
import norm.util.AssertUtils;

import java.util.HashMap;
import java.util.Map;

public class PageUtil {
    private static Map<String, PageSql> pageSqlMap = new HashMap<String,PageSql>();

    public static PageSql getPageSql(String databaseProductName){
        return pageSqlMap.get(databaseProductName);
    }

    public static void putPageSql(String databaseProductName,PageSql pageSql ){
        AssertUtils.notNull(databaseProductName,"the name of database product");
        AssertUtils.notNull(pageSql,"the page sql");
        pageSqlMap.put(databaseProductName,pageSql);
    }

    static {
        pageSqlMap.put("H2",new H2Page());
        pageSqlMap.put("MySQL",new MySQLPage());
        pageSqlMap.put("Oracle",new OraclePage());
        pageSqlMap.put("PostgreSQL",new PostgreSQLPage());
        pageSqlMap.put("Microsoft SQL Server",new SQLServerPage());
        Db2Page db2Page = new Db2Page();
        pageSqlMap.put("DB2",db2Page);
        pageSqlMap.put("DB2/NT",db2Page);
        pageSqlMap.put("DB2/NT64",db2Page);
        pageSqlMap.put("DB2 UDP",db2Page);
        pageSqlMap.put("DB2/LINUX",db2Page);
        pageSqlMap.put("DB2/LINUX390",db2Page);
        pageSqlMap.put("DB2/LINUXX8664",db2Page);
        pageSqlMap.put("DB2/LINUXZ64",db2Page);
        pageSqlMap.put("DB2/LINUXPPC64",db2Page);
        pageSqlMap.put("DB2/LINUXPPC64LE",db2Page);
        pageSqlMap.put("DB2/400 SQL",db2Page);
        pageSqlMap.put("DB2/6000",db2Page);
        pageSqlMap.put("DB2 UDB iSeries",db2Page);
        pageSqlMap.put("DB2/AIX64",db2Page);
        pageSqlMap.put("DB2/HPUX",db2Page);
        pageSqlMap.put("DB2/HP64",db2Page);
        pageSqlMap.put("DB2/SUN",db2Page);
        pageSqlMap.put("DB2/SUN64",db2Page);
        pageSqlMap.put("DB2/PTX",db2Page);
        pageSqlMap.put("DB2/2",db2Page);
        pageSqlMap.put("DB2 UDB AS400", db2Page);
    }
}
