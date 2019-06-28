package cyf.gradle.dao.mapper;

import cyf.gradle.dao.model.Region;
import cyf.gradle.dao.model.RegionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RegionMapper {
    long countByExample(RegionExample example);

    int deleteByExample(RegionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Region record);

    int insertSelective(Region record);

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

    List<Region> selectByExample(RegionExample example);

    Region selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Region record, @Param("example") RegionExample example);

    int updateByExample(@Param("record") Region record, @Param("example") RegionExample example);

    int updateByPrimaryKeySelective(Region record);

    int updateByPrimaryKey(Region record);
}