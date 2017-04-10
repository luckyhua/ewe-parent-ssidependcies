package mapper.mysql.bjdc.record.user;

import com.ewe.bjdc.domain.record.user.UserGrantRecord;
import com.ewe.bjdc.domain.record.user.UserGrantRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserGrantRecordMapper {
    int countByExample(UserGrantRecordExample example);

    int deleteByExample(UserGrantRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserGrantRecord record);

    int insertSelective(UserGrantRecord record);

    List<UserGrantRecord> selectByExample(UserGrantRecordExample example);

    UserGrantRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserGrantRecord record, @Param("example") UserGrantRecordExample example);

    int updateByExample(@Param("record") UserGrantRecord record, @Param("example") UserGrantRecordExample example);

    int updateByPrimaryKeySelective(UserGrantRecord record);

    int updateByPrimaryKey(UserGrantRecord record);
}