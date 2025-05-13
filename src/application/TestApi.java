package application;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class TestApi {

    public static void main(String[] args) {
        // Path to save the Word document
        String filePath = "StoryDocument.docx";

        // Create the document
        createStoryDocument(filePath);

        System.out.println("Word document created successfully at: " + filePath);
    }

    public static void createStoryDocument(String filePath) {
        
    }

}