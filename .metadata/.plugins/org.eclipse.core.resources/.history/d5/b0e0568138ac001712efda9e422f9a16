package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.StudentMapper;
import com.example.demo.model.CourseModel;
import com.example.demo.model.StudentModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceDatabase implements StudentService
{
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public StudentModel selectStudent (String npm)
    {
        log.info ("select student with npm {}", npm);
        return studentMapper.selectStudent (npm);
    }


    @Override
    public List<StudentModel> selectAllStudents ()
    {
        log.info ("select all students");
        return studentMapper.selectAllStudents ();
    }


    @Override
    public void addStudent (StudentModel student)
    {
        studentMapper.addStudent (student);
    }


    @Override
    public void deleteStudent (String npm)
    {
    	log.info("student " + npm + " deleted");
    	studentMapper.deleteStudent(npm);
    }


	@Override
	public void updateStudent(StudentModel student) {
		log.info("student " + student.getName() + " updated");
		studentMapper.updateStudent(student);
		
	}


	@Override
	public CourseModel selectCourse(String id) {
		 log.info ("select course with id_course {}", id);
	     return studentMapper.selectCourse (id);

	}
}
