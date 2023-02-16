package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwh.common.R;
import com.zwh.domain.Category;
import com.zwh.domain.Dish;
import com.zwh.domain.DishFlavor;
import com.zwh.dto.DishDto;
import com.zwh.service.CategoryService;
import com.zwh.service.DishFlavorService;
import com.zwh.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
@Transactional//保证事物一致性
public class DishControllor {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取全部菜品信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> listDish(int page, int pageSize, String name) {
        Page<DishDto> page1 = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Dish::getName, name);
        }
        List<Dish> dishList = dishService.list(queryWrapper);
        List<DishDto> list = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(dishDto.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        page1.setRecords(list);
        return R.success(page1);
    }

    /**
     * 根据id获取菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id) {
        DishDto dishdto = new DishDto();
        BeanUtils.copyProperties(dishService.getById(id), dishdto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        dishdto.setFlavors(dishFlavorService.list(queryWrapper));
        return R.success(dishdto);
    }

    /**
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(Long[] ids) {
        int len = ids.length;
        for (int i = 0; i < len; i++) {
            Long dishId = ids[i];
            LambdaQueryWrapper<Dish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Dish::getId, dishId);
            dishService.remove(queryWrapper1);
//            dishService.removeById(dishDto);
            LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(DishFlavor::getDishId, dishId);
            dishFlavorService.remove(queryWrapper2);
        }
        return R.success("删除成功");
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> editDish(@RequestBody DishDto dishDto) {
        dishService.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        Long dishId = dishDto.getId();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(queryWrapper);
        for (DishFlavor dishFlavor : dishDto.getFlavors()) {
            dishFlavor.setDishId(dishId);
            dishFlavorService.save(dishFlavor);
        }
        return R.success("修改成功");
    }

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        dishService.save(dishDto);
        Long dishId = dishDto.getId();
        for (DishFlavor dishFlavor : dishDto.getFlavors()) {
            dishFlavor.setDishId(dishId);
            dishFlavorService.save(dishFlavor);
        }
        return R.success("添加成功");
    }

    /**
     * 改变菜品状态
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> editStatus(@PathVariable int status, Long[] ids) {
//        log.info(id.toString());
        Long id;
        int len = ids.length;
//        log.info(String.valueOf(len));
        for (int i = 0; i < len; i++) {
            id = ids[i];
//            log.info(id.toString());
//            log.info(id.toString());
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getDishByCategoryId(Dish dish) {
//        log.info(categoryId.toString());
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        if (dish.getCategoryId() != null) {
            queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
            queryWrapper.eq(Dish::getStatus,dish.getStatus());
        } else {
            queryWrapper.like(Dish::getName, dish.getName());
        }
        List<Dish> dishes = dishService.list(queryWrapper);
        List<DishDto> dishDtos = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long dishId = dishDto.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dishId);
            dishDto.setFlavors(dishFlavorService.list(queryWrapper1));
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }

}
