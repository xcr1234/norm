package norm.page;

import norm.Databases;
import norm.util.Args;


import java.util.HashMap;
import java.util.Map;

/**
 * 分页查询管理器
 */
public final class Pages {
    private static Map<Databases,PageFactory> databasesPageFactoryMap = new HashMap<Databases, PageFactory>();
    private static Map<String,PageFactory> pageFactoryMap = new HashMap<String, PageFactory>();

    private static Databases databases;
    private static String name;

    static {
        databasesPageFactoryMap.put(Databases.DB2,new Db2PageFactory());
        databasesPageFactoryMap.put(Databases.Derby,new DerbyPageFactory());
        databasesPageFactoryMap.put(Databases.H2,new H2PageFactory());
        databasesPageFactoryMap.put(Databases.Oracle,new OraclePageFactory());
        databasesPageFactoryMap.put(Databases.MySQL,new MySQLPageFactory());
        databasesPageFactoryMap.put(Databases.PostgreSQL,new PostgreSQLPageFactory());
        databasesPageFactoryMap.put(Databases.SQLServer,new SQLServerPageFactory());

        pageFactoryMap.put("DB2",new Db2PageFactory());
        pageFactoryMap.put("Derby",new DerbyPageFactory());
        pageFactoryMap.put("H2",new H2PageFactory());
        pageFactoryMap.put("Oracle",new OraclePageFactory());
        pageFactoryMap.put("MySQL",new MySQLPageFactory());
        pageFactoryMap.put("PostgreSQL",new PostgreSQLPageFactory());
        pageFactoryMap.put("SQLServer",new SQLServerPageFactory());
    }

    public static Page create(int pageNumber, int pageSize){
        if(databases != null){
            return create(databases,pageNumber,pageSize);
        }
        if(name != null){
            return create(name,pageNumber,pageSize);
        }
        throw new IllegalStateException("can't create Page:Default databases hasn't been configured!");
    }


    public static void setDefaultDatabase(Databases defaultDatabase){
        Args.notNull(defaultDatabase,"default databases");
        databases = defaultDatabase;
    }

    public static void setDefaultDatabase(String defaultDatabase){
        if(defaultDatabase == null){
            throw new NullPointerException();
        }
        name = defaultDatabase;
    }


    public static Page create(Databases databases, int pageNumber, int pageSize){
        if(databases == null){
            throw new IllegalArgumentException("can't create Page:databases is null!");
        }
        PageFactory pageFactory = databasesPageFactoryMap.get(databases);
        if(pageFactory == null){
            throw new IllegalArgumentException("can't create Page:can't find PageFactory for databases:"+databases);
        }
        return pageFactory.create(pageNumber,pageSize);
    }


    public static Page create(String databases, int pageNumber, int pageSize){
        if(databases == null){
            throw new IllegalArgumentException("can't create Page:databases is null!");
        }
        PageFactory pageFactory = pageFactoryMap.get(databases);
        if(pageFactory == null){
            throw new IllegalArgumentException("can't create Page:can't find PageFactory for databases:"+databases);
        }
        return pageFactory.create(pageNumber,pageSize);
    }


    public static void register(Databases databases, PageFactory pageFactory){
        Args.notNull(databases,"databases");
        Args.notNull(pageFactory,"page factory");
        databasesPageFactoryMap.put(databases,pageFactory);
    }


    public static void register(String databases, PageFactory pageFactory){
        Args.notNull(databases,"databases");
        Args.notNull(pageFactory,"page factory");
        pageFactoryMap.put(databases,pageFactory);
    }
}
