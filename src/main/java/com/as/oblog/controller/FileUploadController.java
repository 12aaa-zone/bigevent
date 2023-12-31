package com.as.oblog.controller;

import com.as.oblog.pojo.Result;
import com.as.oblog.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 12aaa-zone
 */

@RestController
public class FileUploadController {

    // 对应前端enctype=mutipartfile的post请求
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String oldFileName = file.getOriginalFilename();

        //保证文件的名字是唯一的,从而防止文件覆盖
        //将UUID随机码与文件后缀名拼接
        String filename = UUID.randomUUID().toString()+oldFileName.substring(oldFileName.lastIndexOf("."));

        //file.transferTo(new File("E:\\PC_Desktop\\OBlog\\DownloadFiles\\"+oldFileName));

        //OOS云存储文件内容
        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        return Result.success(url);
    }
}
