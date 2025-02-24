package com.ammous.springbatch.student;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rami AMMOUS
 */
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final  JobLauncher jobLauncher;
    private final Job job;


    @PostMapping
    public void importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt",System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException
                 | JobRestartException e) {
            e.printStackTrace();
        }
    }

//  @Scheduled(cron = "0 0 0 * * ?")  // Chaque jour à minuit
    @Scheduled(cron = "0 20 14 24 2 ?")
    public void runJob() throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // Ajout d'un paramètre pour garantir que le job est unique à chaque exécution
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
