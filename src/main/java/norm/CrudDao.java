package norm;

import norm.exception.QueryException;
import norm.page.Page;

import java.util.List;

/**
 * <pre>
 * 支出数据库基本的增删改查的接口，建议所有查询接口（dao接口）都继承该接口。
 * 如果查询过程中发生异常，如sql异常，则统一抛出{@link QueryException}。
 * 如果参数错误（参数为null），则统一抛出 {@link IllegalArgumentException}
 * </pre>
 */
public interface CrudDao <T,ID>{

    /**
     * 将实体对象保存（insert）到数据库中，如果有自增ID则获取自增ID的值
     * @param t 待保存的对象，不可为null
     * @return 若保存成功则返回true，若不成功则返回false.
     */
    boolean save(T t);

    /**
     * 删除存在的java实体（根据id删除）
     * @param t 存在的java实体，不可为null
     * @return 是否删除成功
     */
    boolean delete(T t);

    /**
     * 删除存在的java实体（根据id删除）
     * @param id 存在的java实体的id，不可为null
     * @return 是否删除成功
     */
    boolean deleteByID(ID id);



    /**
     * 更新（update）一个实体对象
     * @param t 待更新的对象，不可为null
     */
    boolean update(T t);

    /**
     * 判断实体id是否在数据表中
     * @deprecated 请使用findOne或者count方法（传对象），这个方法只能传ID
     * @param id 实体对象的id
     * @return id是否在数据表
     */
    @Deprecated
    boolean exists(ID id);

    /**
     * 计算数据表中的总数
     * @return 总数量
     */
    int count(T o);

    /**
     * 根据id的值查询实体
     * @param id 实体对象的id
     * @return 返回查询到的实体，如果id不存在则返回null
     */
    T findOne(ID id);

    /**
     * 根据iqueryWrapper查询实体
     * @param queryWrapper 查询条件
     * @return 返回查询到的实体，如果不存在则返回null
     */
    T findOne(QueryWrapper queryWrapper);

    /**
     * 返回所有的实体List，返回值其实是一个{@link java.util.ArrayList}
     * @return 所有的实体List
     */
    List<T> findAll();

    /**
     * 返回所有的实体List，支持分页查询，返回值其实是一个{@link java.util.ArrayList}
     * @param page 分页查询，
     * @return 所有的实体List（已分页）
     */
    List<T> findAll(Page<T> page);

    /**
     * 返回所有的实体List，支持分页查询，参数做为查询条件，返回值其实是一个{@link java.util.ArrayList}
     * @param t 查询条件
     * @return
     */
    List<T> findAll(T t);

    /**
     * 返回所有的实体List，支持分页查询，参数做为查询条件，返回值其实是一个{@link java.util.ArrayList}
     * @param t 查询条件
     * @param page
     * @return
     */
    List<T> findAll(T t, Page<T> page);

    /**
     * 返回所有的实体List，通过条件查询，参数做为查询条件，返回值其实是一个{@link java.util.ArrayList}
     * @param queryWrapper 查询条件
     * @param page
     * @return
     */
    List<T> findAll(QueryWrapper queryWrapper, Page<T> page);

    /**
     * 返回所有的实体List，通过条件查询，参数做为查询条件，返回值其实是一个{@link java.util.ArrayList}
     * @param queryWrapper 查询条件
     * @return
     */
    List<T> findAll(QueryWrapper queryWrapper);

    JdbcTemplate<T> getJdbcTemplate();
}
