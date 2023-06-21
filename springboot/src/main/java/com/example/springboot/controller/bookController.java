package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Book;
import com.example.springboot.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 小吴
 * @Date 2023/03/21 23:17
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/book")
public class bookController {

    @Autowired
    private BookMapper bookMapper;
    /**
     * 新增
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Book book){
        bookMapper.insert(book);
        return Result.success();
    }

    /**
     * 更新修改
     * @param book
     * @return
     */
    @PutMapping
    public Result update(@RequestBody Book book){
        bookMapper.updateById(book);
        return Result.success();
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        bookMapper.deleteById(id);
        return Result.success();
    }
    /**
     * 列表分页
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @GetMapping
    public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize,
                           @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if(StringUtils.isNotBlank(search)){
            wrapper.like(Book::getName,search);
        }
        Page<Book> animalPage = bookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(animalPage);
    }
}
