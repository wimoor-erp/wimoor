package com.wimoor.shopee.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.shopee.common.Result;
import com.wimoor.shopee.entity.User;
import com.wimoor.shopee.mapper.UserMapper;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;

    @PostMapping("/add")
    public Result<?> saveAction(@RequestBody User user){
    	LambdaQueryWrapper<User> wapper = Wrappers.<User>lambdaQuery();
    	wapper.eq(User::getUsername, user.getUsername());
    	List<Object> list = userMapper.selectObjs(wapper);
    	if(list!=null && list.size()>0) {
    		 return Result.error("-1","插入失败！已存在相同的账户名！");
    	}
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        int res=userMapper.insert(user);
        if(res>0){
            return Result.success();
        }else{
            return Result.error("500","插入失败！");
        }
    }

    @GetMapping("/getUserList")
    public Result<?> getUserListAction(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wapper = Wrappers.<User>lambdaQuery();
        if(StringUtils.isNotEmpty(search)){
            search="%"+(search.trim())+"%";
            wapper.like(User::getName,search);
        }
        Page<User> list=  userMapper.selectPage(new Page<>(pageNum, pageSize), wapper);
        return Result.success(list);
    }

    @PostMapping("/update")
    public Result<?> updateAction(@RequestBody User user){
        if(user.getPassword()==null){
            user.setPassword("123456");
        }
        int res=userMapper.updateById(user);
        if(res>0){
            return Result.success();
        }else{
            return Result.error("500","更新失败！");
        }
    }

    @PostMapping("/deleteUser")
    public Result<?> deleteAction(@RequestBody User user){
        int res=userMapper.deleteById(user.getId());
        if(res>0){
            return Result.success();
        }else{
            return Result.error("500","删除失败！");
        }
    }
    
    @PostMapping("/login")
    public Result<?> loginAction(@RequestBody User user){
    	LambdaQueryWrapper<User> wapper = Wrappers.<User>lambdaQuery();
    	wapper.eq(User::getUsername, user.getUsername());
    	wapper.eq(User::getPassword, user.getPassword());
    	User result = userMapper.selectOne(wapper);
    	if(result==null) {
    		return Result.error("-1", "账户名或密码错误!");
    	}else {
    		return Result.success();
    	}
        
    }
    
    @PostMapping("/register")
    public Result<?> registerAction(@RequestBody User user){
    	LambdaQueryWrapper<User> wappers = Wrappers.<User>lambdaQuery();
    	wappers.eq(User::getUsername, user.getUsername());
    	List<Object> list = userMapper.selectObjs(wappers);
    	if(list!=null && list.size()>0) {
    		 return Result.error("-1","已存在相同的账户名！");
    	}
    	int result = userMapper.insert(user);
    	if(result<=0) {
    		return Result.error("-1", "添加失败！");
    	}else {
    		return Result.success();
    	}
        
    }
    
    
}
