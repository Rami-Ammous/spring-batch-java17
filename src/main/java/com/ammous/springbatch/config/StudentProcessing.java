package com.ammous.springbatch.config;


import com.ammous.springbatch.student.Student;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author Rami AMMOUS
 */

public class StudentProcessing implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) throws Exception {
        //all the business logic goes here
        return student;
    }
}
