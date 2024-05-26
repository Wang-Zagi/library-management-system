package cn.edu.xidian.library.mapper;

import cn.edu.xidian.library.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    UserMapper mapper;

    @Test
    void clearFineTest(){
        mapper.clearFine(7);
    }
}
