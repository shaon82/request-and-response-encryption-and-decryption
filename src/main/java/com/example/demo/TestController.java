package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/save-student")
    public Student saveStudent(@RequestBody Student student){
        Student student1 =  repositoty.save(student);
        return student1;
    }

    @GetMapping("/get-all")
    public List<Student> getAll(HttpServletRequest request, HttpServletResponse response)throws Exception{
        List<Student> list = repositoty.findAll();

//        String response = new ObjectMapper().writeValueAsString(list);
//        request.setAttribute("responseBody", list);
        return list;
    }




    @PostMapping("/save")
    public Student saveSecond(@RequestBody String string){

        System.out.println();
        System.out.println();
        System.out.println();
        return null;
    }
}
