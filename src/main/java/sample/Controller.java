package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;

public class Controller extends ActionEvent{

    @FXML
    TextField textFieldFileName;

    @FXML
    TextField textFieldStampName;

    @FXML
    TextField textFieldDirectoryName;

    @FXML
    Button buttonSelectFile;

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

    @FXML
    Spinner spinnerProcentSizeStamp;

    @FXML
    Rectangle horizontalPage;

    @FXML
    Rectangle verticalPage;

    @FXML
    Label labelScanning;

    @FXML
    Label labelWidthPage;

    @FXML
    Label labelHeightPage;

    @FXML
    Label labelError;


    private File selectedFile = null;
    private File selectedStamp = null;
    private File selectedDirectory = null;


    @FXML
    void initialize(){
        TextFieldChangeListener textFieldChangeListener = new TextFieldChangeListener();
        textFieldFileName.textProperty().addListener(textFieldChangeListener);
        textFieldStampName.textProperty().addListener(textFieldChangeListener);
        textFieldDirectoryName.textProperty().addListener(textFieldChangeListener);
    }

    
    public void selectFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF file");
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) textFieldFileName.setText(selectedFile.getName());
    }

    public void selectStamp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JPG file");
        selectedStamp = fileChooser.showOpenDialog(null);
        if (selectedFile != null ) textFieldStampName.setText(selectedStamp.getName());
    }

    public void selectDirectory(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select directory");
        selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) textFieldDirectoryName.setText(selectedDirectory.getPath());
    }

    public void merge(ActionEvent actionEvent) {
        //GetPropertyValues properties = new GetPropertyValues();
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

    private class TextFieldChangeListener implements ChangeListener<String> {

        public void changed(ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {

            if(Service.validationFile(textFieldFileName) && Service.validationStamp(textFieldStampName) && selectedDirectory!=null){
                try {
                    PdfReader reader = new PdfReader(selectedFile.getPath());
                    Double widthPdfPage = Double.valueOf(reader.getPageSize(reader.getNumberOfPages()).toString().split("x")[0].split(" ")[1]);
                    Double heightPdfPage = Double.valueOf(reader.getPageSize(reader.getNumberOfPages()).toString().split("x")[1].split(" ")[0]);
                    Image image = Image.getInstance(selectedStamp.getPath());
                    float widthStamp = image.getWidth();
                    float heightStamp = image.getHeight();
                    //float newWidthStamp = (float) (widthPdfPage*0.3);
                    float newWidthStamp = 180.0f;
                    Double procentSizeStamp = (newWidthStamp/widthStamp)*100.0;
                    spinnerProcentSizeStamp.setDisable(false);
                    spinnerProcentSizeStamp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100, procentSizeStamp));

                    labelScanning.setVisible(true);

                    if(widthPdfPage>heightPdfPage){
                        verticalPage.setVisible(false);
                        horizontalPage.setVisible(true);
                    }else{
                        horizontalPage.setVisible(false);
                        verticalPage.setVisible(true);
                    }

                    labelHeightPage.setText(heightPdfPage+" px");
                    labelWidthPage.setText(widthPdfPage+" px");
                    spinnerStampX.setDisable(false);
                    spinnerStampY.setDisable(false);
                    spinnerStampX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, widthPdfPage, widthPdfPage*0.6));
                    spinnerStampY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, heightPdfPage, heightPdfPage*0.1));

                    buttonMerge.setDisable(false);


                    reader.close();
                } catch (Exception e) {
                    labelError.setText("Error: "+e);
                }

            }
            else {
                if (!Service.validationFile(textFieldFileName) && selectedFile!=null) labelError.setText("Nie poprawny format dokumentu");
                else if(!Service.validationStamp(textFieldStampName) && selectedStamp!=null ) labelError.setText("Nie poprawny format pieczątki");
                else labelError.setText("");
                buttonMerge.setDisable(true);
            }
        }


    }






}
