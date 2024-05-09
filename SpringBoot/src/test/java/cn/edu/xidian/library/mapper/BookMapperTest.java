package cn.edu.xidian.library.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookMapperTest {
    @Autowired
    BookInfoMapper bookInfoMapper;

    @Test
    public void Test(){
        System.out.println(bookInfoMapper.getList(1,10,"","The",""));
    }
}
