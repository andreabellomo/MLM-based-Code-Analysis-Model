package com.mycompany;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.Statement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class FileParser {

    public static List<Statement> parseFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            input.append(scanner.nextLine()).append("\n");
        }
        scanner.close();

        CompilationUnit compilationUnit = StaticJavaParser.parse(input.toString());
        return compilationUnit.findAll(Statement.class);
    }
    
}
