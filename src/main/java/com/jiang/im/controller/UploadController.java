package com.jiang.im.controller;


import com.jiang.im.utils.FileUtil;
import com.jiang.im.utils.ResultVOUtil;
import com.jiang.im.vo.ResultVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

//    @Value("${server.uploadaddr}")
//    private String uploadAddr ;

    @Value("${server.assert-avater}")
    private String avaterPath ;
    /**
     * 单文件上传
     * @param file
     * @param name
     * @return
     */
    @RequestMapping(value="img",method= RequestMethod.POST)
    public ResultVo uploadHeader(@RequestParam("file") MultipartFile file,
                                      @RequestParam("name") String name){
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        name += ".jpg" ;
       // String fileUrl = "http://"+uploadAddr+"/headers/"+name;
        String filePath = avaterPath+"headers/";
        String dbPath = "headers/" +name ;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, name);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.success(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.success(e.getMessage());
        }
        return ResultVOUtil.success(dbPath);
    }


    /**
     * 单文件上传 废弃
     * @param file
     * @param name
     * @return
     */
    @RequestMapping(value="poster",method= RequestMethod.POST)
    public ResultVo uploadPoster(@RequestParam("file") MultipartFile file,
                                 @RequestParam("name") String name){
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        name += ".jpg" ;
        // String fileUrl = "http://"+uploadAddr+"/headers/"+name;
        String filePath = avaterPath+"headers/";
        String dbPath = avaterPath +name ;
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, name);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.success(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.success(e.getMessage());
        }
        return ResultVOUtil.success(dbPath);
    }

}
