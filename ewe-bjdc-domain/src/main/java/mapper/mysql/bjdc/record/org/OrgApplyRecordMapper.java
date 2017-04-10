package mapper.mysql.bjdc.record.org;

import com.ewe.bjdc.domain.record.org.OrgApplyRecord;
import com.ewe.bjdc.domain.record.org.OrgApplyRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrgApplyRecordMapper {
    int countByExample(OrgApplyRecordExample example);

    int deleteByExample(OrgApplyRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrgApplyRecord record);

    int insertSelective(OrgApplyRecord record);

    List<OrgApplyRecord> selectByExample(OrgApplyRecordExample example);

    OrgApplyRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrgApplyRecord record, @Param("example") OrgApplyRecordExample example);

    int updateByExample(@Param("record") OrgApplyRecord record, @Param("example") OrgApplyRecordExample example);

    int updateByPrimaryKeySelective(OrgApplyRecord record);

    int updateByPrimaryKey(OrgApplyRecord record);
}