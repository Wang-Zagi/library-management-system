package cn.edu.xidian.library.controller;

import cn.edu.xidian.library.commom.Result;
import cn.edu.xidian.library.entity.Book;
import cn.edu.xidian.library.entity.BorrowRecord;
import cn.edu.xidian.library.mapper.BookMapper;
import cn.edu.xidian.library.mapper.BorrowRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/borrowRecord")
public class BorrowRecordController {
    @Resource
    BorrowRecordMapper borrowRecordMapper;
    @Resource
    private BookMapper bookMapper;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(required = false) String borrowerId,
                              @RequestParam(required = false) String borrowerName,
                              @RequestParam(required = false) String bookBarcode,
                              @RequestParam(required = false) String bookName,
                              @RequestParam(required = false) String status){
        LambdaQueryWrapper<BorrowRecord> wrappers = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(borrowerId))
            wrappers.eq(BorrowRecord::getBorrowerId, borrowerId);
        if(StringUtils.isNotBlank(borrowerName))
            wrappers.eq(BorrowRecord::getBorrowerName, borrowerName);
        if(StringUtils.isNotBlank(bookBarcode))
            wrappers.like(BorrowRecord::getBookBarcode, bookBarcode);
        if(StringUtils.isNotBlank(bookName))
            wrappers.like(BorrowRecord::getBookName, bookName);
        if(StringUtils.isNotBlank(status))
            wrappers.like(BorrowRecord::getStatus, status);
        wrappers.orderByDesc(BorrowRecord::getBorrowTime);    //按照借阅时间最新在前排序
        Page<BorrowRecord> BorrowRecordPage = borrowRecordMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(BorrowRecordPage);
    }
    @PostMapping
    @Transactional
    public Result<?> borrow(@RequestBody BorrowRecord borrowRecord){
        borrowRecordMapper.insert(borrowRecord);
        Book book =bookMapper.selectById(borrowRecord.getBookBarcode());
        bookMapper.updateById(book);
        return Result.success();
    }
    @PutMapping
    @Transactional
    public  Result<?> update(@RequestBody BorrowRecord borrowRecord){
        BorrowRecord oldBorrowRecord = borrowRecordMapper.selectById(borrowRecord.getId());
        borrowRecordMapper.updateById(borrowRecord);
        if(oldBorrowRecord.getStatus().equals("borrowed")&& borrowRecord.getStatus().equals("returned")){
            Book book =bookMapper.selectById(borrowRecord.getBookBarcode());
            System.out.println(book);
            bookMapper.updateById(book);
        }
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        BorrowRecord record= borrowRecordMapper.selectById(id);
        if(record.getStatus().equals("borrowed"))
            return Result.error("-1","The book was not returned and the record could not be deleted.");
        borrowRecordMapper.deleteById(id);
        return Result.success();
    }
    @DeleteMapping
    @Transactional
    public Result<?> deleteBatch(@RequestParam List<Long> ids){
        for (Long id:ids){
            BorrowRecord record= borrowRecordMapper.selectById(id);
            if(record.getStatus().equals("borrowed"))
                return Result.error("-1","The book was not returned and the record could not be deleted.");
        }
        borrowRecordMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
