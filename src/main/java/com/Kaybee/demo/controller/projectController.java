package com.Kaybee.demo.controller;


import com.Kaybee.demo.DemoApplication;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class projectController {

    @GetMapping("/bundle")
        public String get(HttpServletResponse response) throws Exception {
        ZipFolder(response);
        return "HI";
        }


        public static void ZipFolder(HttpServletResponse response) throws Exception {

            // Get the folder to be zipped
            String folderName = "ProjectInstance";
            ClassLoader classLoader = DemoApplication.class.getClassLoader();
            File folder = new File(classLoader.getResource(folderName).getFile());

            // Get the zip file path and create the output stream
            final ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

            // Create a recursive function to add all files and subdirectories in the folder to the zip file
            zipFolder(folder, folder.getName(), zos);

            // Close the output stream
            zos.close();
        }

        private static void zipFolder(@NotNull File folder, String parentFolderName, ZipOutputStream zos) throws Exception {
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    zipFolder(file, parentFolderName + "/" + file.getName(), zos);
                } else {
                    byte[] buffer = new byte[1024];
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(parentFolderName + "/" + file.getName()));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
        }


}


