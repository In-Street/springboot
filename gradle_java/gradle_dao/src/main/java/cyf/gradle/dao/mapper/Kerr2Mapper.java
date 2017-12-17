package cyf.gradle.dao.mapper;

import cyf.gradle.dao.model.Kerr2;
import cyf.gradle.dao.model.Kerr2Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface Kerr2Mapper {
    long countByExample(Kerr2Example example);

    int deleteByExample(Kerr2Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(Kerr2 record);

    int insertSelective(Kerr2 record);

    int selfPlusMinus(@Param("columnName") String columnName, @Param("operator") String operator, @Param("count") int count, @Param("example") Object example);

    List<Kerr2> selectByExample(Kerr2Example example);

    Kerr2 selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Kerr2 record, @Param("example") Kerr2Example example);

    int updateByExample(@Param("record") Kerr2 record, @Param("example") Kerr2Example example);

    int updateByPrimaryKeySelective(Kerr2 record);

    int updateByPrimaryKey(Kerr2 record);
}