package org.pwc.com.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JMeterResultAnalyzer {

    public static String analyzeResults(String resultFilePath) {
        try {
            File resultFile = new File(resultFilePath);
            if (resultFile.exists()) {
                return new String(Files.readAllBytes(Paths.get(resultFilePath)));
            } else {
                return "Result file not found.";
            }
        } catch (Exception e) {
            return "Error reading result file: " + e.getMessage();
        }
    }
}