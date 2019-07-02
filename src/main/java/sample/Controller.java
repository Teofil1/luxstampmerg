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
        //GetPropertyValues properties = new GetPropertyValues();

        try {
            PdfReader reader = new PdfReader(textFieldFileName.getText());
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(textFieldDirectoryName.getText()+"/out.pdf"));
            Image stamp = Image.getInstance(textFieldStampName.getText());
            Double newStampWidth = (stamp.getWidth()/100.0)*spinnerProcentSizeStamp.getValue();
            Double newStampHeight = (stamp.getHeight()/100.0)*spinnerProcentSizeStamp.getValue();
            stamp.scalePercent(spinnerProcentSizeStamp.getValue().floatValue());
            stamp.setAbsolutePosition(spinnerStampX.getValue().floatValue(), spinnerStampY.getValue().floatValue());
            PdfContentByte over = stamper.getOverContent(reader.getNumberOfPages());
            over.addImage(stamp);
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
                    labelError.setText("");
                    PdfReader reader = new PdfReader(selectedFile.getPath());
                    Double widthPdfPage = ((int) (reader.getPageSize(reader.getNumberOfPages()).getWidth()*100.0))/100.0;
                    Double heightPdfPage = ((int) (reader.getPageSize(reader.getNumberOfPages()).getHeight()*100.0))/100.0;
                    Image stamp = Image.getInstance(selectedStamp.getPath());
                    Double widthStamp = (double)stamp.getWidth();
                    Double newWidthStamp = 180.0;
                    Double procentSizeStamp = (newWidthStamp/widthStamp)*100.0;
                    spinnerProcentSizeStamp.setDisable(false);
                    spinnerProcentSizeStamp.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, procentSizeStamp));


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
                    spinnerStampX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, widthPdfPage, widthPdfPage*0.6));
                    spinnerStampY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, heightPdfPage, heightPdfPage*0.1));

                    buttonMerge.setDisable(false);

                    reader.close();
                } catch (Exception e) {
                    labelError.setText("Error: "+e);
                }

            }
            else {
                if (!Service.validationFile(textFieldFileName) && selectedFile!=null) labelError.setText("Nie poprawny format dokumentu");
                else if(!Service.validationStamp(textFieldStampName) && selectedStamp!=null ) labelError.setText("Nie poprawny format pieczątki");
                buttonMerge.setDisable(true);
                spinnerStampX.setValueFactory(null);
                spinnerStampY.setValueFactory(null);
                spinnerProcentSizeStamp.setValueFactory(null);
                labelScanning.setVisible(false);
                verticalPage.setVisible(false);
                horizontalPage.setVisible(false);
                spinnerStampX.setDisable(true);
                spinnerStampY.setDisable(true);
                spinnerProcentSizeStamp.setDisable(true);
                labelWidthPage.setText("");
                labelHeightPage.setText("");
            }
        }


    }






}
