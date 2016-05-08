package com.helpfooter.steve.petcure.common;

import java.sql.Struct;

/**
 * Created by steve on 2016/4/30.
 */
public class StaticVar {
    public static final String UPLOADURL = "http://www.myhkdoc.com/petcure/ui/fileupload.php?field=uploadfile";
    public static String UpdateVersionUrl="http://www.myhkdoc.com/PetCureApp/update.xml";
    public static String UploadModulePet="PET";

    public static class APIUrl{
        public static String LoginReg= "http://www.myhkdoc.com/petcure/api/Member/login_reg.php";
        public static String PosterCreate= "http://www.myhkdoc.com/petcure/api/Poster/create.php";
    }

}
