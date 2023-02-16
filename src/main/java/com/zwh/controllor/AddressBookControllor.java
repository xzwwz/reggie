package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zwh.common.MyBaseContext;
import com.zwh.common.R;
import com.zwh.domain.AddressBook;
import com.zwh.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookControllor {
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        Long currentId = MyBaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,currentId);
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List> list(){
        Long currentId = MyBaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,currentId);
        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        return R.success(addressBooks);
    }

    @PostMapping
    public R<String> add(@RequestBody AddressBook addressBook){
        Long currentId = MyBaseContext.getCurrentId();
        addressBook.setUserId(currentId);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,currentId);
        if(addressBookService.count(queryWrapper)==0){
            addressBook.setIsDefault(1);
        }else{
            addressBook.setIsDefault(0);
        }
        addressBookService.save(addressBook);
        return R.success("");
    }

    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook){
        addressBook.setIsDefault(1);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,MyBaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(queryWrapper);
        one.setIsDefault(0);
        addressBookService.updateById(one);
        addressBookService.updateById(addressBook);
        return R.success("");
    }
}
