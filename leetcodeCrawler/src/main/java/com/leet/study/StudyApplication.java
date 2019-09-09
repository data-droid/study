package com.leet.study;

import com.leet.study.model.Problem;
import com.leet.study.model.User;
import com.leet.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;


@SpringBootApplication
public class StudyApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(StudyApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException, InterruptedException {
        for (User user : repository.findAll()) {
            System.out.println(user.toString());
            Login login = new Login(user.getEmail(),user.getPassword());
            login.doLogin();
            System.out.println(new ProblemService().getAllProblemsInformation().toString());
        }
    }
}
