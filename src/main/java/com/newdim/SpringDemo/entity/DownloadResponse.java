package com.newdim.SpringDemo.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
public class DownloadResponse {
    private String url;
    private String name;
    private String message;
}
