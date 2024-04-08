package cn.edu.xidian.library.controller;

import cn.edu.xidian.library.commom.Result;
import cn.edu.xidian.library.entity.Book;
import cn.edu.xidian.library.entity.LendRecord;
import cn.edu.xidian.library.mapper.BookMapper;
import cn.edu.xidian.library.mapper.LendRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/lendRecord")
public class LendRecordController {
    @Resource
    LendRecordMapper lendRecordMapper;
    @Resource
    private BookMapper bookMapper;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(required = false) String borrowerId,
                              @RequestParam(required = false) String borrowerName,
                              @RequestParam(required = false) String bookId,
                              @RequestParam(required = false) String bookName,
                              @RequestParam(required = false) String status){
        LambdaQueryWrapper<LendRecord> wrappers = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(borrowerId))
            wrappers.eq(LendRecord::getBorrowerId, borrowerId);
        if(StringUtils.isNotBlank(borrowerName))
            wrappers.eq(LendRecord::getBorrowerName, borrowerName);
        if(StringUtils.isNotBlank(bookId))
            wrappers.like(LendRecord::getBookId, bookId);
        if(StringUtils.isNotBlank(bookName))
            wrappers.like(LendRecord::getBookName, bookName);
        if(StringUtils.isNotBlank(status))
            wrappers.like(LendRecord::getStatus, status);
        wrappers.orderByDesc(LendRecord::getLendTime);    //按照借阅时间最新在前排序
        Page<LendRecord> LendRecordPage = lendRecordMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(LendRecordPage);
    }
    @PostMapping
    @Transactional
    public Result<?> lend(@RequestBody LendRecord lendRecord){
        lendRecordMapper.insert(lendRecord);
        Book book=bookMapper.selectById(lendRecord.getBookId());
        book.setStatus("on loan");
        book.setBorrowNum(book.getBorrowNum()+1);
        bookMapper.updateById(book);
        return Result.success();
    }
    @PutMapping
    @Transactional
    public  Result<?> update(@RequestBody LendRecord lendRecord){
        LendRecord oldLendRecord=lendRecordMapper.selectById(lendRecord.getId());
        lendRecordMapper.updateById(lendRecord);
        if(oldLendRecord.getStatus().equals("on loan")&&lendRecord.getStatus().equals("returned")){
            Book book=bookMapper.selectById(lendRecord.getBookId());
            System.out.println(book);
            book.setStatus("in library");
            bookMapper.updateById(book);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        LendRecord record=lendRecordMapper.selectById(id);
        if(record.getStatus().equals("on loan"))
            return Result.error("-1","该记录未归还，无法删除");
        lendRecordMapper.deleteById(id);
        return Result.success();
    }
    @DeleteMapping
    @Transactional
    public Result<?> deleteBatch(@RequestParam List<Long> ids){
        for (Long id:ids){
            LendRecord record=lendRecordMapper.selectById(id);
            if(record.getStatus().equals("on loan"))
                return Result.error("-1","该记录未归还，无法删除");
        }
        lendRecordMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
