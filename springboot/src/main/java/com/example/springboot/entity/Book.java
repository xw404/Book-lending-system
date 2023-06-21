package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @Author 小吴
 * @Date 2023/03/22 20:02
 * @Version 1.0
 */
@Data
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String kind;
    private String author;
    private String press;
    private String introductory;
    private Integer totalcount;
    private Integer surpluscount;
    private Integer weedingcount;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String createtime;
    private String picture;

}
