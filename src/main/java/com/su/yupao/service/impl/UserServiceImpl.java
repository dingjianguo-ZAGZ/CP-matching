package com.su.yupao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.su.yupao.common.ErrorCode;
import com.su.yupao.exception.BusinessException;
import com.su.yupao.mapper.UserMapper;
import com.su.yupao.model.domain.User;
import com.su.yupao.service.UserService;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.su.yupao.constant.UserConstant.ADMIN;
import static com.su.yupao.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author suhon
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-09-09 19:59:30
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;

    /**
     * 设置“盐值”，混淆加密
     */
    private static final String SALT = "su";

    /**
     * 用户注册
     * @param account 账户名
     * @param password 密码
     * @param checkPassword 校验密码
     * @param planetCode 星球编号
     * @return
     */
    @Override
    public long Register(String account, String password, String checkPassword,String planetCode) {
        if(StringUtils.isAnyBlank(account,password,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(account.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(password.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //账户只能包含数字和字母(必须包含一个数字和一个字母，长度1-9位)
        String validPatten = "^(?=.*\\d)(?=.*[A-z])[\\da-zA-Z]{1,9}$";
        Matcher matcher = Pattern.compile(validPatten).matcher(account);
        if(!matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //查询数据库操作，比较浪费性能，所以置后
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",account);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //查询数据库操作，星球编号不能重复
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("planetCode",planetCode);
        count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if(!password.equals(checkPassword)){//判断字符串相等，不能用==
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(account);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return user.getId();//自动拆箱为long
    }

    /**
     * 用户登录
     * @param userAccount 账户名
     * @param userPassword 密码
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(userPassword.length()<8){
            return null;
        }
        //账户只能包含数字和字母(必须包含一个数字和一个字母，长度1-9位)
        String validPatten = "^(?=.*\\d)(?=.*[A-z])[\\da-zA-Z]{1,9}$";
        Matcher matcher = Pattern.compile(validPatten).matcher(userAccount);
        if(!matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //将输入的密码进行加密，然后与数据库中存储的加密密码进行对比
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //在数据库中查询
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("用户名密码不匹配");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //用户脱敏，创建返回给前端的用户,将从数据库中查到的可以返回的数据填入
        User safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

    return safetyUser;
    }

    /**
     * 获取安全用户信息
     * @param orignUser
     * @return
     */
    @Override
    public User getSafetyUser(User orignUser){

        User safetyUser = new User();
        safetyUser.setId(orignUser.getId());
        safetyUser.setUsername(orignUser.getUsername());
        safetyUser.setUserAccount(orignUser.getUserAccount());
        safetyUser.setAvatarUrl(orignUser.getAvatarUrl());
        safetyUser.setGender(orignUser.getGender());
        safetyUser.setPhone(orignUser.getPhone());
        safetyUser.setEmail(orignUser.getEmail());
        safetyUser.setUserStatus(orignUser.getUserStatus());
        safetyUser.setCreateTime(orignUser.getCreateTime());
        safetyUser.setUserRole(orignUser.getUserRole());
        safetyUser.setPlanetCode(orignUser.getPlanetCode());
        safetyUser.setTags(orignUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int logout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签查询用户 (内存过滤)
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> selectUserByTags(List<String> tagNameList) {
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        //从内存中查询
        //先查询出所有用户
        List<User> userList = userMapper.selectList(queryWrapper);
        //在内存中进行筛选
        //将json字符串转换为集合
        Gson gson = new Gson();
        return userList.stream().filter(
                user -> {
                    String tags = user.getTags();
                    Set<String> tempTagNameList = gson.fromJson(tags, new TypeToken<Set<String>>(){}.getType());
                    //从数据库中取出来的数据，要判断是否为空
                    tempTagNameList = Optional.ofNullable(tempTagNameList).orElse(new HashSet<>());
                    for (String tagName : tagNameList) {
                        if(!tempTagNameList.contains(tagName)){
                            return false;
                        }
                    }
                    return true;
                }
        ).map(this::getSafetyUser).collect(Collectors.toList());

    }

    /**
     * 修改用户信息
     * @param user
     * @param loginUser
     * @return
     */
    @Override
    public int updateUser(User user, User loginUser) {
        //获取id
        long userId = user.getId();
        if(userId <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //TODO如果用户没有传任何要更新的值，就直接报错，不用执行 update 语句
        //验证登录用户是否为管理员或自己
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //触发修改
        //根据 id 修改用户
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 根据标签查询用户 （SQL版）
     * @param tagNameList
     * @return
     */
    @Deprecated
    private List<User> selectUserByTagsBySQL(List<String> tagNameList) {
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        //设置查询条件,模糊查询
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags",tagName);
        }
        List<User> list = userMapper.selectList(queryWrapper);

        return list.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        //鉴权
        //获取用户登录态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user == null || user.getUserRole() != ADMIN){//要判断用户登录态是否为空，才能判断角色
            return false;
        }
        return true;
    }
    /**
     * 是否为管理员
     * @param user
     * @return
     */
    public boolean isAdmin(User user){
        //鉴权
        //获取用户登录态
        Integer userObj = user.getUserRole();
        if(user == null || user.getUserRole() != ADMIN){//要判断用户登录态是否为空，才能判断角色
            return false;
        }
        return true;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if(userObj == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return (User) userObj;
    }
}




