package norm;

/**
 * 提供了常用的jdbc driver名称。
 */
public interface JdbcDrivers {
    String MYSQL = "com.mysql.jdbc.Driver";
    String ORACLE = "oracle.jdbc.driver.OracleDriver";
    String DB2 = "com.ibm.db2.jcc.DB2Driver";
    String SYBASE = "com.sybase.jdbc.SybDriver";
    String POSTGRESQL = "org.postgresql.";
    String SQLSERVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    String JTDS = "net.sourceforge.jtds.jdbc.Driver";
    String INFORMIX = "com.informix.jdbc.IfxDriver";
    String ODBC = "sun.jdbc.odbc.JdbcOdbcDriver";
    String TERADATA = "com.ncr.teradata.TeraDriver";
    String NETEZZA = "org.netezza.Driver";
    String H2 = "org.h2.Driver";
    String SQLITE = "org.sqlite.JDBC";
    String DERBY = "org.apache.derby.jdbc.ClientDriver";
    String DERBY_EMBED = "org.apache.derby.jdbc.EmbeddedDriver";
}
