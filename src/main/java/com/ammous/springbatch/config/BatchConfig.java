package com.ammous.springbatch.config;


import com.ammous.springbatch.student.Student;
import com.ammous.springbatch.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Rami AMMOUS
 */
@Configuration
public class BatchConfig {

    private final StudentRepository repository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemWriter<Student> writer;

    public BatchConfig(
            StudentRepository repository,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("studentItemWriterDataSource") ItemWriter<Student> writer
    ) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.writer = writer;
    }

    //@Value("${inputFile}") Resource resource;    // 1 000 000
    @Value("${inputFile}") Resource resource;  // 50 000
    @Bean
    public FlatFileItemReader<Student> itemReader() {


        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(resource);
        itemReader.setName("csvStudents");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Student> lineMapper() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
//      setStrict(false) >> Une ligne peut avoir moins de colonnes que prévu, et aucune exception ne sera levée.
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "age");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public StudentProcessing processor() {
        return new StudentProcessing();
    }

    /*@Bean
    public RepositoryItemWriter<Student> writer(){
        RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }*/

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<Student, Student>chunk(1000, transactionManager)    //chunk::  1m21     //chunk:: 38
                .reader(itemReader())
                .processor(processor())
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importStudents", jobRepository)
                .start(importStep())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(100);
        return taskExecutor;
    }
}
