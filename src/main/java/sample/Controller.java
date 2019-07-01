package sample;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;

public class Controller {

    @FXML
    TextField textFieldFileName;

    @FXML
    TextField textFieldStampName;

    @FXML
    Button buttonMerge;

    File selectedFile = null;
    File selectedStamp = null;
    
    public void selectFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.setTitle("Select PDF file");
        if (selectedFile != null) {
            textFieldFileName.setText(selectedFile.getName());
            if (selectedStamp!=null) {
                if (Service.validationFile(selectedFile) && Service.validationStamp(selectedStamp))
                    buttonMerge.setDisable(false);
                else buttonMerge.setDisable(true);
            }
        }



    }

    public void selectStamp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        selectedStamp = fileChooser.showOpenDialog(null);
        fileChooser.setTitle("Select JPG file");
        if (selectedStamp != null) {
            textFieldStampName.setText(selectedStamp.getName());
            if(selectedFile!=null) {
                if (Service.validationFile(selectedFile) && Service.validationStamp(selectedStamp))
                    buttonMerge.setDisable(false);
                else buttonMerge.setDisable(true);
            }
        }
    }

    public void merge(ActionEvent actionEvent) {
        GetPropertyValues properties = new GetPropertyValues();
        String pathToFile = selectedFile.getPath();
        String pathToStamp = selectedStamp.getPath();
        String outFile = "src/out.pdf";

        try {
            PdfReader reader = new PdfReader(pathToFile);
            float widthPdfPage = Float.valueOf(reader.getPageSize(reader.getNumberOfPages()).toString().split("x")[0].split(" ")[1]);
            float heightPdfPage = Float.valueOf(reader.getPageSize(reader.getNumberOfPages()).toString().split("x")[1].split(" ")[0]);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
            Image image = Image.getInstance(pathToStamp);
            System.out.println(widthPdfPage);
            System.out.println(heightPdfPage);
            System.out.println(image.getWidth());
            System.out.println(image.getHeight());
            image.scaleAbsolute(widthPdfPage*0.2f, widthPdfPage*0.2f);
            image.setAbsolutePosition(widthPdfPage*0.6f, heightPdfPage*0.1f);
            System.out.println(reader.getPageSize(reader.getNumberOfPages()));

            PdfContentByte over = stamper.getOverContent(2);
            over.addImage(image);
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
