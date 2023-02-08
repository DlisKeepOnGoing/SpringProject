package com.newdim.SpringDemo.service;

import com.newdim.SpringDemo.entity.FileData;
import com.newdim.SpringDemo.respository.FileDataRepository;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

@Service
public class MySQLService {

    @Autowired
    private FileDataRepository fileDataRepository;

    public String addFile(FileData fileData) {
        fileDataRepository.save(fileData);
        System.out.println(fileData.getName());
        System.out.println(fileData.getFilePath());
        System.out.println(fileData.getSourcePath());
        return "Sava successfully";
    }
    public String getFilePathByName(String fileName) {
        try {
            Optional<FileData> fileData = fileDataRepository.findByName(fileName);
            return fileData.get().getFilePath();
        } catch (Exception e) {
            // do nothing or add action code
            return null;
        }
    }



}
