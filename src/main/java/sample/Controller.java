package sample;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;


import javax.print.DocFlavor;
import java.awt.event.ActionListener;
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
    TextField textFieldCoefficientSizeStamp;

    @FXML
    Button buttonSelectStamp;

    @FXML
    Button buttonSelectDirectory;

    @FXML
    Button buttonMerge;

    @FXML
    Spinner spinnerStampX;

    @FXML
    Spinner spinnerStampY;

    File selectedFile = null;
    File selectedStamp = null;
    File selectedDirectory = null;

    
    public void selectFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF file");
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null && Service.validationFile(selectedFile)) {
            buttonSelectStamp.setDisable(false);
            try {
                PdfReader reader = new PdfReader(selectedFile.getPath());
                Double widthPdfPage = Double.valueOf(reader.getPageSize(reader.getNumberOfPages()).toString().split("x")[0].split(" ")[1]);
                Double heightPdfPage = Double.valueOf(reader.getPageSize(reader.getNumberOfPages()).toString().split("x")[1].split(" ")[0]);
                System.out.println(widthPdfPage+" "+heightPdfPage);
                spinnerStampX.setDisable(false);
                spinnerStampY.setDisable(false);
                spinnerStampX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, widthPdfPage, widthPdfPage*0.6));
                spinnerStampY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, heightPdfPage, heightPdfPage*0.1));
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         else {
             buttonSelectStamp.setDisable(true);
             spinnerStampX.setDisable(true);
             spinnerStampY.setDisable(true);
            spinnerStampX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 0, 0));
            spinnerStampY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 0, 0));
        }
    }

    public void selectStamp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JPG file");
        selectedStamp = fileChooser.showOpenDialog(null);
        if (selectedFile != null && Service.validationFile(selectedFile)) {
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
            float k = image.getHeight()/image.getWidth();
            image.scaleAbsolute(widthPdfPage*0.3f, k*widthPdfPage*0.3f);
            image.setAbsolutePosition(widthPdfPage*0.6f, heightPdfPage*0.1f);
            PdfContentByte over = stamper.getOverContent(reader.getNumberOfPages());
            over.addImage(image);
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void actionPerformed(java.awt.event.ActionEvent e) {
        System.out.println("!!!!!!!!!!!!!!!!");
        if (selectedStamp!=null && selectedFile!=null && selectedDirectory!=null)
            if(Service.validationFile(selectedFile) && Service.validationStamp(selectedStamp))
                buttonMerge.setDisable(false);
            else buttonMerge.setDisable(true);
    }*/
}
