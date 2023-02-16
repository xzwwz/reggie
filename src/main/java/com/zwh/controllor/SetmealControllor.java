package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwh.common.R;
import com.zwh.domain.Category;
import com.zwh.domain.Setmeal;
import com.zwh.domain.SetmealDish;
import com.zwh.dto.SetmealDto;
import com.zwh.service.CategoryService;
import com.zwh.service.SetmealDishService;
import com.zwh.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/setmeal")
@RestController
@Transactional
@Slf4j
public class SetmealControllor {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<SetmealDto>> setmealList(int page, int pageSize, String name) {
//        Page<Setmeal> page1 = new Page<>(page,pageSize);
//        Page<SetmealDto> page2 = new Page<>(page,pageSize);
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        if(name!=null){
//            queryWrapper.like(Setmeal::getName,name);
//        }
//        setmealService.page(page1,queryWrapper);
////        BeanUtils.copyProperties(page1,page2,"records");
////        setmealService.page(page1,queryWrapper);
//        List<Setmeal> records = page1.getRecords();
//        List<SetmealDto> dtoList = records.stream().map((item) -> {
//            SetmealDto setmealDto = new SetmealDto();
//            BeanUtils.copyProperties(item, setmealDto);
//            Long categoryId = setmealDto.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//            if (category != null) {
//                setmealDto.setCategoryName(category.getName());
//            }
//            return setmealDto;
//        }).collect(Collectors.toList());
//        page2.setRecords(dtoList);
//        return R.success(page2);

//        Page<Setmeal> page1 = new Page<>(page,pageSize);
        Page<SetmealDto> page2 = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Setmeal::getName, name);
        }
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        List<SetmealDto> dtoList = setmealList.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = setmealDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        page2.setRecords(dtoList);
        return R.success(page2);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealById(@PathVariable Long id) {
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmealService.getById(id), setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateById(setmealDto);
        Long setmealDtoId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDtoId);
        setmealDishService.remove(queryWrapper);
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealDtoId);
            setmealDishService.save(setmealDish);
        }
        return R.success("成功修改");
    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.save(setmealDto);
        Long id = setmealDto.getId();
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(id);
            setmealDishService.save(setmealDish);
        }
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<String> deleteSetmeal(Long[] ids) {
        int len = ids.length;
        for (int i = 0; i < len; i++) {
            Long setmealId = ids[i];
            LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Setmeal::getId, setmealId);
            setmealService.remove(queryWrapper1);
            LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(SetmealDish::getSetmealId, setmealId);
            setmealDishService.remove(queryWrapper2);
        }
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> setStatus(@PathVariable int status, Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Long setmealId = ids[i];
            Setmeal setmeal = setmealService.getById(setmealId);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List> list(Setmeal setmeal){
//        log.info("id : {},status : {}",categoryId,status);
        if(setmeal==null)return R.error("error");
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        return R.success(setmeals);
    }
}
