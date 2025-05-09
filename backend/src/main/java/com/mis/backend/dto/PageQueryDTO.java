package com.mis.backend.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class PageQueryDTO {
    private Integer page = 1;
    private Integer size = 5;
    public <T> Page<T> toMpPage(){
        page = page== null?1:page;
        size = size== null?5:size;
        return new Page<T>(page,size);
    }
}
