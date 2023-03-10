package com.Kaybee.demo.controller;

import com.Kaybee.demo.model.ProjectModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.Kaybee.demo.constants.SpringAPIConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
public class SpringInitialiserAPIController {
//    @PostMapping(path="/springApi",consumes="application/json")
    @GetMapping("/sapi")
    public  void createBySpringInitAPI(
//            @RequestBody ProjectModel model
    ) throws IOException {
        String projectArtifactId = "my-spring-boot-app";
        String projectGroupId = "com.example";
        String projectVersion = "0.0.1-SNAPSHOT";
        String projectName = "My Spring Boot App";
        String projectDescription = "A custom Spring Boot app created using Spring Initializr API";
        String javaVersion = "11";
        String packaging = "jar";
        String dependencies = "web,data-jpa";

//        String projectArtifactId = model.getArtifactId();
//        String projectGroupId = model.getGroupId();
//        String projectVersion = "0.0.1-SNAPSHOT";
//        String projectName = model.getProjectName();
//        String projectDescription = model.getProjectDesc();
//        String javaVersion = "11";
//        String packaging = "jar";
//        String dependencies = (model.getDependencies()).toString();

        String apiUrl = SpringAPIConstants.SPRING_INIT_API_URL
                + projectArtifactId + "&groupId=" + projectGroupId + "&artifactId=" + projectArtifactId + "&version="
                + projectVersion + "&name=" + projectName + "&description=" + projectDescription + "&packageName="
                + projectGroupId + "." + projectArtifactId + "&packaging=" + packaging + "&javaVersion=" + javaVersion
                + "&dependencies=" + dependencies;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Encoding", "gzip");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = connection.getHeaderField("Content-Disposition").split("filename=")[1];
            InputStream inputStream = connection.getInputStream();
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String filePath = System.getProperty("user.dir") + File.separator + zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(Paths.get(filePath));
                } else {
                    Path parent = Paths.get(filePath).getParent();
                    if (parent != null && Files.notExists(parent)) {
                        Files.createDirectories(parent);
                    }
                    FileOutputStream outputStream = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                }
                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.close();

            System.out.println("Project created successfully");
        } else {
            System.out.println("Failed to create project: " + responseCode);
        }
    }
}
