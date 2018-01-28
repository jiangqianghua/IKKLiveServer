package com.jiang.im.controller;


import com.jiang.im.utils.FileUtil;
import com.jiang.im.utils.ResultVOUtil;
import com.jiang.im.vo.ResultVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    /**
     * 单文件上传
     * @param file
     * @param name
     * @return
     */
    @RequestMapping(value="header",method= RequestMethod.POST)
    public ResultVo uploadHeader(@RequestParam("file") MultipartFile file,
                                      @RequestParam("name") String name){
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        name += ".jpg" ;
        String fileUrl = "http://192.168.1.102:8080/headers/"+name;
        String filePath = "/Users/jiangqianghua/Downloads/apache-tomcat-7.0.75/webapps/headers/";
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, name);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.success(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.success(e.getMessage());
        }
        return ResultVOUtil.success(fileUrl);
    }

}
