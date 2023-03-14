package com.Kaybee.demo.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
public class WebClientController {

    @GetMapping("/web")
    public  void createBySpringInitAPI(
//            @RequestBody ProjectModel model
    ) throws IOException {
//        String initializrUrl = "https://start.spring.io";
//        String projectName = "Kaybee-project";
//        String packaging = "jar";
//        String javaVersion = "11";
//        String[] dependencies = {"web"};
//        String groupId = "com.example";
//        String artifactId = "my-project";
//        String version = "0.0.1-SNAPSHOT";
//        String[] privateRepos = {
////                "http://my-private-repo.com/repository/maven-releases"
//        };
//
//        WebClient webClient = WebClient.builder().baseUrl(initializrUrl).build();
//
//        String url = String.format("%s/starter.zip", initializrUrl);
//        // response body chained to a string
//         webClient.post()
//                .uri(url)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .bodyValue(buildRequestBody(projectName, packaging, javaVersion, dependencies, groupId, artifactId, version, privateRepos))
//                .retrieve()
////                .bodyToMono(String.class)
////                .block();
//                .bodyToFlux(DataBuffer.class)
//                .reduce(DataBufferUtils::join)
//                 .map(dataBuffer -> {
//                     try {
//                         File file = new File("starter.zip");
//                         FileOutputStream fileOutputStream = new FileOutputStream(file);
//                         fileOutputStream.getChannel().write(dataBuffer.asByteBuffer());
//                         fileOutputStream.close();
//                         DataBufferUtils.release(dataBuffer);
//                         return file;
//                     } catch (Exception e) {
//                         throw new RuntimeException("Failed to create starter project ZIP file", e);
//                     }
//                 })
//                 .block();
//
//
//
//        // save the response body as a file
//        // ...
//
////        File outputFile = new File("output.zip");
////        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
////            fos.write(body.getBytes());
////        }
//
//
//    }
//
//    private static String buildRequestBody(String projectName, String packaging, String javaVersion, String[] dependencies, String groupId, String artifactId, String version, String[] privateRepos) {
//        return String.format("{\"type\":\"%s\",\"name\":\"%s\",\"language\":\"java\",\"bootVersion\":\"2.6.3\",\"groupId\":\"%s\",\"artifactId\":\"%s\",\"version\":\"%s\",\"description\":\"Demo project for Spring Boot\",\"packageName\":\"%s\",\"packaging\":\"%s\",\"javaVersion\":\"%s\",\"dependencies\":[%s],\"repositories\":[%s]}", packaging, projectName, groupId, artifactId, version, groupId, packaging, javaVersion, buildDependencies(dependencies), buildRepositories(privateRepos));
//    }
//
//    private static String buildDependencies(String[] dependencies) {
//        StringBuilder sb = new StringBuilder();
//        for (String dependency : dependencies) {
//            sb.append("\"").append(dependency).append("\",");
//        }
//        return sb.toString().replaceAll(",$", "");
//    }
//
//    private static String buildRepositories(String[] privateRepos) {
//        StringBuilder sb = new StringBuilder();
//        for (String privateRepo : privateRepos) {
//            sb.append("{\"name\":\"private-repo\",\"url\":\"").append(privateRepo).append("\"},");
//        }
//        return sb.toString().replaceAll(",$", "");
//    }

        WebClient webClient = WebClient.builder()
                .baseUrl("https://start.spring.io")
                .build();

        Mono<byte[]> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/starter.zip")
                        .queryParam("type","maven-project")
                        .queryParam("dependencies", "web")
                        .queryParam("language", "java")
                        .queryParam("bootVersion", "2.6.2")
                        .queryParam("groupId", "com.example")
                        .queryParam("artifactId", "demo")
                        .queryParam("name", "demo")
                        .queryParam("description", "Demo project")
                        .queryParam("packageName", "com.example.demo")
                        .build())
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class);

//        Path path = Paths.get("demo.zip");
//        Files.write(path, Objects.requireNonNull(response.block()));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Objects.requireNonNull(response.block()));
        try (ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            System.out.println(System.getProperty("user.dir"));
            while (zipEntry != null) {
                System.out.println("Zip = "+zipEntry.getName());
                String filePath = System.getProperty("user.dir") + File.separator + zipEntry.getName();
                Path dir = Paths.get(filePath);
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(dir);
                } else {
                    Path parent = dir.getParent();
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
//                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
                System.out.println("Project created successfully");
            }
        } catch (IOException e) {
            System.out.println("Failed to create project: " + e);
            e.printStackTrace();

        }



    }



}
