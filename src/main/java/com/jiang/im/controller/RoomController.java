package com.jiang.im.controller;

import com.jiang.im.dataobject.RoomInfo;
import com.jiang.im.dataobject.UserProfile;
import com.jiang.im.enums.ResultEnum;
import com.jiang.im.form.RoomForm;
import com.jiang.im.service.RoomInfoService;
import com.jiang.im.service.UserService;
import com.jiang.im.utils.ResultVOUtil;
import com.jiang.im.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/room")
public class RoomController {


    @Autowired
    UserService userService ;

    @Autowired
    RoomInfoService roomInfoService ;

    /**
     * 创建房间
     * @param roomForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/createroom",method = RequestMethod.POST)
    public ResultVo createRoom(@Valid RoomForm roomForm ,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultVOUtil.error(ResultEnum.USER_UPDATE_ERR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        UserProfile userProfile = userService.findOne(roomForm.getUserId());
        if(userProfile == null){
            return ResultVOUtil.error(ResultEnum.USER_NOT_FOUND);
        }

        RoomInfo roomInfo = new RoomInfo();
        BeanUtils.copyProperties(roomForm,roomInfo);
        roomInfo.setUserName(userProfile.getUserNick());
        roomInfo.setUserAvater(userProfile.getUserHeader());

        roomInfoService.createRoom(roomInfo);

        return ResultVOUtil.success(roomInfo);

    }

    /**
     * 获取房间列表
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ResultVo roomList(@RequestParam(value = "size",defaultValue = "10") Integer size ,
                             @RequestParam(value = "page",defaultValue = "1")Integer page){

        PageRequest request = new PageRequest(page-1,size);
        Page<RoomInfo> roomInfoPage = roomInfoService.findList(request);
        return ResultVOUtil.success(roomInfoPage.getContent());
    }



}
