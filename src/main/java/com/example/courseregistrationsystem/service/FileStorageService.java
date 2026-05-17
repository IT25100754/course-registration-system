package com.example.courseregistrationsystem.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    public List<String> readLines(String fileName) {

        List<String> lines = new ArrayList<>();

        try {

            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                lines.add(line);
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    public void writeLines(
            String fileName,
            List<String> lines) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String s, String data) {

    }

    public List<String> readFromFile(List<String> s) {

        return s;
    }

    public void appendLine(String studentsFile, String fileString) {
    }
}