package norm;

import norm.page.Page;

import java.util.List;

public interface BaseService<T,ID> {
    /**
     * 将实体对象保存（insert）到数据库中，如果有自增ID则获取自增ID的值
     * @param t 待保存的对象，不可为null
     * @return 若保存成功则返回该对象（对自增主键会自动处理），若不成功则返回null.
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
     * @param id 实体对象的id
     * @return id是否在数据表
     */
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
    List<T> findAll(Page page);

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
    List<T> findAll(T t,Page page);

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
}
