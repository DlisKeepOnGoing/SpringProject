package com.newdim.SpringDemo.service;

import com.newdim.SpringDemo.entity.FileData;
import com.newdim.SpringDemo.respository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MySQLService {

    @Autowired
    private FileDataRepository fileDataRepository;

    public String addFile(final FileData fileData) {

        try{
            fileDataRepository.save(fileData);
            return fileData.getName() + ": file downloaded successfully!";
        }catch (DataIntegrityViolationException e) {
            System.out.println("There are duplicate entries for fileName " + fileData.getName());
            return fileData.getName() + ": file downloaded successfully! there is already an entry in the host server, use the latest download to overlap the old one!";
        }catch (Exception ignored) {
            System.out.println("Unknown ERROR");
            return fileData.getName() + ": Unknown ERROR!";
        }
    }

    public String getFilePathByName(final String fileName) {
        try {
            Optional<FileData> fileData = fileDataRepository.findByName(fileName);
            return fileData.get().getFilePath();
        } catch (Exception e) {
            System.out.println("we can not find the data");
            return null;
        }
    }

}
