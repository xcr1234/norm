package norm.page;

/**
 * {@link Page}接口的工厂类
 */
public interface PageFactory {

    /**
     * 创建一个Page接口的实现类
     * @param pageNumber 页码，从1开始。
     * @param pageSize 页面大小，(pageSize &gt; 0)
     * @return 已创建的Page Request。
     */
    Page create(int pageNumber, int pageSize);


}
