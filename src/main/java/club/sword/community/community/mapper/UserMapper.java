package club.sword.community.community.mapper;

import club.sword.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //与数据库直接打交道，但是不需要写数据库连接语句
    //由MyBatis实现了与数据库的连接
    /*
    MyBatis它是一个基于Java的持久层框架的，且内部封装了 JDBC，
    这使开发者只需要关注 sql 语句本身，
    而不需要花费精力去处理加载驱动、创建连接、创建 statement 等等这么繁琐的过程
     */
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Long id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name = #{name}, gmt_modified = #{gmtModified},token = #{token}, avatar_url = #{avatarUrl} where account_id = #{accountId}")
    void updateByAccountId(User dbUser);

    @Select("select * from user where id = #{id}")
    User selectByPrimaryKey(@Param("id") Long creator);
}
