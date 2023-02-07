package com.newdim.SpringDemo;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TestABC {

    @Test
    public void test() throws EncoderException, URIException {
        String url = "http://10.122.90.20/大文件/制动器_mir.asm";
        String encodeUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
        System.out.println(encodeUrl);
        // 1. 手动逐个字符替换
        encodeUrl = encodeUrl
                .replace("%2F", "/")
                .replace("%3A",":")
                .replace("%40", "@");
        System.out.println(encodeUrl);
        // 2. 使用 URIUtil 只编码特殊字符
        String encoded = URIUtil.encodeQuery(url, "UTF-8");
        System.out.println(encoded);
        // 3. 使用 toASCIIString 只编码特殊字符
        URI uri = URI.create(url);
        String uriEncoded = uri.toASCIIString();
        System.out.println(uriEncoded);
    }

    @Test
    public void test_FileURL() {
        String path = "D://IdeaProjects//SpringDemo//files//制动器_mir.asm";
        File file = new File(path);
        System.out.println(file.getName());
        System.out.println(URLEncoder.encode(file.getName(), StandardCharsets.UTF_8));
    }
}
