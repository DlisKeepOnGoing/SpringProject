package com.newdim.SpringDemo.controller;

import com.newdim.SpringDemo.entity.FileData;
import com.newdim.SpringDemo.respository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class SQLController {
    @Autowired
    private FileDataRepository fileDataRepository;

    @PostMapping("/add")
    public String addFileData(@RequestParam String name,
                              @RequestParam String filePath,
                              @RequestParam String sourcePath) {
        FileData fileData = new FileData();
        fileData.setName(name);
        fileData.setFilePath(filePath);
        fileData.setSourcePath(sourcePath);
        fileDataRepository.save(fileData);
        return "Saved";
    }

    @GetMapping("/sayhi")
    public String addFileData() {
        return "hi";
    }

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<FileData> getAllUsers() {
        // This returns a JSON or XML with the users
        return fileDataRepository.findAll();
    }

}
