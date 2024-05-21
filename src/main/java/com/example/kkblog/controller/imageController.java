package com.example.kkblog.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.kkblog.config.exception.KKBlogException;
import com.example.kkblog.controller.dto.ResponseDto;
import com.example.kkblog.domain.Image;
import com.example.kkblog.domain.User;
import com.example.kkblog.domain.dto.UserDto;
import com.example.kkblog.mapper.ImageMapper;
import com.example.kkblog.mapper.UserMapper;
import com.example.kkblog.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @Author xiaoke
 * @Date 2024 04 10 22 00
 **/
@Controller
@RequestMapping("/image")
public class imageController {
    @Autowired
    private ImageMapper imageMapper;

    @Value("${kkBlog.image-path}")
    private String imagePath;

    @Value("${kkBlog.ip-address}")
    private String ipAddress;

    @Value("${server.port}")
    private String port;

    @Value("${kkBlog.protocol}")
    private String protocol;

    @Autowired
    private UserMapper userMapper;

    @ResponseBody
    @PostMapping("/upload")
    public ResponseDto upload(@RequestParam MultipartFile image, @RequestParam String userId,
                              @RequestParam(required = false) String name, @RequestParam(required = false) String description) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        //原始名称
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseDto.Fail("图片格式异常！");
        }
        if (StringUtils.isNull(userId)) {
            return ResponseDto.Fail("用户id为空！");
        }
        //生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);
        //新文件
        File newFile = new File(imagePath + File.separator + dateDirs + File.separator + newFileName);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        image.transferTo(newFile);
        Image saveImage = new Image();
        // 保存到数据库
        saveImage.setUserId(Integer.parseInt(userId));
        saveImage.setName(name);
        saveImage.setDescription(description);
        saveImage.setPath(("/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveImage.setUrl((protocol + "://" + ipAddress + ":" + port + "/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        imageMapper.insert(saveImage);
        //完整的url
        return ResponseDto.Success("上传成功", saveImage.getPath());
    }

    @ResponseBody
    @PostMapping("/md/upload")
    public JSONObject mdUpload(@RequestParam("editormd-image-file") MultipartFile image, HttpSession session) throws IOException {
        JSONObject jsonObject = new JSONObject();
        UserDto user = (UserDto) session.getAttribute("user");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        //原始名称
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            jsonObject.put("success", 0);
            jsonObject.put("message", "图片格式异常!");
            return jsonObject;
        }
        //生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);
        //新文件
        File newFile = new File(imagePath + File.separator + dateDirs + File.separator + newFileName);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        image.transferTo(newFile);
        Image saveImage = new Image();
        // 保存到数据库
        saveImage.setName(originalFilename);
        saveImage.setUserId(user.getId());
        saveImage.setPath(("/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveImage.setUrl((protocol + "://" + ipAddress + ":" + port + "/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        imageMapper.insert(saveImage);
        jsonObject.put("success", 1);
        jsonObject.put("message", "上传成功!");
        jsonObject.put("url", saveImage.getPath());
        return jsonObject;
    }

    // 更换背景
    @ResponseBody
    @PostMapping("/changeBg")
    public ResponseDto changeBg(@RequestParam MultipartFile image,
                              @RequestParam(required = false) String name,
                                HttpSession session) throws IOException {
        UserDto user = (UserDto) session.getAttribute("user");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        //原始名称
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseDto.Fail("图片格式异常！");
        }
        //生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);
        //新文件
        File newFile = new File(imagePath + File.separator + dateDirs + File.separator + newFileName);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        image.transferTo(newFile);
        Image saveImage = new Image();
        // 保存到数据库
        saveImage.setUserId(user.getId());
        saveImage.setName(StringUtils.isEmpty(name) ? originalFilename : name);
        saveImage.setPath(("/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveImage.setUrl((protocol + "://" + ipAddress + ":" + port + "/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        imageMapper.insert(saveImage);
        // 保存用户的背景图
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setBackgroundImage(saveImage.getPath());
        userMapper.updateUserBgById(updateUser);
        //完整的url
        return ResponseDto.Success("上传成功", saveImage.getPath());
    }

    // 更换头像
    @ResponseBody
    @PostMapping("/changeAvatar")
    public ResponseDto changeAvatar(@RequestParam MultipartFile image,
                                @RequestParam(required = false) String name,
                                HttpSession session) throws IOException {
        UserDto loginUser = (UserDto) session.getAttribute("user");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        //原始名称
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            return ResponseDto.Fail("图片格式异常！");
        }
        if (image.getSize() > 1024 * 1024 * 2) {
            return ResponseDto.Fail("头像仅支持最大图片2M");
        }
        //生成文件名
        assert originalFilename != null;
        String newFileName = res + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        String folderPath = date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1) + File.separator + (date.get(Calendar.DATE));
        File dateDirs = new File(folderPath);
        //新文件
        File newFile = new File(imagePath + File.separator + dateDirs + File.separator + newFileName);
        //判断目标文件所在的目录是否存在
        if (!newFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        //将内存中的数据写入磁盘
        image.transferTo(newFile);
        Image saveImage = new Image();
        // 保存到数据库
        saveImage.setUserId(loginUser.getId());
        saveImage.setName(StringUtils.isEmpty(name) ? originalFilename : name);
        saveImage.setPath(("/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        saveImage.setUrl((protocol + "://" + ipAddress + ":" + port + "/images/" + folderPath + "/" + newFileName).replaceAll("\\\\", "/"));
        imageMapper.insert(saveImage);
        // 保存用户的背景图
        User updateUser = new User();
        updateUser.setId(loginUser.getId());
        updateUser.setAvatar(saveImage.getPath());
        userMapper.updateUserAvatarById(updateUser);
        // 更新session
        loginUser.setAvatar(saveImage.getPath());
        session.setAttribute("user", loginUser);
        //完整的url
        return ResponseDto.Success("上传成功", saveImage.getPath());
    }
}
