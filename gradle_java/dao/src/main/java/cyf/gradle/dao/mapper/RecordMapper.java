package cyf.gradle.dao.mapper;

import cyf.gradle.dao.model.Record;
import cyf.gradle.dao.model.RecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RecordMapper {
    long countByExample(RecordExample example);

    int deleteByExample(RecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Record record);

    int insertSelective(Record record);

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

    List<Record> selectByExample(RecordExample example);

    Record selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Record record, @Param("example") RecordExample example);

    int updateByExample(@Param("record") Record record, @Param("example") RecordExample example);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    @Select("select * from record")
    List<Record> getRecordList();

}