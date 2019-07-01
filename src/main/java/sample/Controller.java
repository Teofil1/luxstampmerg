package sample;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;

public class Controller {

    @FXML
    TextField textFieldFileName;

    @FXML
    TextField textFieldStampName;

    @FXML
    TextField textFieldDirectoryName;

    @FXML
    Button buttonMerge;

    File selectedFile = null;
    File selectedStamp = null;
    File selectedDirectory = null;

    
    public void selectFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF file");
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            textFieldFileName.setText(selectedFile.getName());
            if (selectedStamp!=null && selectedDirectory!=null) {
                if (Service.validationFile(selectedFile) && Service.validationStamp(selectedStamp))
                    buttonMerge.setDisable(false);
                else buttonMerge.setDisable(true);
            }
        }
    }

    public void selectStamp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JPG file");
        selectedStamp = fileChooser.showOpenDialog(null);
        if (selectedStamp != null) {
            textFieldStampName.setText(selectedStamp.getName());
            if(selectedFile!=null && selectedDirectory!=null) {
                if (Service.validationFile(selectedFile) && Service.validationStamp(selectedStamp))
                    buttonMerge.setDisable(false);
                else buttonMerge.setDisable(true);
            }
        }
    }

    public void selectDirectory(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select directory");
        selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) {
            textFieldDirectoryName.setText(selectedDirectory.getPath());
            if (selectedStamp!=null && selectedFile!=null) {
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
        String outFile = selectedDirectory.getPath()+"/out.pdf";
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
            image.setAbsolutePosition(properties.getStampsPositionX(), properties.getStampsPositionY());
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
