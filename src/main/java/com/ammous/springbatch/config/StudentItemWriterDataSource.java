package com.ammous.springbatch.config;

import com.ammous.springbatch.student.Student;
import com.ammous.springbatch.student.StudentRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;


@Component
public class StudentItemWriterDataSource implements ItemWriter<Student> {

    private final DataSource dataSource;

    public StudentItemWriterDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void write(Chunk<? extends Student> chunk) throws Exception {
        String sql = "INSERT INTO student (id, first_name, last_name, age) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Student student : chunk.getItems()) {
                preparedStatement.setInt(1, student.getId());
                preparedStatement.setString(2, student.getFirstName());
                preparedStatement.setString(3, student.getLastName());
                preparedStatement.setInt(4, student.getAge());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}