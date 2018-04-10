package cyf.gradle.dao.mapper;

import cyf.gradle.dao.model.Kerr;
import cyf.gradle.dao.model.KerrExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface KerrMapper {
    long countByExample(KerrExample example);

    int deleteByExample(KerrExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Kerr record);

    int insertSelective(Kerr record);

    int selfPlusMinus(@Param("columnName") String columnName, @Param("operator") String operator, @Param("count") int count, @Param("example") Object example);

    int selfPlusMinusByPrimaryKey(@Param("columnName") String columnName, @Param("operator") String operator, @Param("count") int count, @Param("id") int id);

    /**
        eg:
        fieldMap 名称固定
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("reply_num", "+1");
        fieldMap.put("comments_num", "+1");

        Map<String, Object> paramMap = new HashMap<>();
        params.put("fieldMap", fieldMap);
        params.put("id", id);
*/
    int multiplePlusMinusByPrimaryKey(java.util.Map<String, Object> paramMap);

    List<Kerr> selectByExample(KerrExample example);

    Kerr selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Kerr record, @Param("example") KerrExample example);

    int updateByExample(@Param("record") Kerr record, @Param("example") KerrExample example);

    int updateByPrimaryKeySelective(Kerr record);

    int updateByPrimaryKey(Kerr record);
}