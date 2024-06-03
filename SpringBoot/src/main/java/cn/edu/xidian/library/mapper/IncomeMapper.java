package cn.edu.xidian.library.mapper;

import cn.edu.xidian.library.entity.IncomeRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IncomeMapper {
    @Insert("insert into income_record values (#{id},#{payerId},#{amount},#{payTime})")
    void insert(String id, Integer payerId, Integer amount, String payTime);
    @Select("select amount, pay_time from income_record")
    List<IncomeRecord> selectList(Object o);
    @Select("select sum(amount) from income_record")
    Integer getTotal();
}
