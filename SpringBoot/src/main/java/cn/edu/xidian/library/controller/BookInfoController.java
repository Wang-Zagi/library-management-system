package cn.edu.xidian.library.controller;

import cn.edu.xidian.library.commom.Result;
import cn.edu.xidian.library.entity.Book;
import cn.edu.xidian.library.entity.BookInfo;
import cn.edu.xidian.library.entity.BorrowRecord;
import cn.edu.xidian.library.mapper.BookInfoMapper;
import cn.edu.xidian.library.mapper.BookMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookInfo")
public class BookInfoController {
    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private BookMapper bookMapper;
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String isbn,
                              @RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "") String author){
        return Result.success(bookInfoMapper.getList(pageNum,pageSize,isbn,name,author));
    }

    @GetMapping("total")
    public Result<?> getTotal(@RequestParam(defaultValue = "") String isbn,
                              @RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "") String author){
        return Result.success(bookInfoMapper.getTotal(isbn,name,author));
    }

    @PostMapping
    public Result<?> add(@RequestBody BookInfo bookInfo){
        if (bookInfoMapper.selectById(bookInfo.getIsbn()) != null)
            return Result.error("-1","Add failed. This ISBN is already in data base.");
        bookInfoMapper.insert(bookInfo);
        return Result.success();
    }

    @PutMapping("/{isbn}")
    public Result<?> update(@PathVariable String isbn,@RequestBody BookInfo bookInfo){
        if(!isbn.equals(bookInfo.getIsbn())){
            if (bookInfoMapper.selectById(bookInfo.getIsbn()) != null)
                return Result.error("-1","Edit failed. This ISBN is already in data base.");
            bookInfoMapper.deleteById(isbn);
            bookInfoMapper.insert(bookInfo);
        }
        else bookInfoMapper.updateById(bookInfo);
        return Result.success();
    }

    @DeleteMapping("/{isbn}")
    public Result<?> delete(@PathVariable String isbn){
        LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Book::getIsbn,isbn);
        if (!bookMapper.selectObjs(wrapper).isEmpty())
            return Result.error("-1","Please remove all associated book entities first.");
        bookInfoMapper.deleteById(isbn);
        return Result.success();
    }

    @DeleteMapping
    @Transactional
    public Result<?> deleteBatch(@RequestParam List<String> ids){
        for (String id:ids){
            LambdaQueryWrapper<Book> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Book::getIsbn,id);
            if (!bookMapper.selectObjs(wrapper).isEmpty())
                return Result.error("-1","Please remove all associated book entities first.");
        }
        bookInfoMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
