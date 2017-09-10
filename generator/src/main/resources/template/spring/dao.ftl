
<#-- @ftlvariable name="entity" type="norm.generator.Entity" -->
package ${entity.basePackage}.entity;


import norm.CrudDao;
import org.springframework.stereotype.Repository;
import ${entity.basePackage}.entity.${entity.name};
@Repository
public interface ${entity.name}Dao extends CrudDao<${entity.name} , ${entity.idColumn.javaType}> {



    ${entity.name} save(${entity.name} t);


    boolean delete(${entity.name} t);

    /**
     * 删除存在的java实体（根据id删除）
     * @param id 存在的java实体的id，不可为null
     * @return 是否删除成功
     */
    boolean deleteByID(ID id);

    /**
     * 删除数据表的全部内容
     * @return 删除成功的数量，失败返回0
     */
    int deleteAll();


    /**
     * 更新（update）一个实体对象
     * @param t 待更新的对象，不可为null
     */
    void update(T t);

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
    int count();

    /**
     * 根据id的值查询实体
     * @param id 实体对象的id
     * @return 返回查询到的实体，如果id不存在则返回null
     */
    T findOne(ID id);

    /**
     * 返回所有的实体List，返回值其实是一个{@link java.util.ArrayList}
     * @return 所有的实体List
     */
    List<T> findAll();

    /**
     * 返回所有的实体List，支持分页查询，返回值其实是一个{@link java.util.ArrayList}
     * @param page 分页查询，建议使用{@link norm.page.Pages#create(Databases, int, int)}实现类。
     * @return 所有的实体List（已分页）
     */
    List<T> findAll(Page page);


}