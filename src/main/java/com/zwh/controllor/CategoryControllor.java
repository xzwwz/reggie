package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwh.common.R;
import com.zwh.domain.Category;
import com.zwh.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@Transactional
public class CategoryControllor {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page<Category>> listCategory(int page, int pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        Page<Category> page1 = new Page<>(page, pageSize);
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(page1, queryWrapper);
        return R.success(page1);
    }

    @PostMapping
    public R<String> addCategory(HttpServletRequest request, @RequestBody Category category) {
//        log.info(category.toString());
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(empId);
//        category.setUpdateUser(empId);
        categoryService.save(category);
        return R.success("添加成功");
    }

    @PutMapping
    public R<String> editCategory(HttpServletRequest request, @RequestBody Category category) {
        log.info(category.toString());
//        Category category1 = categoryService.getById(category.getId());
//        category1.setName(category.getName());
//        category1.setSort(category.getSort());
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        category1.setUpdateTime(LocalDateTime.now());
//        category1.setUpdateUser(empId);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteCategory(Long ids) {
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> getCategoryListByType(/*@RequestBody */Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getName);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
