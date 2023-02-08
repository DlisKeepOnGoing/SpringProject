package com.newdim.SpringDemo.controller;

import com.newdim.SpringDemo.entity.FileData;
import com.newdim.SpringDemo.respository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/*
This file is used for testing ddb only
* */
@RestController
public class SQLController {
    @Autowired
    private FileDataRepository fileDataRepository;

    @PostMapping("/add")
    public String addFileData(@RequestParam String name,
                              @RequestParam String filePath,
                              @RequestParam String sourcePath) {

        try{
            fileDataRepository.save(FileData.builder().name(name).filePath(filePath).sourcePath(sourcePath).build());
            return "file uploaded successfully : " + name;
        }catch (DataIntegrityViolationException e) {
            System.out.println("There are duplicate entries for fileName " + name);
            return "No need upload because there is already an entry in the host server";
        }catch (Exception ignored) {
            System.out.println("Unknown ERROR");
            return "Unknown ERROR";
        }
    }

    @GetMapping("/findByName")
    public String findByName(@RequestParam String fileName) {
        try {
            Optional<FileData> fileData = fileDataRepository.findByName(fileName);
            return fileData.get().getFilePath();
        } catch (Exception e) {
            System.out.println("we can not find the data");
            return null;
        }
    }

    @GetMapping("/sayhi")
    public String addFileData() {
        return "hi";
    }

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<FileData> getAllUsers() {
        // This returns a JSON or XML with the users
        // fileDataRepository.
        return fileDataRepository.findAll();
    }

}
