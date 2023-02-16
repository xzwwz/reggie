package com.zwh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwh.domain.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
