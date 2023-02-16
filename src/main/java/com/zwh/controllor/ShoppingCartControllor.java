package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zwh.common.MyBaseContext;
import com.zwh.common.R;
import com.zwh.domain.ShoppingCart;
import com.zwh.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Transactional
@Slf4j
public class ShoppingCartControllor {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List> list(){
        Long currentId = MyBaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCarts);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        log.info(shoppingCart.toString());
        Long currentId = MyBaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
//        queryWrapper.eq(shoppingCart.getDishFlavor()!=null,ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        if(shoppingCart1!=null){
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartService.updateById(shoppingCart1);
        }
        else{
            shoppingCart.setUserId(currentId);
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCart1=shoppingCart;
        }

        return R.success(shoppingCart1);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        Long currentId = MyBaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        Integer number = cartServiceOne.getNumber();
        cartServiceOne.setNumber(number-1);
        if(number>1){
            shoppingCartService.updateById(cartServiceOne);
        }else{
            shoppingCartService.removeById(cartServiceOne);
        }

        return R.success(cartServiceOne);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,MyBaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("");
    }
}
