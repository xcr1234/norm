package norm.result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 定义复杂自定义查询的查询结果，适用于多表查询
 * 用法：
 * 在{@link norm.anno.Query}注解自定义查询中时，将方法的返回值定义为QueryResult。
 *
 * 例如：
 * {@code @Query(sql="select A.id,A.name,B.id,B.name")}
 * QueryResult queryAB();
 *
 *
 * 该类可以提供从复杂查询中直接获得值的，或者直接获得Java Bean（通过Ognl表达式）的解决方案。
 *
 * 例子，执行上述的queryAB()方法后：
 * QueryResult result = dao.queryAB();
 * 这时，如果要获得A的List、B的List，应该使用ognl表达式构造。
 *
 * A、B的构造方法如下：
 * package my.bean;
 * public class A{
 *     public A(){}
 *     public A(int id,String name){
 *         this.id = id;
 *         this.name = name;
 *     }
 * }
 * package my.bean;
 * public class B{
 *     public B(){}
 *     public B(int id,String name){
 *         this.id = id;
 *         this.name = name;
 *     }
 * }
 *
 * 从queryList，获取List&lt;A&gt;、List&lt;B&gt;的方法是：
 *
 * 方法一，通过ognl表达式，调用构造方法构造
 * List&lt;A&gt; aList = (List&lt;A&gt; aList)result.toOgnlList("new my.bean.A(A.id,A.name)");
 * List&lt;B&gt; bList = (List&lt;B&gt; aList)result.toOgnlList("new my.bean.B(B.id,B.name)");
 *
 * 方法二，取出值，自己执行构造方法构造
 * List&lt;QueryResultItem&gt; itemList = result.toList();
 * List&lt;A&gt; aList = new ArrayList&lt;&gt;();
 * List&lt;B&gt; bList = new ArrayList&lt;&gt;();
 * for(QueryResultItem item : itemList){
 *     Integer aId = item.getInt("A.id");
 *     String aName = item.getString("A.name");
 *     aList.add(new A(aId,aName):
 *
 *     Integer bId = item.getInt("b.id");
 *     String bName = item.getString("b.name");
 *     bList.add(new B(bId,bName):
 * }
 *
 * 对于基本的多表查询，必须使用“表名.列名”的形式，才能获取到真实的值。
 *
 * 而对于复杂的查询，返回的结果集中往往不包含表名，这时直接使用列名，就可以获取到真实的值，比如：
 * select *,xxx as xxx from (select ...) where xxx
 * 这样的查询，结果集中是不包含表名的，因此可以直接使用列名得到真实的值。
 *
 * 如果顺序确定，也可以通过索引值获取，但该方式在ognl表达式中不支持。
 * </pre>
 */
public interface QueryResult {

    /**
     * 转换为QueryResultItem List，以获取每一项的值
     * @return QueryResultItem List
     */
    List<QueryResultItem> toList();

    /**
     * 转换为Java Bean的List，通过ognl表达式
     * @param ognl ognl表达式
     * @return Java Bean的List
     */
    List<?> toOgnlList(String ognl) ;

    /**
     * 转换为Java基本对象的Map List，按列
     * @return Java基本对象的Map
     */
    List<Map<String,Object>> toMapList();

    /**
     * 转换为Java基本对象的Map List，按索引
     * @return Java基本对象的Map
     */
    List<Map<Integer,Object>> toIndexMapList();

    /**
     * @return 本次查询结果的总行数
     */
    int size();





}
