package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwh.common.CustomException;
import com.zwh.common.MyBaseContext;
import com.zwh.common.R;
import com.zwh.domain.*;
import com.zwh.dto.OrdersDto;
import com.zwh.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import wiremock.com.github.jknack.handlebars.Lambda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Transactional
@Slf4j
public class OrdersControllor {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDetailService ordersDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/page")
    public R<Page> pgaeOrder(int page, int pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(number!=null){
            ordersLambdaQueryWrapper.like(Orders::getNumber,number);
        }
        ordersLambdaQueryWrapper.ge(beginTime!=null,Orders::getOrderTime,beginTime);
        ordersLambdaQueryWrapper.le(endTime!=null,Orders::getCheckoutTime,endTime);
        Page<Orders> page1 =new Page<>(page,pageSize);
        ordersService.page(page1,ordersLambdaQueryWrapper);
        return R.success(page1);
    }


    @PostMapping("/submit")
    public R<String> submit(@RequestBody OrdersDto ordersDto){
        log.info(ordersDto.toString());
        ordersDto.setUserId(MyBaseContext.getCurrentId());
        ordersDto.setOrderTime(LocalDateTime.now());
        ordersDto.setCheckoutTime(LocalDateTime.now());

        Long userId = MyBaseContext.getCurrentId();
        User user = userService.getById(userId);

        AddressBook addressBook = addressBookService.getById(ordersDto.getAddressBookId());
        if(addressBook==null){
            throw new CustomException("地址信息有误");
        }


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        if(list==null||list.isEmpty()){
            throw new CustomException("购物栏为空");
        }

        ordersDto.setAddress(
                (addressBook.getProvinceName()==null?"":addressBook.getProvinceName())
                + (addressBook.getCityName()==null?"":addressBook.getCityName())
                + (addressBook.getDistrictName()==null?"":addressBook.getDistrictName())
                + addressBook.getDetail()
        );
        ordersDto.setUserId(userId);
        ordersDto.setUserName(user.getName());
        ordersDto.setPhone(addressBook.getPhone());
        ordersDto.setConsignee(addressBook.getConsignee());
        ordersDto.setStatus(2);



        AtomicInteger amount = new AtomicInteger(0);

        ordersDto.setAmount(new BigDecimal(String.valueOf(amount)));
        ordersService.save(ordersDto);

        List<OrderDetail> details = list.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(ordersDto.getId());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setName(item.getName());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        ordersDto.setAmount(new BigDecimal(String.valueOf(amount)));
        ordersService.updateById(ordersDto);
        ordersDetailService.saveBatch(details);

        shoppingCartService.remove(queryWrapper);
        return R.success("");
    }

    @PutMapping
    public R<String> editStatus(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return R.success("");
    }
}
