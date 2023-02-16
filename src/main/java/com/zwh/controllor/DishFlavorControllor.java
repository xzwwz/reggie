package com.zwh.controllor;

import com.zwh.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dishflavor")
@Transactional
public class DishFlavorControllor {
    @Autowired
    private DishFlavorService dishFlavorService;
}
