package com.zwh.controllor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zwh.common.R;
import com.zwh.domain.User;
import com.zwh.service.UserService;
import com.zwh.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Transactional
@Slf4j
public class UserControllor {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
//            try {
//                SMSUtils.senMsg(phone, code);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            httpSession.setAttribute(phone,code);
            return R.success("发送成功");
        }
        return R.error("发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
//        log.info("{},{}",phone,code);
        String phone = map.get("phone").toString();
        String code = map.get("code".toString()).toString();
        Object codeInSession = session.getAttribute(phone);
        if(codeInSession!=null&&codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);

            if(user==null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
