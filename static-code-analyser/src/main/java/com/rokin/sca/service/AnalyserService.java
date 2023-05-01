package com.rokin.sca.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.rokin.sca.utils.InheritedClasses;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class AnalyserService {

    private List<String> findImports(CompilationUnit cu, String prefix) {
        List<String> imports = new ArrayList<>();
        for (ImportDeclaration importDeclaration : cu.findAll(ImportDeclaration.class)) {
            if(importDeclaration.getNameAsString().contains(prefix)) {
                String importString = importDeclaration.getNameAsString();
                String className = importString.substring(importString.lastIndexOf('.') + 1);
                imports.add(className);
            }
//            if (id.getNameAsString().equals("org.springframework.web.bind.annotation.RestController")) {
//                return true;
//            } else if (id.getNameAsString().equals("org.springframework.web.bind.annotation") && id.isAsterisk()) {
//                return true;
//            }
        }

        return imports;
    }

    private List<File> getSourceFiles(String directoryOrFile) {
        if (directoryOrFile.endsWith(".java")) { // not a directory, but a single java file
            return new ArrayList<>(Collections.singletonList(new File(directoryOrFile)));
        } else {
            return (List<File>) FileUtils.listFiles(new File(directoryOrFile), new String[]{"java"}, true);
        }
    }

    public Map<String, List<String>> getDependencyData(String projectPath) throws FileNotFoundException {
        List<File> allClasses = getSourceFiles(projectPath);
        Map<String, List<String>> dependencies = new HashMap<>();

        // Oops! Hardcoded it! Take as input from the user
        String prefix = "net";

        for (File classFile : allClasses) {
            CompilationUnit cu = StaticJavaParser.parse(classFile);
            List<String> imports = findImports(cu, prefix);

            dependencies.put(classFile.getName().replace(".java", ""), imports);
        }

        return dependencies;
    }

    public String getInheritanceData(String projectPath) throws FileNotFoundException {
        List<File> allClasses = getSourceFiles(projectPath);
        for (File classFile : allClasses) {
            CompilationUnit cu = StaticJavaParser.parse(classFile);
            VoidVisitor<Void> methodinherts = new InheritedClasses();
            System.out.println(classFile.getName());
            methodinherts.visit(cu, null);
        }

        return "gg";
    }


    public List<String> getDirectories(File folder) {
        File[] files = folder.listFiles();

        List<String> directories = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
//                String[] directoryPath = file.getAbsolutePath().split("/");
                directories.add(file.getAbsolutePath());
            }
        }

        return directories;
    }

    public Map<String, List<String>> getMicroserviceClasses(String projectPath) throws FileNotFoundException {
        List<String> directories = getDirectories(new File(projectPath));

        Map<String, List<String>> microserviceClasses = new HashMap<>();
        for (String directory : directories) {
            if (!directory.contains("service")) {
                continue;
            }

            List<File> allClasses = getSourceFiles(directory);

            List<String> classes = new ArrayList<>();
            for (File classFile : allClasses) {
                classes.add(classFile.getName().replace(".java", ""));
            }

            String[] directoryPath = directory.split("/");
            String directoryName = directoryPath[directoryPath.length - 1];
            microserviceClasses.put(directoryName, classes);
        }

        return microserviceClasses;
    }

}
