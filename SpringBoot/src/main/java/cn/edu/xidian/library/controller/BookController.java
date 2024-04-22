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
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private LendRecordMapper lendRecordMapper;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String isbn,
                              @RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "") String author){
        LambdaQueryWrapper<Book> wrappers = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(isbn)){
            wrappers.like(Book::getIsbn, isbn);
        }
        if(StringUtils.isNotBlank(name)){
            wrappers.like(Book::getName, name);
        }
        if(StringUtils.isNotBlank(author)){
            wrappers.like(Book::getAuthor, author);
        }
        wrappers.orderByDesc(Book::getBorrowNum);    //按借阅次数排序
        Page<Book> BookPage = bookMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        return Result.success(BookPage);
    }
    @PostMapping
    public Result<?> add(@RequestBody Book book){
        bookMapper.insert(book);
        return Result.success();
    }
    @PutMapping
    public Result<?> update(@RequestBody Book book){
        bookMapper.updateById(book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id){
        LambdaQueryWrapper<LendRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LendRecord::getBookId,id)
                .eq(LendRecord::getStatus,"on loan");
        if (lendRecordMapper.selectOne(wrapper) != null)
            return Result.error("-1","The book is on loan and can't be unloaded.");
        bookMapper.deleteById(id);
        return Result.success();
    }
    @DeleteMapping
    @Transactional
    public Result<?> deleteBatch(@RequestParam List<String> ids){
        for (String id:ids){
            LambdaQueryWrapper<LendRecord> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(LendRecord::getBookId,id)
                    .eq(LendRecord::getStatus,"on loan");
            if (lendRecordMapper.selectOne(wrapper) != null)
                return Result.error("-1","The book is on loan and can't be unloaded.");
        }
        bookMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
