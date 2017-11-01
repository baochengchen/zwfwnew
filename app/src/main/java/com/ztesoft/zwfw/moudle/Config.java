package com.ztesoft.zwfw.moudle;

/**
 * Created by BaoChengchen on 2017/8/6.
 */

public class Config {


    //http://220.163.118.118
    //http://192.168.50.109:8080/portal
    //http://192.168.50.110/portal
    //http://192.168.0.113:8080/portal
    //http://10.45.10.134
    //public static final String BASE_URL = "http://220.163.118.118";
    //public static final String BASE_URL = "http://192.168.50.110/portal";

    public static final String BASE_URL = "http://10.45.67.193:8080";

    //public static final String BASE_URL = "http://220.163.118.118";

    public static final String URL_LOGIN = "/rest/login";

    public static final String URL_USER_CURRENT = "/rest/user/current";

    public static final String URL_MESSAGE_LIST = "/rest/message/qryList";

    public static final String URL_QRYWORKLIST = "/rest/sgsp/mywork/qryMyWorkList";

    public static final String URL_QUERYMYWORKTASKS = "/rest/sgsp/myworktasks/queryMyWorkTasks";

    public static final String URL_QUERYWARN = "/rest/app/querywarn";

    public static final String URL_QUERYTIMEOUT = "/rest/app/querytimeout";

    public static final String URL_SEARCHFLOWBUTTONDTO = "/rest/commons/searchFlowButtonDto";

    public static final String URL_EXCUTEBIZPROCESS = "/rest/commons/excuteBizProcess";

    public static final String URL_QRYEXCEPTION = "/rest/supervision/myworks/queryMyExceptionWorksList";

    public static final String URL_QRYINTERACTION = "/rest/web/websiteinteraction/queryInterAction";

    public static final String URL_QRYSUPERVISE = "/rest/supervision/supervisionwork/queryMySupervisionList";

    public static final String URL_QRYEVALUATE = "/rest/supervision/serviceEvaluate/QueryServiceEvaluateTasks";

    public static final String URL_SEARCHFRONTSTARTINFO = "/rest/commons/searchFrontStartInfo";

    public static final String URL_SEARCHFRONTEXCUTELIST = "/rest/commons/searchFrontExcuteList";

    public static final String URL_QUERYBIZINFOBYID = "/rest/supervision/supervisionwork/queryBizInfoById";

    public static final String URL_QUERYAPPWORKS = "/rest/app/queryappworks";

    public static final String URL_ATTACHMENT = "/rest/attachment";

    public static final String URL_USERS_QUERY = "/rest/users/query";

    public static final String URL_TALK_ADDCHAT = "/rest/app/talk/addchat";

    public static final String URL_TALK_CHATLIST = "/rest/app/talk/chatlist";

    public static final String URL_TALK_ADDCOMMENT = "/rest/app/talk/addcomment";

    public static final String URL_TALK_GETCOMMENTS = "/rest/app/talk/getcomments";

    public static final String URL_TALK_UPDATECHAT = "/rest/app/talk/updatechat";

    public static final String URL_TALK_GETNEWCOUNT = "/rest/app/talk/getnewcount";

    public static final String URL_CHECKVERSION = "/rest/app/version";

    public static final String URL_DOWNLOADFILE = "/rest/download";

    public static final String URL_USERS = "/rest/users";

    public static final String URL_LOGOUT = "/rest/logout";

    public static final String URL_SELF = "/rest/staffs/self";

    public static final String URL_SELF_PWD = "/rest/users/self/pwd";

    public static final String USERINFO = "userinfo";
    public static final String IS_LOGIN = "islogin";
    public static final String CURRENT_ROLE = "currentRole";

    public static enum RoleType {
        OSP("OSP", 1), OJD("OJD", 2), AJD("AJD", 3);

        private String name;
        private int index;

        RoleType(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }
    }

    public static final int TYPE_MINE = 0;
    public static final int TYPE_TOME = 1;
    public static final int TYPE_PUBLIC = 2;
}
