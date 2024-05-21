package cn.edu.xidian.library.utils;

import cn.edu.xidian.library.entity.BorrowRecord;
import cn.edu.xidian.library.entity.User;
import cn.edu.xidian.library.mapper.BorrowRecordMapper;
import cn.edu.xidian.library.mapper.UserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class Debt {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    private void updateDebt() {
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getRole, 2));
        for (User user : users) {
            Integer id = user.getId();
            List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(Wrappers.<BorrowRecord>lambdaQuery()
                    .eq(BorrowRecord::getBorrowerId, id)
                    .eq(BorrowRecord::getStatus, "borrowing"));
            int debt = 0;
            for (BorrowRecord borrowRecord : borrowRecords) {
                Long days = ChronoUnit.DAYS.between(borrowRecord.getBorrowTime().toInstant(), new Date().toInstant());
                if (days >= 0) {
                    debt += 1;// 假设每逾期一天，欠款增加1
                }
                user.setDebt(debt);
                userMapper.updateById(user);
            }

        }
    }
}
