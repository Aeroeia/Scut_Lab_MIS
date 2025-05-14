package com.mis.backend.vo;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryVO<T> {
    private Integer total;
    private Integer pages;
    private Integer current;
    private Integer size;
    private List<T> records;

    public static <PO,VO> PageQueryVO<VO> of(Page<PO> page, Function<PO,VO> convertor){
        PageQueryVO<VO> pageQueryVO = new PageQueryVO<>();
        pageQueryVO.setPages((int)page.getPages());
        pageQueryVO.setTotal((int)page.getTotal());
        pageQueryVO.setSize((int)page.getSize());
        pageQueryVO.setCurrent((int)page.getCurrent());
        List<PO> records = page.getRecords();
        if(CollUtil.isEmpty(records)){
            pageQueryVO.setRecords(List.of());
            return pageQueryVO;
        }
        List<VO> collect = records.stream().map(convertor).collect(Collectors.toList());
        pageQueryVO.setRecords(collect);
        return pageQueryVO;
    }
    public static <VO> PageQueryVO<VO> of(){
       return PageQueryVO.<VO>builder()
                .current(0)
                .pages(0)
                .records(List.<VO>of())
                .total(0)
                .size(0)
                .build();
    }
}
