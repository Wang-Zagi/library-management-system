package cn.edu.xidian.library.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookMapperTest {
    @Autowired
    BookInfoMapper bookInfoMapper;
    @Autowired
    BookMapper bookMapper;

    @Test
    public void Test(){
        System.out.println(bookInfoMapper.getList(1,10,"","",""));
//        System.out.println(bookMapper.get("00001"));
    }
}
