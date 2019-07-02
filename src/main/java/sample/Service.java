package sample;

import javafx.scene.control.TextField;

public class Service {

    public static boolean validationFile(TextField nameFile){
        return getFileExtension(nameFile).equals("pdf");
    }

    public static boolean validationStamp(TextField nameFile){
        return (getFileExtension(nameFile).equals("png") || getFileExtension(nameFile).equals("jpg") || getFileExtension(nameFile).equals("bmp"));
    }

    private static String getFileExtension(TextField nameFile) {
        String nameFileString = nameFile.getText();
        if(nameFileString.lastIndexOf(".") != -1 && nameFileString.lastIndexOf(".") != 0)
            return nameFileString.substring(nameFileString.lastIndexOf(".")+1);
        else return "";
    }

}
