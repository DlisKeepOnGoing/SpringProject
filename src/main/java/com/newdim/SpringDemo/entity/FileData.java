package com.newdim.SpringDemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "file_data")
@IdClass(FileData.FilePrimaryKey.class)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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

}
