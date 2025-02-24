package com.ammous.springbatch.config;

import com.ammous.springbatch.student.Student;
import com.ammous.springbatch.student.StudentRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Component
public class StudentItemWriter implements ItemWriter<Student> {

    private final StudentRepository studentRepository;

    public StudentItemWriter(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void write(Chunk<? extends Student> chunk) throws Exception {
        studentRepository.saveAll(chunk.getItems());
    }
}
