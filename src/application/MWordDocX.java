package application;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

public class MWordDocX {
	
	DBMSConnection dbms = new DBMSConnection(DBMSConnection.getURL(),DBMSConnection.getUsername() , DBMSConnection.getPassword());

    public void CreateDoc() {
        try {
            //Creating the Word document
            String wordFilePath = "output.docx";
            mainTextDocument(wordFilePath);

            //Converting it to a PDF
            String pdfFilePath = "MrRizz.pdf";
            convertWordToPDF(wordFilePath, pdfFilePath);

            System.out.println("PDF created successfully at: " + pdfFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void mainTextDocument(String filePath) {
    	 
    	Project project = dbms.loadProject();
    	
    	try (XWPFDocument document = new XWPFDocument()) {
            // Add a title
            addTitle(document, "Buhler Group Contract Example");

            // Add paragraphs for the story
            addParagraph(document, "Thank you for choosing Buhler Group, your email is " + project.getCustEmail() +
                    " It is amazing to get new customers etc... etc...");

            addParagraph(document, "Buhler was founded to make you eat, anyways you want a " + project.getMTypeA() + 
                    " and also a " +project.getMTypeB() + " that has a (Machine Trait) and mentioned (Another Machine Trait), placed within etc...");

            addParagraph(document, "Your request was issued in " + project.getIssued());

            addParagraph(document, "You want a size A of " + project.getSizeA() + 
                    " Size B of " + project.getSizeB());

            addParagraph(document, "You were served by " + project.getUserEmail());

            // Save the document
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create a Word document
    private void createWordDocument(String filePath) throws Exception {
        try (XWPFDocument document = new XWPFDocument()) {
            // Create a paragraph and add text
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("sample");

            // Save the document
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
        }
    }

   
    

    // Method to add a title to the document
    private void addTitle(XWPFDocument document, String titleText) {
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(titleText);
        titleRun.setBold(true);
        titleRun.setFontSize(20);
    }

    // Method to add a paragraph to the document
    private void addParagraph(XWPFDocument document, String paragraphText) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setSpacingAfter(200); // Add spacing after the paragraph

        XWPFRun run = paragraph.createRun();
        run.setText(paragraphText);
        run.setFontSize(12);
    }
    
    // Method to convert a Word document to PDF
    private void convertWordToPDF(String wordFilePath, String pdfFilePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(new File(wordFilePath));
             XWPFDocument document = new XWPFDocument(fis)) {

            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfFilePath));
            pdfDocument.open();

            // Iterate through the Word document content and add it to the PDF
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                pdfDocument.add(new Paragraph(paragraph.getText()));
            }

            pdfDocument.close();
        }
    }
}
