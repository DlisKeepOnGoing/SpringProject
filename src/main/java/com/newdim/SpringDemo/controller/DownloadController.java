package com.newdim.SpringDemo.controller;

import com.newdim.SpringDemo.entity.FileData;
import com.newdim.SpringDemo.service.MySQLService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class DownloadController {
    @Autowired
    private MySQLService mySQLService;
    private static final String FILE_DIR = "D://IdeaProjects//SpringDemo//files//";

    // The remote host -> SpringServer -> Save as file stored in my server
    @RequestMapping("/downloadToServer")
    public void downloadToServer(@RequestParam String netAddress, @RequestParam String fileName) throws IOException {
        fetchResourcesFromNet(netAddress, fileName);
        FileData fileData = new FileData();
        fileData.setName(fileName);
        fileData.setFilePath(FILE_DIR + fileName);
        fileData.setSourcePath(netAddress);
        String pathName = mySQLService.getFilePathByName(fileName);
        if(pathName == null) mySQLService.addFile(fileData);
    }

    @RequestMapping(value="/downloadMutipleToServer")
    public void downloadMutipleToServer(@RequestBody JsonNode payload) throws IOException {
        if (payload.isArray()) {
            for (JsonNode jsonNode : payload) {
                String urlPath = jsonNode.get("url").asText();
                String fileName = jsonNode.get("name").asText();
                System.out.println(urlPath);
                System.out.println(fileName);
                fetchResourcesFromNet(urlPath, fileName);
                FileData fileData = new FileData();
                fileData.setName(fileName);
                fileData.setFilePath(FILE_DIR + fileName);
                fileData.setSourcePath(urlPath);
                String pathName = mySQLService.getFilePathByName(fileName);
                if(pathName == null) mySQLService.addFile(fileData);
            }
        }
        System.out.println(payload);
    }



    // SpringServer -> Client -> Servlet Response
    @RequestMapping("/downloadToClient")
    public void downloadToClient(HttpServletResponse response, @RequestParam String fileName) throws IOException {

        // Search the fileName in ddb.
        // If there is one, continue the below steps.
        // If there is no, then return to the front end and tell users.
        String pathName = mySQLService.getFilePathByName(fileName);
        System.out.println("pathName: " + pathName);
        if (pathName == null) {
            System.out.println("pathName: " + pathName);
            return;
        }

        // Input
        File file = new File(pathName);
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        // Output
        response.reset();
        response.setContentType("application/octet-stream");
        String headKey = "Content-Disposition";
        String headValue = "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setHeader(headKey, headValue);
        ServletOutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();

    }

    private void fetchResourcesFromNet(String netAddress, String fileName) throws IOException {

        // Input Stream
        URL url = new URL(URIUtil.encodeQuery(netAddress, "UTF-8"));
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        // Output Stream
        FileOutputStream fileOutputStream = new FileOutputStream(FILE_DIR + fileName);

        int byteRead;
        byte[] buffer = new byte[8192];
        while ((byteRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, byteRead);
        }
        fileOutputStream.close();

    }

}
