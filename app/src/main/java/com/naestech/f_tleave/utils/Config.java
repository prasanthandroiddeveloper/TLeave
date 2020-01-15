package com.naestech.f_tleave.utils;

public class Config {

 private static final String BASE_URL = "https://tripnetra.com/prasanth/androidphpfiles/emp_leave/";

  public static final String EMPDETAILS = BASE_URL+"EmpDetails.php";//empdetails act
  public static final String PROFILE = BASE_URL+"emp_persnl_info.php"; //profile frag
  public static final String GETSPINNERDATA = BASE_URL+"getsppinerdata.php";//applyleavefrag
  public static final String FETCHFCMTOKEN = BASE_URL+"fetch_fcmtoken.php";//applyleavefrag
  public static final String GETNOT = BASE_URL+"sendnotnav.php";//applyleavefrag
  public static final String GETDATA = BASE_URL+"fetch_FromDate.php";//applyleavefrag
  public static final String UPDATERSN = BASE_URL+"updatersn.php";//applyleavefrag

  public static final String LEAVEINSERTDATA = BASE_URL+"insert_leaveinfo.php";//applyleavefrag

  public static final String LEAVECONFIRMATION = BASE_URL+"Leave_confirmation.php";//approveleaveact
  public static final String LEAVEAPPROVE = BASE_URL+"LApprove.php";//approveleaveact
  public static final String FETCHFCMDATA = BASE_URL+"fetch_dataa.php";//approveleaveact
  public static final String EMPNAMES = BASE_URL+"empnames.php";//assignemplysfrag
  public static final String SUPPEMPNAMES = BASE_URL+"SupEnames.php";//assignweekoff_frag
  public static final String INSERTOKEN = BASE_URL+"Insert_token.php";//Dashboardact
  public static final String LEAVEDATAINSERT = BASE_URL+"leave_datainsert.php";//Dashboardact
  public static final String LEAVECOUNT = BASE_URL+"testLeaveCount.php";//Dashboardact
  public static final String RECYCLERSTATUS = BASE_URL+"Rcyclrstatus.php";//LConfirmationfrag
  public static final String DELETEONSWIPE = BASE_URL+"DeleteOnSwipe.php";//LConfirmationfrag,LeaveStatusFrag
  public static final String GETSTATUS = BASE_URL+"get_status.php";//Leavestatusfrag
  public static final String LOGIN = BASE_URL+"emp_login.php";//LOGIN_FRAG//replaced with test4
  public static final String UPDATEPASS = BASE_URL+"updtpass.php";//LOGIN_FRAG
  public static final String LREPORT = BASE_URL+"LReport.php";//REPORT_FRAG
  public static final String FETCHNOT = BASE_URL+"fetch_not.php";//SELECTEDEMPLYSACT
  public static final String ASSIGN = BASE_URL+"assign2.php";//SELECTEDEMPLYSACT
  public static final String EMPREG = BASE_URL+"emp_det_reg.php";//SIGNUPFRAG
  public static final String INSERTWEEKOFF = BASE_URL+"insertweekoff.php";//SELECTWEEKOFF
  public static final String GETPROFILE = BASE_URL+"profiledetails.php";//Profileedit//newly added
  public static final String UPDATEPROF = BASE_URL+"profileupdate.php";//Profileedit//newly added
  public static final String FETCHWEEKOFF = BASE_URL+"FetchWeekoff.php";//SELECTWEEKOFF
  public static final String GETNAME = BASE_URL+"getname.php";//SELECTWEEKOFF
  public static final String UPDATELEAVE = BASE_URL+"updateleave.php";//UPDATELEAVE

//extra
 public static final String UPDATEWEEKOFF = BASE_URL+"uweekoff.php";//UPDATEWEEKOFF
 public static final String INSERTWEEKDAY = BASE_URL+"assigndayname.php";//INSERTWEEKDAY
 public static final String DELETEMPDETAILS = BASE_URL+"dellogdetails.php";//INSERTWEEKDAY

}
