package com.leet.study;


import com.leet.study.model.Problem;
import com.leet.study.model.URL;
import okhttp3.Response;

import java.io.IOException;

import static java.lang.System.out;

public class ProblemService {
    private static volatile ProblemService problemService;
    private Problem problem;
    private OkHttpHelper okHttpHelperInstance;

    public ProblemService() {
        okHttpHelperInstance = OkHttpHelper.getSingleton();
    }

    public static ProblemService getSingleton() {
        ProblemService result = problemService;
        if (result == null) {
            synchronized (ProblemService.class) {
                result = problemService;
                if (result == null) {
                    result = problemService = new ProblemService();
                }
            }
        }
        return result;
    }

    public Problem getAllProblemsInformation() throws IOException {
        Problem instance = problem;

        while (instance == null) {
            Response response = okHttpHelperInstance.getSync(URL.PROBLEMS);

            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                instance = problem = okHttpHelperInstance.fromJson(responseData, Problem.class);
            } else {
                    out.println("code : " + response.code() + " message : " + response.message());
            }
            response.close();
        }

        return instance;
    }

}
