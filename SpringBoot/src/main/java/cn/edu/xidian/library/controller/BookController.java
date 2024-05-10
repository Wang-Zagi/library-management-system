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
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @GetMapping("{isbn}")
    public Result<?> get(@PathVariable String isbn){
        Book book = bookMapper.get(isbn);
        if (book == null)
            return Result.error("-1","The barcode isn't in library.");
        return Result.success(book);
    }

    @PostMapping
    public Result<?> add(@RequestBody Book book){
        if (bookMapper.selectById(book.getBarcode()) != null)
            return Result.error("-1","Add failed. This Barcode is already in data base.");
        bookMapper.insert(book);
        return Result.success();
    }
    @PutMapping("/{barcode}")
    @Transactional
    public Result<?> update(@PathVariable String barcode,@RequestBody Book book){
        if(!barcode.equals(book.getBarcode())){
            if (bookMapper.selectById(book.getBarcode()) != null)
                return Result.error("-1","Edit failed. This Barcode is already in data base.");
            bookMapper.deleteById(barcode);
            bookMapper.insert(book);
        }
        else bookMapper.updateById(book);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id){
        if (!bookMapper.selectById(id).getStatus().equals("in library"))
            return Result.error("-1","The book is not library and can't be unloaded.");
        bookMapper.deleteById(id);
        return Result.success();
    }
    @DeleteMapping
    @Transactional
    public Result<?> deleteBatch(@RequestParam List<String> ids){
        for (String id:ids){
            if (!bookMapper.selectById(id).getStatus().equals("in library"))
                return Result.error("-1","The book is not library and can't be unloaded.");
        }
        bookMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
