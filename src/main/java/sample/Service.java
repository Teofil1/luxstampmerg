package sample;

import java.io.File;

public class Service {

    public static boolean validationFile(File file){
        return getFileExtension(file).equals("pdf");
    }

    public static boolean validationStamp(File file){
        return (getFileExtension(file).equals("png") || getFileExtension(file).equals("jpg"));
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

}
