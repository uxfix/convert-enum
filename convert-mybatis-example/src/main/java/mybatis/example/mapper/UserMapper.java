package mybatis.example.mapper;

import mybatis.example.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,gender,status) values (#{name},#{gender},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(User user);

    @Select("select id,name,gender,status from user where id = #{id}")
    User findUserById(@Param("id") Integer id);
}
