package com.helpfooter.steve.petcure.common;

import java.sql.Struct;

/**
 * Created by steve on 2016/4/30.
 */
public class StaticVar {
    public static final String UPLOADURL = "http://www.myhkdoc.com/petcure/ui/fileupload.php?field=uploadfile";
    public static String UpdateVersionUrl="http://www.myhkdoc.com/PetCureApp/update.xml";
    public static String UploadModulePet="PET";
    public static String PetImageUrl="http://www.myhkdoc.com/PetCure/UI/upload/pet/";
    public static String PosterShowerUrl="http://www.myhkdoc.com/PetCure/UI/shower.php?poster_id=";

    public static String WechatAppId="wxe8d9406cf695d6da";
    public static String WechatAppSecret="4848047098788c8e433e6548388d439a";
    public static String WechatState="com.helpfooter.steve.petcure";

    public static class APIUrl{
        public static String LoginReg= "http://www.myhkdoc.com/petcure/api/Member/login_reg.php";
        public static String PosterCreate= "http://www.myhkdoc.com/petcure/api/Poster/create.php";
        public static String PosterList= "http://www.myhkdoc.com/petcure/api/Poster/list.php";
        public static String PosterPhoto= "http://www.myhkdoc.com/petcure/api/poster/photo.php";

    }

}
