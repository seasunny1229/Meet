package com.example.framework.file;

public class ApplicationFileNameManager {

    // photo raw file name from camera
    public static final String RAW_PHOTO_FILE_NAME = "raw_photo.jpg";

    // cropped photo file name
    public static final String CROPPED_PHOTO_FILE_NAME = "cropped_photo.jpg";

    // uploaded cropped photo name
    public static String getCroppedPhotoName(String uid){
        return uid + "_" + CROPPED_PHOTO_FILE_NAME;
    }


}
