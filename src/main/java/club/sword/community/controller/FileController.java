package club.sword.community.controller;

import club.sword.community.dto.FileDTO;
import club.sword.community.exception.CustomizeErrorCode;
import club.sword.community.exception.CustomizeException;
import club.sword.community.provider.AliyunProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by XuCao on 2020/5/18
 */
@Controller
@Slf4j
public class FileController {

    @Autowired
    private AliyunProvider aliyunProvider;

    @RequestMapping("/file/upload")
    @ResponseBody//使返回的是json
    public FileDTO upload(HttpServletRequest request) {
        //强制转换
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //获取文件流
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        FileDTO fileDTO = new FileDTO();
        try {
            assert file != null;
            //上传文件后，返回该文件的url地址
            String url = aliyunProvider.fileUpload(file.getInputStream(), Objects.requireNonNull(file.getOriginalFilename()));
            fileDTO.setUrl(url);
            fileDTO.setSuccess(1);
            fileDTO.setMessage("上传成功");
        } catch (IOException e){
            log.error("upload error", e);
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            e.printStackTrace();
            //抛出异常
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_FILE_ERROR);
        }
        return fileDTO;
    }
}
