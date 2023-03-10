package com.Kaybee.demo.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class ProjectModel {

    @NotNull String GroupId;
    @NotNull String ArtifactId;
    @NotNull String ProjectName;
    @NotNull String ProjectDesc;

    List<String> dependencies;




}
