package cn.edu.xidian.library.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IncomeMapper {
    @Insert("insert into income_record values (#{id},#{payerId},#{amount},#{payTime})")
    void insert(String id, Integer payerId, Integer amount, String payTime);
}
