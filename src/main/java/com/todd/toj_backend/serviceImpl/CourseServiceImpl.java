package com.todd.toj_backend.serviceImpl;

import cn.hutool.core.io.FileUtil;
import com.todd.toj_backend.mapper.CourseMapper;
import com.todd.toj_backend.pojo.course.AddCourseRequest;
import com.todd.toj_backend.pojo.course.AttendCourse;
import com.todd.toj_backend.pojo.course.Course;
import com.todd.toj_backend.pojo.course.CourseFile;
import com.todd.toj_backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseMapper courseMapper;

    @Value("${file-path.base-file-path}")
    String baseFilePath;

    @Override
    public List<Course> getAllCourseList() {
        return courseMapper.queryAllCourse();
    }

    @Override
    public Boolean isCourseEnroll(AttendCourse attendCourse) {
        var result = courseMapper.queryIsCourseEnroll(attendCourse);
        if(result == null)
            return false;
        return courseMapper.queryIsCourseEnroll(attendCourse) > 0;
    }

    @Override
    public Integer addCourseEnroll(AttendCourse attendCourse) {
        return courseMapper.insertCourseEnroll(attendCourse);
    }

    @Override
    public Course getCourse(String courseId) {
        return courseMapper.queryCourse(courseId);
    }

    @Override
    @Transactional
    public void addCourse(AddCourseRequest addCourseRequest) {
        courseMapper.insertCourse(addCourseRequest);
        FileUtil.move(Paths.get(baseFilePath + "/temp/" + addCourseRequest.getCoverFile()),
                Paths.get(baseFilePath + "/cover/course/" + addCourseRequest.getId() + ".jpg"),
                true);
    }

    @Override
    public Integer deleteCourse(String courseId) {
        deleteAllCourseFile(courseId);
        return courseMapper.deleteCourse(courseId);
    }

    @Override
    @Transactional
    public boolean uploadCourseFile(MultipartFile file, String courseId) throws IOException {
        CourseFile courseFile = new CourseFile();
        courseFile.setCourseId(courseId);
        courseFile.setFile(file.getOriginalFilename());

        if(Objects.equals(FileUtil.extName(file.getOriginalFilename()), "mp4")){
            courseFile.setFileType("video");
        }else {
            courseFile.setFileType("other");
        }
        courseMapper.insertCourseFile(courseFile);

        String savedFileName = file.getOriginalFilename();
        String saveDirPath = baseFilePath + "/course_file/" + courseId + "/";
        if(!FileUtil.exist(saveDirPath)){
            FileUtil.mkdir(saveDirPath);
        }
        File savedFile = new File(saveDirPath + savedFileName);

        file.transferTo(savedFile);
        return true;
    }

    @Override
    public boolean deleteCourseFile(String courseFileId, String courseId) {
        CourseFile courseFile = courseMapper.queryCourseFile(courseFileId);
        if(courseFile == null)
            return false;

        String file = courseFile.getFile();
        String filePath = baseFilePath + "/course_file/" + courseId + "/";
        FileUtil.del(filePath + file);
        courseMapper.deleteCourseFile(courseFileId);

        return true;
    }

    @Override
    public void deleteAllCourseFile(String courseId) {
        courseMapper.deleteAllCourseFile(courseId);
        String filePath = baseFilePath + "/course_file/" + courseId;
        FileUtil.del(filePath);
    }

    @Override
    public Integer deleteCourseEnroll(AttendCourse attendCourse) {
        return courseMapper.deleteCourseEnroll(attendCourse);
    }

    @Override
    public List<Course> getOwnCourseList(String userId) {
        return courseMapper.queryCourseUserIdLimit(userId);
    }

    @Override
    public List<CourseFile> getCourseFileList(String courseId) {
        return courseMapper.queryCourseFileList(courseId);
    }
}
