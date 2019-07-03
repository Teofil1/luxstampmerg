package sample;

import javafx.scene.control.TextField;
import java.io.File;


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

   public static String createNameOutFile(String path) {
       String nameOutFile = "/Merged document.pdf";
       File file = new File(path + nameOutFile);
       int index = 1;
       while (file.exists()) {
           index++;
           nameOutFile = "/Merged document (" + index + ").pdf";
           file = new File(path+nameOutFile);
       }
       return nameOutFile;
   }
}
