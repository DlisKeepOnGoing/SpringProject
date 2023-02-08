package com.newdim.SpringDemo.entity;

import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "file_data")
@IdClass(FileData.FilePrimaryKey.class)
public class FileData {

    static class FilePrimaryKey implements Serializable {
        private Integer id;
        private String name;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private String filePath;
    private String sourcePath;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
