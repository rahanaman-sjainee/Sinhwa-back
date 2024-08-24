package com.example.backend.Controller;


import com.example.backend.Firebase.FirebaseService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/")
    public String index() {
        //template 속 html 로 리다이렉트
        return "test";

    }

    //@PostMapping 도 가능
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String post(@RequestParam("files") List<MultipartFile> files) throws IOException{
        for (MultipartFile file : files) {
            firebaseService.uploadFiles(file,"test/",file.getName());
        }
//        files.forEach(file ->{
//            if(file.isEmpty()) return;
//            try {
//                firebaseService.uploadFiles(file,file.getName());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
        return "test";
    }

}
