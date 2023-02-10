package com.newdim.SpringDemo.controller;

import com.newdim.SpringDemo.entity.DownloadResponse;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class DownloadController {

    private static final String FILE_DIR = "D://IdeaProjects//SpringDemo//files//";

    @Autowired
    private MySQLService mySQLService;

    @RequestMapping("json")
    public DownloadResponse testJson() {
        DownloadResponse downloadResponse = DownloadResponse.builder().url("http://www.baidu.com").name("json").message("success").build();
        return downloadResponse;
    }

    @RequestMapping("/jsonList")
    public List<DownloadResponse> testJsonList() {
        DownloadResponse downloadResponse = DownloadResponse.builder().url("http://www.baidu.com").name("json").message("success").build();
        List<DownloadResponse> downloadResponseList = new ArrayList<>();
        downloadResponseList.add(downloadResponse);
        return downloadResponseList;
    }

    // The remote host -> SpringServer -> Save as file stored in my server
    @RequestMapping("/downloadToServer")
    public DownloadResponse downloadToServer(@RequestParam String netAddress, @RequestParam String fileName) {
        FileData fileData = FileData.builder().name(fileName).filePath(FILE_DIR + fileName).sourcePath(netAddress).build();
        String SQL_response = mySQLService.addFile(fileData);
        boolean res_Downloaded = fetchResourcesFromNet(netAddress, fileName);
        String message;
        if(res_Downloaded) {
            message = SQL_response;
        } else {
            message = "download failed, there is a network error";
        }
        return DownloadResponse.builder().url(netAddress).name(fileName).message(message).build();
    }

    @RequestMapping(value="/downloadMutipleToServer")
    public List<DownloadResponse> downloadMutipleToServer(@RequestBody JsonNode payload) {
        // StringBuilder message = new StringBuilder();
        List<DownloadResponse> downloadResponseList = new ArrayList<>();
        if (!payload.isArray()) {
            System.out.println("The payload is not correct, please check it");
            return downloadResponseList;
        }
        for (JsonNode jsonNode : payload) {
            String urlPath = jsonNode.get("url").asText();
            String fileName = jsonNode.get("name").asText();
            FileData fileData = FileData.builder().name(fileName).filePath(FILE_DIR + fileName).sourcePath(urlPath).build();
            String SQL_response = mySQLService.addFile(fileData);
            boolean res_Downloaded = fetchResourcesFromNet(urlPath, fileName);
            String log_info;
            if(res_Downloaded) {
                log_info = SQL_response;
            } else {
                log_info = "download failed, there is a network error!";
            }
            DownloadResponse downloadResponse = DownloadResponse.builder()
                    .url(urlPath)
                    .name(fileName)
                    .message(log_info)
                    .build();
            downloadResponseList.add(downloadResponse);
        }
        return downloadResponseList;
    }

    // SpringServer -> Client -> Servlet Response
    @RequestMapping("/downloadToClient")
    public void downloadToClient(HttpServletResponse response, @RequestParam String fileName) throws IOException {

        // Search the fileName in ddb.
        // If there is one, continue the below steps.
        // If there is no, then return to the front end and tell users.
        String pathName = mySQLService.getFilePathByName(fileName);
        if (pathName == null) {
            System.out.println("Can not search file in the file system: " + fileName);
            response.reset();
            response.setStatus(404);
            response.setHeader("Content-Type", "text/plain");
            PrintWriter writer = response.getWriter();
            writer.write("we can not find this file");
            writer.close();
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


    private boolean fetchResourcesFromNet(String netAddress, String fileName) {

        try {
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
            return true;
        } catch (IOException e) {
            System.out.println("There is an error for the network in downloading the file");
            return false;
        }

    }

}
