package com.todd.toj_backend.controller;

import cn.hutool.core.io.FileUtil;
import com.todd.toj_backend.pojo.ResponseResult;
import com.todd.toj_backend.pojo.upload.UploadResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {

    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        String savedFileName = UUID.randomUUID() + "." + FileUtil.extName(file.getOriginalFilename());
        String saveDirPath = "D:/toj_files/temp/";
        File savedFile = new File(saveDirPath + savedFileName);
        if(!savedFile.createNewFile()){
            return new ResponseResult<>(500, "上传文件失败");
        }
        file.transferTo(savedFile);

        return new ResponseResult<>(200, new UploadResponse(savedFileName));
    }
}
