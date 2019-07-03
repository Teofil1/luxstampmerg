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
import java.io.IOException;

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
    Button buttonSetDefaultValues;

    @FXML
    Spinner<Double>  spinnerStampX;

    @FXML
    Spinner<Double>  spinnerStampY;

    @FXML
    Spinner<Double> spinnerProcentSizeStamp;

    @FXML
    Rectangle horizontalPage;

    @FXML
    Rectangle verticalPage;

    @FXML
    Label labelPageSize;

    @FXML
    Label labelWidthPage;

    @FXML
    Label labelHeightPage;

    @FXML
    Label labelError;

    private File selectedFile = null;
    private File selectedStamp = null;
    private File selectedDirectory = null;
    private GetPropertyValues properties = new GetPropertyValues();

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
        if (selectedFile != null) textFieldFileName.setText(selectedFile.getPath());
    }

    public void selectStamp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JPG file");
        selectedStamp = fileChooser.showOpenDialog(null);
        if (selectedStamp != null ) textFieldStampName.setText(selectedStamp.getPath());
    }

    public void selectDirectory(ActionEvent actionEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select directory");
        selectedDirectory = chooser.showDialog(null);
        if (selectedDirectory != null) textFieldDirectoryName.setText(selectedDirectory.getPath());
    }

    public void merge(ActionEvent actionEvent) {
        try {
            PdfReader reader = new PdfReader(textFieldFileName.getText());
            PdfStamper stamper = new PdfStamper(reader,
                    new FileOutputStream(textFieldDirectoryName.getText()+Service.createNameOutFile(textFieldDirectoryName.getText())));
            Image stamp = Image.getInstance(textFieldStampName.getText());
            stamp.scalePercent(spinnerProcentSizeStamp.getValue().floatValue());
            stamp.setAbsolutePosition(spinnerStampX.getValue().floatValue(), spinnerStampY.getValue().floatValue());
            PdfContentByte over = stamper.getOverContent(reader.getNumberOfPages());
            over.addImage(stamp);
            saveValuesInConfig(reader, stamp);
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveValuesInConfig(PdfReader reader, Image stamp){
        try {
            Double widthPdfPage = (double) reader.getPageSize(reader.getNumberOfPages()).getWidth();
            Double heightPdfPage = (double) reader.getPageSize(reader.getNumberOfPages()).getHeight();
            Double widthStamp = (double) stamp.getWidth();
            Double coefficientStampsPositionX = spinnerStampX.getValue()/widthPdfPage;
            Double coefficientStampsPositionY = spinnerStampY.getValue()/heightPdfPage;
            Double newWidthStamp = (widthStamp*spinnerProcentSizeStamp.getValue())/100.0;
            properties.setLastUsedCoefficientStampsPositionX(coefficientStampsPositionX);
            properties.setLastUsedCoefficientStampsPositionY(coefficientStampsPositionY);
            properties.setLastUsedWidthStamp(newWidthStamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValues(Double coefficientStampsPositionX, Double coefficientStampsPositionY, Double newWidthStamp){
        try {
            PdfReader reader = new PdfReader(selectedFile.getPath());
            Double widthPdfPage = ((int) (reader.getPageSize(reader.getNumberOfPages()).getWidth()*100.0))/100.0;
            Double heightPdfPage = ((int) (reader.getPageSize(reader.getNumberOfPages()).getHeight()*100.0))/100.0;
            Image stamp = Image.getInstance(selectedStamp.getPath());
            Double widthStamp = (double)stamp.getWidth();
            Double procentSizeStamp = (newWidthStamp/widthStamp)*100.0;
            spinnerProcentSizeStamp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, procentSizeStamp));
            spinnerStampX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0,
                    widthPdfPage, widthPdfPage*coefficientStampsPositionX));
            spinnerStampY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0,
                    heightPdfPage, heightPdfPage*coefficientStampsPositionY));
            showSymbolicPictureOfPage(widthPdfPage, heightPdfPage);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            labelError.setText("Error: "+e);
        }
    }

    public void setDefaultValues(){
        try {
            setValues(properties.getDefaultCoefficientStampsPositionX(),
                    properties.getDefaultCoefficientStampsPositionY(),properties.getDefaultWidthStamp());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSymbolicPictureOfPage(Double widthPdfPage, Double heightPdfPage){
        labelPageSize.setVisible(true);
        if(widthPdfPage>heightPdfPage){
            verticalPage.setVisible(false);
            horizontalPage.setVisible(true);
        }else{
            horizontalPage.setVisible(false);
            verticalPage.setVisible(true);
        }
        labelHeightPage.setText(heightPdfPage+" px");
        labelWidthPage.setText(widthPdfPage+" px");
    }

    private class TextFieldChangeListener implements ChangeListener<String> {

        public void changed(ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {

            if(Service.validationFile(textFieldFileName) && Service.validationStamp(textFieldStampName) && selectedDirectory!=null){
                labelError.setText("");
                spinnerProcentSizeStamp.setDisable(false);
                spinnerStampX.setDisable(false);
                spinnerStampY.setDisable(false);
                buttonMerge.setDisable(false);
                buttonSetDefaultValues.setDisable(false);
                try {
                    setValues(properties.getLastUsedCoefficientStampsPositionX(),
                            properties.getLastUsedCoefficientStampsPositionY(),properties.getLastUsedWidthStamp());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (!Service.validationFile(textFieldFileName) && selectedFile!=null)
                    labelError.setText("Nie poprawny format dokumentu");
                else if(!Service.validationStamp(textFieldStampName) && selectedStamp!=null )
                    labelError.setText("Nie poprawny format pieczątki");
                else labelError.setText("");
                buttonMerge.setDisable(true);
                labelPageSize.setVisible(false);
                verticalPage.setVisible(false);
                horizontalPage.setVisible(false);
                spinnerStampX.setDisable(true);
                spinnerStampY.setDisable(true);
                spinnerProcentSizeStamp.setDisable(true);
                buttonSetDefaultValues.setDisable(true);
                labelWidthPage.setText("");
                labelHeightPage.setText("");
            }
        }




    }






}
