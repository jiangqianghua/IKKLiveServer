package com.jiang.im.controller;

import com.jiang.im.check.LoginCheck;
import com.jiang.im.constant.CookieConstant;
import com.jiang.im.constant.RedisConstant;
import com.jiang.im.dataobject.RoomInfo;
import com.jiang.im.dataobject.UserProfile;
import com.jiang.im.entity.GiftInfo;
import com.jiang.im.enums.ResultEnum;
import com.jiang.im.exception.LiveException;
import com.jiang.im.form.UserProfileForm;
import com.jiang.im.service.RoomInfoService;
import com.jiang.im.service.UserService;
import com.jiang.im.utils.CookieUtil;
import com.jiang.im.utils.CryptoUtils;
import com.jiang.im.utils.ResultVOUtil;
import com.jiang.im.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService ;

    @Autowired
    private RoomInfoService roomInfoService ;

    @Autowired
    private StringRedisTemplate redisTemplate ;

    /**
     * 登陆
     * @param logininfo
     * @return
     */
    @PostMapping("/login")
    public ResultVo login(@RequestParam(value="logininfo") String logininfo){
        try {
            // 解密
            //"{user:jiang,pass:111,time:111,mac=10.2.3.4,rand=3344}"
            Map<String,String> loginInfoMap =  CryptoUtils.parseLoginInfo(logininfo);
            String account = loginInfoMap.get(CryptoUtils.ACCOUNT) ;
            String password = loginInfoMap.get(CryptoUtils.PASSWORD);
            Long loginTime = Long.parseLong(loginInfoMap.get(CryptoUtils.TIMESTAMP));
            String mac = loginInfoMap.get(CryptoUtils.MAC);
            //1 检测是否超时
            boolean isTimeOut = LoginCheck.checkLoginTimeOut(loginTime);
            if(isTimeOut){
                return ResultVOUtil.error(ResultEnum.USER_EXPIRE);
            }
            //2 检测用户是否存在
            UserProfile userInfo = userService.findUserByIDAndPwd(account,password);
            if (userInfo == null) {
                return ResultVOUtil.error(ResultEnum.USER_NOT_FOUND);
            }

            //3 获取盐值
            String salt = CryptoUtils.getSalt();

            String token = CryptoUtils.getEncryptCient(salt,account,mac);
            Integer expire = RedisConstant.EXPIRE;// 过期时间
            //4 设置token到redis
            String key = String.format(RedisConstant.TOKEN_PREFIX,account,mac) ;
            redisTemplate.opsForValue().set(key,salt,expire, TimeUnit.SECONDS);

            //5 token 返回给客户端
            return ResultVOUtil.success(token);
        }catch (LiveException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        } catch (Exception e){
            return ResultVOUtil.error(ResultEnum.USER_LOGIN_ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 自动登陆检测
     * @param token
     * @return
     */
    @PostMapping("/autologin")
    public ResultVo autoLogin(@RequestParam(value="token") String token){
        try {
            //token解密
            Map<String, String> map = CryptoUtils.decodeToken(token);
            String salt = map.get(CryptoUtils.SALT);
            String account = map.get(CryptoUtils.ACCOUNT);
            String timestamp = map.get(CryptoUtils.TIMESTAMP);
            String mac = map.get(CryptoUtils.MAC);

            String redisKey = String.format(RedisConstant.TOKEN_PREFIX,account,mac);
            // redis是否存在该token
            String tokenValue = redisTemplate.opsForValue().get(redisKey);
            if(salt.equals(tokenValue)){
                return ResultVOUtil.success(account);
            }
            return ResultVOUtil.error(ResultEnum.USER_EXPIRE);
        }catch (LiveException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        } catch (Exception e){
            return ResultVOUtil.error(ResultEnum.USER_LOGIN_ERROR.getCode(),e.getMessage());
        }
    }


    /**
     * 注册
     * @param account
     * @return
     */
    @PostMapping("/register")
    public ResultVo register(@RequestParam(value = "account") String account,@RequestParam(value = "password") String password) {

        UserProfile userProfile = new UserProfile();
        userProfile.setUserAccount(account);
        userProfile.setUserPassword(password);
        try {
            userService.reigisterUserProfile(userProfile);
            return ResultVOUtil.success();
        }catch (LiveException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }
    }

    /**
     * 获取自己的信息
     * @param account
     * @return
     */
    @PostMapping("selfprofile")
    public ResultVo selfProfile(@RequestParam(value = "account") String account){
        UserProfile userProfile =  userService.findOne(account);
        if (userProfile != null && userProfile.getUserAccount().equals(account)){
            return ResultVOUtil.success(userProfile);
        }
        return ResultVOUtil.error(ResultEnum.USER_NOT_FOUND);
    }

    /**
     * 退出
     */
    @PostMapping("/sigout")
    public ResultVo sigout(@RequestParam(value="token") String token){

        try {
            //token解密
            Map<String, String> map = CryptoUtils.decodeToken(token);
            String salt = map.get(CryptoUtils.SALT);
            String account = map.get(CryptoUtils.ACCOUNT);
            String timestamp = map.get(CryptoUtils.TIMESTAMP);
            String mac = map.get(CryptoUtils.MAC);

            String redisKey = String.format(RedisConstant.TOKEN_PREFIX,account,mac);
            // 清空redis对应的缓存
            redisTemplate.opsForValue().getOperations().delete(redisKey);
            return ResultVOUtil.success();
        }catch (LiveException e){
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        } catch (Exception e){
            return ResultVOUtil.error(ResultEnum.USER_LOGIN_ERROR.getCode(),e.getMessage());
        }
    }

    /**
     * 更新用户信息接口
     * @param userProfileForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/update")
    public ResultVo updateUser(@Valid UserProfileForm userProfileForm,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultVOUtil.error(ResultEnum.USER_UPDATE_ERR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        UserProfile userProfile = userService.findOne(userProfileForm.getUserAccount());
        if(userProfile == null){
            return ResultVOUtil.error(ResultEnum.USER_NOT_FOUND);
        }

        BeanUtils.copyProperties(userProfileForm,userProfile);

        userService.save(userProfile);

        return ResultVOUtil.success();
    }

    /**
     * 发送礼物
     * @return
     */
    @PostMapping("/sendgift")
    public ResultVo sendGift(@RequestParam(value = "roomId") int roomId,
                             @RequestParam(value = "sendUserId") String sendUserId,
                             @RequestParam(value = "giftId") int giftId,
                             @RequestParam(value = "num") int num){
        // 查找房间
        RoomInfo roomInfo = roomInfoService.findOne(roomId);
        if(roomInfo == null)
            return ResultVOUtil.error(ResultEnum.ROOM_NOT_FOUND);
        // 获取主播的信息
        UserProfile userProfile =  userService.findOne(roomInfo.getUserId());
        if(userProfile == null)
            return ResultVOUtil.error(ResultEnum.USER_NOT_FOUND);
        // 更新主播的数据
        userProfile.setUserGetnum(userProfile.getUserGetnum()+num);
        userService.save(userProfile);
        // 更新发送礼物的数据
        UserProfile sendUserProfile = userService.findOne(sendUserId);
        if(sendUserProfile == null)
            return ResultVOUtil.error(ResultEnum.USER_NOT_FOUND);
        // 更新发送者经验值
        sendUserProfile.setUserSendnum(sendUserProfile.getUserSendnum() + num);
        GiftInfo giftInfo = GiftInfo.getGiftById(giftId);
        sendUserProfile.setExp(sendUserProfile.getExp() + giftInfo.expValue*num);
        // 计算用户等级
        sendUserProfile.setUserLevel(sendUserProfile.getExp()/200 + 1);
        // 更新用户
        userService.save(sendUserProfile);
        return ResultVOUtil.success();
    }

}
