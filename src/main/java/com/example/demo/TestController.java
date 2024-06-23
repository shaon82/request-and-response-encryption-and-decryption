package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class TestController {


    @Autowired
    private StudentRepositoty repositoty;


    @GetMapping("/get-text")
    public String getText(){
        return "Hello! i am Shaon";
    }


//    @PostMapping("/save-student")
//    public Student saveStudent(@RequestBody Student student){
//        Student student1 =  repositoty.save(student);
//        return student1;
//    }

    @GetMapping("/get-all")
    public List<Student> getAll(HttpServletRequest request, HttpServletResponse response)throws Exception{
        List<Student> list = repositoty.findAll();
        return list;
    }

    @GetMapping("/get-by-id")
    public Student getById(@RequestParam Long id){
        Student student =  repositoty.getById(id);
        return student;
    }

    @PostMapping("/test/save/student")
    public Student saveSecond(@RequestBody Student student){

        System.out.println();
        System.out.println();
        System.out.println();
        return student;
    }



}
