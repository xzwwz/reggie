package com.zwh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwh.domain.AddressBook;
import com.zwh.mapper.AddressBookMapper;
import com.zwh.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
