package com.newdim.SpringDemo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class DownloadController {
    private static final String FILE_DIR = "D://IdeaProjects//SpringDemo//files//";

    // The remote host -> SpringServer -> Save as file stored in my server
    @RequestMapping("/downloadToServer")
    public void downloadToServer(@RequestParam String netAddress, @RequestParam String fileName) throws IOException {

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

    // SpringServer -> Client -> Servlet Response
    @RequestMapping("/downloadToClient")
    public void downloadToClient(HttpServletResponse response, @RequestParam String fileName) throws IOException {
        // Input
        File file = new File(FILE_DIR + fileName);
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

}
