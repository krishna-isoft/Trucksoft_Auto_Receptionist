package com.isoft.trucksoft_autoreceptionist;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.isoft.trucksoft_autoreceptionist.service.Constant;
import com.isoft.trucksoft_autoreceptionist.service.OnlineCheck;
import com.isoft.trucksoft_autoreceptionist.service.PeopleAdapter;
import com.isoft.trucksoft_autoreceptionist.service.ServiceGenerator;
import com.isoft.trucksoft_autoreceptionist.service.Service_Api;
import com.isoft.trucksoft_autoreceptionist.service.User_model;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_page extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private TextView t_newuser;
    private TextView t_alreadyuser,t_returninguser;
    private ImageView img_logout;
    private TextView texttitle;
    Preference pref;
    private Context context;
    private String logo;
    private ImageView imglogo;
    private String title;
    LinearLayout linmain,linsub1,linsub2;
    private TextView btnsubmitmno;
    private EditText search_mno;
    private int mStatusCode;
    ProgressDialog dialog;



    //

    private TextView btnsubmit;
    private String eid;
    private String rowid="";
    Service_Api api;
    AutoCompleteTextView compname,personname,reason;
    AutoCompleteTextView automobno;
    AutoCompleteTextView autosearch;
    private String image_string3;

   signature mSignature;
    private Bitmap mBitmap;
    private String image_stringprofile="";
    LinearLayout mContent;
    View mView;
    CircleImageView imgprofile;
    private ImageView imgsign;
    private byte[] image,imagepf;
    private static String sig;
    public static byte[] pikimg;
    PeopleAdapter adapter;
    List<User_model> listusers;
    private ArrayList<String> arraytmo;
    private String empphone;
    private TextView meetings;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int PERMISSION_CAMERA = 4;
    private TextView tcancel,btncancelmno;
    private TextView teditfont1,teditfont2,teditfont3,teditfont4,teditfont5;
    Font_manager font_manager;
private Intent ink;
private int intsk=0;
    private LinearLayout lhead;
    private LinearLayout linimage;

    Location mCurrentLocation;
    private String lat="";
    private String lon="";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String straddress="";
    boolean GpsStatus;
    LocationManager locationManager;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 10; /* 2 sec */
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        context=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pref=Preference.getInstance(context);
        lhead=findViewById(R.id.lhead);
        ink=getIntent();
        if(ink.hasExtra("sk"))
        {
            intsk=ink.getIntExtra("sk",0);
        }
        listusers=new ArrayList<>();
        font_manager=new Font_manager();
        t_newuser=findViewById(R.id.tnewuser);
        t_alreadyuser=findViewById(R.id.treturninguser);
        t_returninguser=findViewById(R.id.treturninguser);
        btnsubmitmno=findViewById(R.id.btn_submitmno);
        img_logout=findViewById(R.id.logout);
        linimage=findViewById(R.id.linimage);
        texttitle=findViewById(R.id.ttle);
        imglogo=findViewById(R.id.logo);
        imgsign=findViewById(R.id.imgs);
        search_mno=findViewById(R.id.search_mno);
        search_mno.addTextChangedListener(new PhoneNumberFormattingTextWatcher(search_mno));
        imgprofile=findViewById(R.id.imgprofile);
        linmain=findViewById(R.id.lin_main);
        autosearch=findViewById(R.id.autosearch);
        linsub1=findViewById(R.id.linsub1);
        linsub2=findViewById(R.id.lin_sub2);
        btnsubmit=findViewById(R.id.btn_submit);
        tcancel=findViewById(R.id.btn_cancel);
        btncancelmno=findViewById(R.id.btn_cancelmno);
        reason=findViewById(R.id.edt_reason);
        compname=findViewById(R.id.cname);
        meetings=findViewById(R.id.meeting);
        personname=findViewById(R.id.y_name);
        automobno=findViewById(R.id.auto_mno);
        automobno.addTextChangedListener(new PhoneNumberFormattingTextWatcher(automobno));
       // buy_button=findViewById(R.id.buy_button);
        teditfont1=findViewById(R.id.teditfont1);
        teditfont1.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        teditfont2=findViewById(R.id.teditfont2);
        teditfont2.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        teditfont3=findViewById(R.id.teditfont3);
        teditfont3.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        teditfont4=findViewById(R.id.teditfont4);
        teditfont4.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        teditfont5=findViewById(R.id.teditfont5);
        teditfont5.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

     //   buy_button	.setTypeface(font_manager.get_icons("fonts/ionicons.ttf",context));

        api = ServiceGenerator.createService(Service_Api.class,context);
        logo = pref.getString("company_logo");
        title= pref.getString("cc");
        Picasso.with(getApplicationContext()).load(logo).into(imglogo);
        texttitle.setText("Welcome To : "+title);
        getdispatcher();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        CheckGpsStatus();
        if (!GpsStatus) {
            getMyLocation(context);
        }
        if(intsk==1)
        {
            linsub1.setVisibility(View.VISIBLE);
            linmain.setVisibility(View.GONE);
        }
        t_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Uri alarmSound = null;

                    alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getApplicationContext().getPackageName() + "/raw/wcme");

                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                    r.play();

                } catch (Exception e) {
                    e.printStackTrace();
                }


                personname.setError(null);
                compname.setError(null);
                autosearch.setError(null);
                automobno.setError(null);
                reason.setError(null);
                imgprofile.setImageBitmap(null);
                imgsign.setImageBitmap(null);
                teditfont1.setVisibility(View.VISIBLE);
                teditfont2.setVisibility(View.VISIBLE);
                teditfont3.setVisibility(View.VISIBLE);
              linsub2.setVisibility(View.VISIBLE);
                linmain.setVisibility(View.GONE);
               // buy_button.setVisibility(View.VISIBLE);
              //  imgprofile.setVisibility(View.GONE);
search_mno.setText("");
                personname.setText("");
                compname.setText("");
                compname.setFocusable(true);
                compname.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(compname, InputMethodManager.SHOW_IMPLICIT);

                autosearch.setText("");
                automobno.setText("");
                reason.setText("");

                personname.setEnabled(true);
               // personname.setFocusable(true);
               // personname.requestFocus();
              //  personname.invalidate();
                compname.setEnabled(true);
               // compname.setFocusable(true);
               // compname.requestFocus();
               // compname.invalidate();
                autosearch.setEnabled(true);
//                autosearch.setFocusable(true);
//                autosearch.requestFocus();
//                autosearch.invalidate();
                automobno.setEnabled(true);
//                automobno.setFocusable(true);
//                automobno.requestFocus();
//                automobno.invalidate();
                reason.setEnabled(true);
//                reason.setFocusable(true);
//                reason.requestFocus();
//                reason.invalidate();

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                insertDummyContactWrapper();

        }
        tcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intsk==1)
                {
                    Intent mIntent = new Intent(
                            Home_page.this,
                            Dashboard.class);
                    mIntent.putExtra("sk",0);
                    startActivity(mIntent);


                    finish();
                }else {
                    imgprofile.setImageBitmap(null);
                    imgsign.setImageBitmap(null);
                    linsub2.setVisibility(View.GONE);
                    linsub1.setVisibility(View.GONE);
                    linmain.setVisibility(View.VISIBLE);
                    personname.setText("");
                    compname.setText("");
                    autosearch.setText("");
                    automobno.setText("");
                    search_mno.setText("");
                    reason.setText("");
                    personname.setEnabled(true);
                    //   personname.setFocusable(true);
                    compname.setEnabled(true);
                    //  compname.setFocusable(true);
                    autosearch.setEnabled(true);
                    // autosearch.setFocusable(true);
                    automobno.setEnabled(true);
                    //  automobno.setFocusable(true);
                    reason.setEnabled(true);
                    // reason.setFocusable(true);
                    personname.setError(null);
                    compname.setError(null);
                    autosearch.setError(null);
                    automobno.setError(null);
                    reason.setError(null);
                    // buy_button.setVisibility(View.VISIBLE);
                    //  imgprofile.setVisibility(View.GONE);
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm.isAcceptingText()) {
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                } else {
//                    // writeToLog("Software Keyboard was not shown");
//                }
                }
            }
        });
        imgsign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //drawsign();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission(PERMISSION_CAMERA)) {
                        callcamerad();
                    } else {
                        Log.e("call","permission");
                        insertDummyContactWrapper();
                    }
                }else {
                    callcamerad();
                }
            }
        });
        btncancelmno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intsk==1)
                {
                    Intent mIntent = new Intent(
                            Home_page.this,
                            Dashboard.class);
                    mIntent.putExtra("sk",0);
                    startActivity(mIntent);


                    finish();

                }else {
                    imgprofile.setImageBitmap(null);
                    imgsign.setImageBitmap(null);
                    linsub2.setVisibility(View.GONE);
                    linsub1.setVisibility(View.GONE);
                    linmain.setVisibility(View.VISIBLE);
                    //  buy_button.setVisibility(View.VISIBLE);
                    //   imgprofile.setVisibility(View.GONE);
                    personname.setText("");
                    compname.setText("");
                    autosearch.setText("");
                    automobno.setText("");
                    search_mno.setText("");
                    reason.setText("");
                    personname.setEnabled(true);
                    // personname.setFocusable(true);
                    compname.setEnabled(true);
                    // compname.setFocusable(true);
                    autosearch.setEnabled(true);
                    // autosearch.setFocusable(true);
                    automobno.setEnabled(true);
                    // automobno.setFocusable(true);
                    reason.setEnabled(true);

                    personname.setError(null);
                    compname.setError(null);
                    autosearch.setError(null);
                    automobno.setError(null);
                    reason.setError(null);
                    // reason.setFocusable(true);
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm.isAcceptingText()) {
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                } else {
//                    // writeToLog("Software Keyboard was not shown");
//                }
                }
            }
        });
        meetings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                } else {
                    // writeToLog("Software Keyboard was not shown");
                }




                getdispatcher();

            }
        });
        img_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pref.putString("logged", "logged_out");
                pref.putString(Constant.BASE_URL_GUEST, "http://trucksoft.net/guestsignin/");
                Intent mIntent = new Intent(
                        Home_page.this,
                        Authentication.class);

                startActivity(mIntent);


                finish();
            }
        });
        t_returninguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               linsub1.setVisibility(View.VISIBLE);
                linmain.setVisibility(View.GONE);

            }
        });
        btnsubmitmno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // buy_button.setVisibility(View.VISIBLE);
               // imgprofile.setVisibility(View.GONE);
                imgprofile.setImageBitmap(null);
                imgsign.setImageBitmap(null);
                if(search_mno.getText().toString() !=null && search_mno.getText().toString().length()>0)
                {
                    String phn=search_mno.getText().toString();
                    getsigndetail(phn);
                }else{
                    search_mno.setError("Please enter mobile no.");
                }

            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub










                String strcname,strpname,strmno,strmeet,strreason;

                strpname=personname.getText().toString().trim();
                strmno=automobno.getText().toString().trim();
                strmeet=autosearch.getText().toString().trim();
                strcname=compname.getText().toString().trim();
                strreason=reason.getText().toString().trim();

                String pm=null;
                String pg=null;
                String cn=null;
                String rs=null;
                String ms=null;

                if(strmeet !=null && strmeet.length()>0)
                {
                    if(strmeet.contentEquals("Select"))
                    {
                        ms="Select the person";
                        Toast.makeText(context, ms, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        ms=null;
                    }
                }else {
                    ms="Select the person";
                }

                autosearch.setError(ms);
                if (strpname.length() > 0) {
                    if (strpname.length() >= 3) {
                        if (strpname.length() <= 50) {
                            if (checkname(strpname)) {
                                pg = null;
                            } else {
                                pg = "Enter a valid name e.g. : Andrew D.S";
                            }
                        } else {
                            pg = "Maximum of " + 50
                                    + " characters are allowed";
                        }
                    } else {
                        pg = "Enter minimum of " + 3
                                + " characters";
                    }

                }else
                {
                    pg="Enter your name";
                }
                personname.setError(pg);
                if (strmno.length() > 0) {
                    if (maxcheck(strmno, 15)) {
                    //    if (checkmobile(strmno)) {
                        Log.e("valz",""+isValidMobile(strmno));
                        if (isValidMobile(strmno)) {
                            pm = null;
                        } else {
                            pm = "Enter a valid mobile number e.g. : (559) 759-9700";
                        }
                    } else {
                        pm = "Maximum of 15" + " numbers are allowed";
                    }
                } else {
                    pm = "Enter a mobile number";
                }

                automobno.setError(pm);

                if(strcname !=null && strcname.length()>0 )
                {
                    cn=null;

                }else
                {
                    cn="Enter the company name";
                }
                compname.setError(cn);





                if(strreason !=null && strreason.length()>0)
                {
                    rs=null;

                }else
                {
                    rs="Enter the reason";
                }
                reason.setError(rs);
                if(pm==null && pg==null && cn==null && rs==null && ms==null && image_string3!=null && image_stringprofile !=null)
                {
                    String msgdetail=null;
                    String msgdetailsms=null;


                    msgdetail="Dear "+strmeet+","+" "+"\n"+
                            "        Mr. "+strpname+""+" is here to visit you from "+strcname+" at the front desk."
                            +"\n"+
                            "\n"+"Reason : "+strreason+""+"\n"+"Contact Number : "+strmno+"\n"
                            +"\n"
                            +"              Thanks - i-Soft";


//                    msgdetailsms="Dear "+strmeet+","+
//                            "Mr. "+strpname+""+" is here to visit you from "+strcname+" at the front desk."+
//                            " Reason : "+strreason+""+". Contact Number : "+strmno+"  Thanks, Trucksoft- Auto Receptionist";

                    msgdetailsms="Dear "+strmeet+","+
                            "Mr."+strpname+"("+strmno+")" +
                        " is here to visit you from "+strcname+" at the front desk."+
                            " Re: "+strreason+""+"  Tks, i-Soft Auto Receptionist";


                    //Log.e("msgdetail", ""+msgdetail);
                    //guestinsert(strcname,strpname,strmno,strmeet,strreason,msgdetail);
                    // sendmessagess();

//Log.e("eid","@"+eid);
if(listusers.size()>0)
{
    for(int h=0;h<listusers.size();h++)
    { String emp=autosearch.getText().toString().toUpperCase().trim();
    String listemp=listusers.get(h).getName().toUpperCase().trim();
        Log.e("emp","@"+emp);
        Log.e("listemp","@"+listemp);
    if(listemp.contentEquals(emp))
    {
       // Log.e("conddd","matched");
        if(eid !=null && eid.length()>0)
        {

        }else{
            eid=listusers.get(h).getId();
        }

        break;// just test add break key
        
    }else{
        eid="";
    }

    }
}else{
    eid="";
}
                    Log.e("eidzzz","@"+eid);
                    if(eid !=null && eid.length()>0 && !eid.contentEquals("null")) {
                        saveguestentry(strcname, strpname, strmno, strmeet, strreason, msgdetail, msgdetailsms);
                    }else{
                       // Log.e("call","@ else part");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Sorry, No Person with such Name exist. Please Type the Person that you are meeting with again.");
                                alertDialogBuilder.setPositiveButton(" OK ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                              autosearch.setText("");
                                                autosearch.requestFocus();
                                            }
                                        });



                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }else if(pm==null && pg==null && cn==null && rs==null && ms==null && image_string3==null)
                {
                    Toast.makeText(context,"Please put your Signature",Toast.LENGTH_SHORT).show();
                }else if(pm==null && pg==null && cn==null && rs==null && ms==null && image_stringprofile ==null)
                {
                    Toast.makeText(context,"Please take your picture",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getsigndetail(String phone){
        Log.e("call","dispstc");
        //dialog = new ProgressDialog(LoginActivity.this,
        //       AlertDialog.THEME_HOLO_LIGHT);
        dialog= new ProgressDialog(Home_page.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        // dialog.setTitle("Title of progress dialog.");
        dialog.setMessage("Please wait.........");

        // Set the progress dialog background color
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B53391")));

        dialog.setIndeterminate(false);
        if (OnlineCheck.isOnline(this)) {
            //dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();

            Log.e("vzzzzzzz",""+pref.getString(Constant.BASE_URL_GUEST)+"&cc="+pref.getString("cd")
            +"mobile="+phone);

            RequestBody ccbody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString("cd"));
            RequestBody phbody = RequestBody.create(MediaType.parse("text/plain"), ""+phone);

            Call<ResponseBody> call = api.getreturninguser(ccbody,phbody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        mStatusCode = response.code();
                        Log.e("mStatusCode", "mStatusCode==" + mStatusCode);
                        if (mStatusCode == Constant.SUCEESSRESPONSECODE) {
                            dialog.dismiss();
                            JSONArray result = new JSONArray(response.body().string());

                            Log.e("RESULTdd", "" + result);
                          //  arraysign=new ArrayList<>();
                            if (result != null) {
                              //  listdetails=new ArrayList<>();
                                if (result.length() > 0) {

                                    for (int i = 0; i < result.length(); i++) {
                                        try {
                                            JSONObject mObject = result.getJSONObject(i);
                                            String id = mObject.getString("id");
                                            String name = mObject.getString("name");
                                            String companyname = mObject.getString("companyname");
                                            String emp = mObject.getString("emp");
                                            String reasonc = mObject.getString("reason");
                                            String client_contact = mObject.getString("client_contact");
                                            String empname = mObject.getString("emp_name");

                                            linsub2.setVisibility(View.VISIBLE);
                                            eid=emp;
                                            rowid=id;
                                            personname.setText(name);
                                            compname.setText(companyname);
                                            autosearch.setText(empname.toUpperCase());
                                            automobno.setText(phone);
                                            reason.setText("");
                                            personname.setEnabled(false);
                                         //   personname.setFocusable(false);
                                            compname.setEnabled(false);
                                           // compname.setFocusable(false);
                                           // autosearch.setEnabled(false);
                                            //autosearch.setFocusable(false);
                                            automobno.setEnabled(false);
                                           // automobno.setFocusable(false);
                                            reason.requestFocus();
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(reason, InputMethodManager.SHOW_IMPLICIT);
                                            //reason.setFocusable(false);
                                            teditfont1.setVisibility(View.GONE);
                                            teditfont2.setVisibility(View.GONE);
                                            teditfont3.setVisibility(View.GONE);
                                            linsub2.setVisibility(View.VISIBLE);
                                            linsub1.setVisibility(View.GONE);

                                        }
                                        catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }

                                    try {
                                        Uri alarmSound = null;

                                        alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                                + "://" + getApplicationContext().getPackageName() + "/raw/wback");

                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                                        r.play();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //setEmployee(arraytmo);
                                }else
                                {
                                    //empphone=null;
                                }

                            }


                        } else if (mStatusCode == Constant.FAILURERESPONSECODE) {
                            dialog.dismiss();


                        } else {
                            dialog.dismiss();
                            if (mStatusCode == Constant.INTERNALERRORRESPONSECODE) {

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (t instanceof SocketTimeoutException) {
                        //Log.v("SocketTimeOut", "SocketTimeOutError");
                        Toast.makeText(Home_page.this, "Oops! Timeout Error!"
                                , Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }

    }

    private void saveguestentry(String strcname, String strpname, String strmno, String strmeet, String strreason,String msgdetail,String msgdetailsms) {
        // Log.e("msgdetailsms","@"+msgdetailsms);
        String ip=getLocalIpAddress();
        dialog= new ProgressDialog(Home_page.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if(lat !=null) {
            getAddressFromLocation(Double.parseDouble(lat), Double.parseDouble(lon));
        }

        dialog.setIndeterminate(false);
        if (OnlineCheck.isOnline(this)) {
            dialog.setMessage("Dear Mr. "+strpname+" I have sent a message to the person who you would like to meet,  Please wait! Thank you.");
            dialog.setCancelable(false);
            dialog.show();
            Spannable wordtoSpan = new SpannableString(msgdetail);
//Log.e("strmeet","@"+strmeet);
            RequestBody companybody = RequestBody.create(MediaType.parse("text/plain"), ""+strcname);
            RequestBody namebody = RequestBody.create(MediaType.parse("text/plain"), ""+strpname);
            RequestBody phonebody = RequestBody.create(MediaType.parse("text/plain"), ""+strmno);
            RequestBody embody = RequestBody.create(MediaType.parse("text/plain"), ""+eid);
            RequestBody empnamebody = RequestBody.create(MediaType.parse("text/plain"), ""+strmeet);
            RequestBody skbody = RequestBody.create(MediaType.parse("text/plain"), ""+intsk);
            RequestBody rowbody = RequestBody.create(MediaType.parse("text/plain"), ""+rowid);
            RequestBody reasonbody = RequestBody.create(MediaType.parse("text/plain"), ""+strreason);
            RequestBody ipaddressbody = RequestBody.create(MediaType.parse("text/plain"), ""+ip);
            RequestBody msgdetailbody = RequestBody.create(MediaType.parse("text/plain"), ""+ Html.toHtml(wordtoSpan));
            RequestBody ccbody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString("cd"));
            RequestBody smsmsgbody = RequestBody.create(MediaType.parse("text/plain"), ""+msgdetailsms);

            RequestBody latbody = RequestBody.create(MediaType.parse("text/plain"), ""+lat);
            RequestBody lonbody = RequestBody.create(MediaType.parse("text/plain"), ""+lon);
            RequestBody addressbody = RequestBody.create(MediaType.parse("text/plain"), ""+straddress);

            RequestBody guestimgbody = RequestBody.create(MediaType.parse("text/plain"), ""+image_string3);
            RequestBody profimgbody = RequestBody.create(MediaType.parse("text/plain"), ""+image_stringprofile);


//Log.e("kk",""+image_stringprofile);

//            Log.e("url",""+"http://trucksoft.net/guestsignin/guestinsertdetail.php?companyname="+strcname
//                            +"&name="+strpname
//                            +"&phone="+strmno
//                            +"&employee="+eid
//                            +"&reason="+strreason
//                            +"&ipaddress="+ip
//                            +"&msgdetail="+Html.toHtml(wordtoSpan)
//                            +"&cc="+pref.getString("cd")
//                            +"&msgdetailsms="+msgdetailsms
//                            +"&lat="+lat
//                            +"&lon="+lon
//                            +"&address="+straddress
//                    //+"&guestimg="+image_string3
//            );



            Call<ResponseBody> call = api.saveGuestentrydetails(companybody,namebody,phonebody,embody,
                    reasonbody,ipaddressbody,msgdetailbody,ccbody,guestimgbody,smsmsgbody,skbody,rowbody,latbody,lonbody,addressbody,empnamebody,profimgbody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        mStatusCode = response.code();
                        Log.e("mStatusCode", "mStatusCode==" + mStatusCode);
                        if (mStatusCode == Constant.SUCEESSRESPONSECODE) {
                            dialog.dismiss();
                            JSONObject result = new JSONObject(response.body().string());

                            Log.e("RESULT", "" + result);
                            String status = result.getString("status");
                            if (status.contentEquals("1")) {
                                if(result.has("message"))
                                {
                                    String msg=result.getString("message");
                                    Toast.makeText(Home_page.this, ""+msg
                                            , Toast.LENGTH_SHORT).show();

									Intent mIntent2 = new Intent(Home_page.this,
                                            Dashboard.class);

									startActivity(mIntent2);
                                    finish();
                                }
                            }else{
//								String msg=result.getString("message");
//								Toast.makeText(Registration.this, ""+msg
//										, Toast.LENGTH_SHORT).show();
                                String msg=result.getString("message");
                                Toast.makeText(Home_page.this, ""+msg
                                        , Toast.LENGTH_SHORT).show();

                                Intent mIntent2 = new Intent(Home_page.this,
                                        Dashboard.class);

                                startActivity(mIntent2);
                                finish();
                            }

                        } else if (mStatusCode == Constant.FAILURERESPONSECODE) {
                            dialog.dismiss();


                        } else {
                            dialog.dismiss();
                            if (mStatusCode == Constant.INTERNALERRORRESPONSECODE) {

                            }
                        }
                    } catch (JSONException e) {
                        Log.e("jeec","@"+e.toString());
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e("jeeciob","@"+e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (t instanceof SocketTimeoutException) {
                        Log.e("SocketTimeOut", "SocketTimeOutError");
//                        Toast.makeText(Home_page.this, "Oops! Timeout Error!"
//                                , Toast.LENGTH_SHORT).show();
                        Intent mIntent2 = new Intent(Home_page.this,
                                Dashboard.class);

                        startActivity(mIntent2);
                        finish();
                    }
                    dialog.dismiss();
                }
            });
        }

    }
    public static String getLocalIpAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)){
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac==null){
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length()>0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

//    public boolean checkmobile(String str) {
//        if (str.contains("+")) {
//            if ((str.length() == 13)
//                    && (str.substring(0, 1).contentEquals("+"))) {
//                int count = str.replaceAll("[^+]", "").length();
//                if (count < 2) {
//                    if ((str.matches("[0-9+]+$"))) {
//                        return true;
//                    }
//                }
//            }
//        } else if (str.substring(0, 1).contentEquals("0")) {
//            if ((str.matches("[0-9]+$")) && (str.length() == 11)) {
//                return true;
//            }
//        } else if ((str.matches("[0-9]+$") && (str.length() == 10))) {
//            return true;
//        }
//        return false;
//
//    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 15;
        }
        return false;
    }

    public boolean maxcheck(String str, int value) {
        if (str.length() <= value) {
            return true;
        }
        return false;
    }
    public boolean checkname(String str) {
        if (str.matches("[a-zA-Z. ]+$")
                && (!(str.substring(0, 1).matches("[. ]+$")))) {
            return true;
        }
        return false;
    }
    private void callcamerad()
    {
        //linimg.setVisibility(View.GONE);

        View view = View.inflate(context, R.layout.signature, null);

        TextView btnsave = (TextView) view.findViewById(R.id.btsave);
        TextView btncancel = (TextView) view.findViewById(R.id.btcancel);
        mContent = (LinearLayout) view.findViewById(R.id.signature_ll_signView);


        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.FILL_PARENT);

        mView = mContent;
        final Dialog dialogsk = new Dialog(context,R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialogsk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogsk.setContentView(view);


        Window window =dialogsk.getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialogsk.show();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogsk.dismiss();
                imgprofile.setImageBitmap(null);
                mView.setDrawingCacheEnabled(true);
                mSignature.save(mView);
                imgsign.setImageBitmap(mBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image = stream.toByteArray();

                image_string3 = Base64
                        .encodeToString(image, Base64.DEFAULT);
                callcamera();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogsk.dismiss();

            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
// Get last known recent location.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {



            }
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            //Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            lat=String.valueOf(mCurrentLocation.getLatitude());
            lon=String.valueOf(mCurrentLocation.getLongitude());
            Log.e("latlat",""+lat);
            Log.e("lonlon",""+lon);
        }
        // Begin polling for new location updates.
        try {
            startLocationUpdates();
        }catch (Exception e)
        {
            Log.e("loc",""+e.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            //	Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            //Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        lat=String.valueOf(location.getLatitude());
        lon=String.valueOf(location.getLongitude());
        Log.e("lat",""+lat);
        Log.e("lon",""+lon);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }


    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(HALF_STROKE_WIDTH);
        }

        public void save(View v) {
            //Log.v("log_tag", "Width: " + v.getWidth());
            //Log.v("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(),
                        mContent.getHeight(), Bitmap.Config.RGB_565);

            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                v.draw(canvas);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // bitmap = BitmapFactory.decodeFile(filePath, o2);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);




            } catch (Exception e) {
                //Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            //mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        linimage.setVisibility(View.VISIBLE);
        imgprofile.setVisibility(View.VISIBLE);
        image_stringprofile ="";
        sig=pref.getString(Constant.signok);
        //Log.e("useridfdf", pref.getString(Interfacemulty.userid));
        //Log.e("sigsig", pref.getString(Interfacemulty.signok));
        if(sig.contentEquals("1"))
        {
            //	linimg.setVisibility(View.VISIBLE);
            byte[] imagebyte=pikimg;
            Bitmap bmp = BitmapFactory.decodeByteArray(imagebyte, 0, imagebyte.length);
           // buy_button.setVisibility(View.GONE);

            imgprofile.setImageBitmap(Bitmap.createScaledBitmap(bmp, imgprofile.getWidth(),
                    imgprofile.getHeight(), false));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagepf = stream.toByteArray();

            image_stringprofile = Base64
                    .encodeToString(imagepf, Base64.DEFAULT);
            //Log.e("utype", ""+image_stringprofile);

            //ipload();
        }

    }

    private void callcamera()
    {

        //	pref.putString(Interfacemulty.userid, userid);
        //	pref.putString(Interfacemulty.status, ab);
        //	pref.putString(Interfacemulty.utype, utype);
        View view = View.inflate(context, R.layout.drivermsghead, null);

        TextView btntv = (TextView) view.findViewById(R.id.btnone);

        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        //dialog = new Dialog(this, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);


        Window window =dialog.getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        dialog.setCancelable(false);

        btntv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent i = new Intent(Home_page.this,CameraView.class);
                startActivityForResult(i, 999);
            }
        });


    }



    private void getdispatcher(){
        Log.e("call","dispstc");
        //dialog = new ProgressDialog(LoginActivity.this,
        //       AlertDialog.THEME_HOLO_LIGHT);
        dialog= new ProgressDialog(Home_page.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        // dialog.setTitle("Title of progress dialog.");
        dialog.setMessage("Please wait.........");

        // Set the progress dialog background color
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B53391")));

        dialog.setIndeterminate(false);
        if (OnlineCheck.isOnline(this)) {
            //dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();


            RequestBody ccbody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString("cd"));

            Call<ResponseBody> call = api.getDispatcher(ccbody);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        mStatusCode = response.code();
                        Log.e("mStatusCode", "mStatusCode==" + mStatusCode);
                        if (mStatusCode == Constant.SUCEESSRESPONSECODE) {
                            dialog.dismiss();
                            JSONArray result = new JSONArray(response.body().string());

                            Log.e("RESULT", "" + result);
                            arraytmo=new ArrayList<>();
                            if (result != null) {
                                listusers=new ArrayList<>();
                                if (result.length() > 0) {

                                    for (int i = 0; i < result.length(); i++) {
                                        try {
                                            JSONObject mObject = result.getJSONObject(i);
                                            //
                                            String id = mObject.getString("id");
                                            String name = mObject.getString("name");
                                            empphone=mObject.getString("phone");
                                            //Log.e("empphoned", "-"+empphone);
                                            User_model udh=new User_model();
                                            udh.setId(id);
                                            udh.setName(name);
                                            if(empphone !=null && empphone.length()>0)
                                            {
                                                arraytmo.add(name+">>"+id+">>"+empphone);
                                                udh.setPhone(empphone);
                                            }else
                                            {
                                                arraytmo.add(name+">>"+id+">>"+"Demo");
                                                udh.setPhone("Demo");
                                            }

                                            listusers.add(udh);

                                        }
                                        catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                    callautosearch();
                                    //setEmployee(arraytmo);
                                }else
                                {
                                    empphone=null;
                                }

                            }


                        } else if (mStatusCode == Constant.FAILURERESPONSECODE) {
                            dialog.dismiss();


                        } else {
                            dialog.dismiss();
                            if (mStatusCode == Constant.INTERNALERRORRESPONSECODE) {

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    if (t instanceof SocketTimeoutException) {
                        //Log.v("SocketTimeOut", "SocketTimeOutError");
                        Toast.makeText(Home_page.this, "Oops! Timeout Error!"
                                , Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }

    }

    private void callautosearch()
    {
        eid="";
        adapter = new PeopleAdapter(this, R.layout.emp_list, R.id.lbl_name, listusers);
        autosearch.setThreshold(3);
        autosearch.setAdapter(adapter);
        autosearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eid=listusers.get(position).getId();
                //			Log.e("emp", ""+eid);
                String emp=listusers.get(position).getPhone();
                if(emp !=null && emp.length()>0)
                {
                    if(emp.contentEquals("Demo"))
                    {

                    }else
                    {
                        empphone=emp;
                    }
                }
                Log.e("empphone",""+empphone);
                autosearch.setText(listusers.get(position).getName().toUpperCase());
                //alert.dismiss();
            }
        });

    }


    @Override
    public void onBackPressed() {
//        Intent mIntent = new Intent(
//                Home_page.this,
//                Dashboard.class);
//        mIntent.putExtra("sk",1);
//
//        startActivity(mIntent);
//
//
//        finish();
        Log.e("intsk",""+intsk);
        if(intsk==1)
        {
            Intent mIntent = new Intent(
                    Home_page.this,
                    Dashboard.class);
            mIntent.putExtra("sk",0);
            startActivity(mIntent);


            finish();
        }else {
            intsk=1;
            imgprofile.setImageBitmap(null);
            imgsign.setImageBitmap(null);
            linsub2.setVisibility(View.GONE);
            linsub1.setVisibility(View.GONE);
            linmain.setVisibility(View.VISIBLE);
            personname.setText("");
            compname.setText("");
            autosearch.setText("");
            automobno.setText("");
            search_mno.setText("");
            reason.setText("");
            personname.setEnabled(true);
            //   personname.setFocusable(true);
            compname.setEnabled(true);
            //  compname.setFocusable(true);
            autosearch.setEnabled(true);
            // autosearch.setFocusable(true);
            automobno.setEnabled(true);
            //  automobno.setFocusable(true);
            reason.setEnabled(true);
            // reason.setFocusable(true);
            personname.setError(null);
            compname.setError(null);
            autosearch.setError(null);
            automobno.setError(null);
            reason.setError(null);
            // buy_button.setVisibility(View.VISIBLE);
            //  imgprofile.setVisibility(View.GONE);
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm.isAcceptingText()) {
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                } else {
//                    // writeToLog("Software Keyboard was not shown");
//                }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
//        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
//            permissionsNeeded.add("CAMERA");
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            permissionsNeeded.add("Write Contacts");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean bool = false;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    bool = false;
            }
            bool = true;
        }
        return bool;
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    protected void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
            }
        }
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }
    public void getMyLocation(final Context cons){
        // Log.e("common dislog","location");
        if(mGoogleApiClient==null )
        { //Log.e("mGoogleApiClient","empty");
            buildGoogleApiClient();
        }
        if(mGoogleApiClient!= null)
        {
            //Log.e("mGoogleApiClient","not connected");
            try{
                mGoogleApiClient.connect();
            }catch (IllegalStateException e)
            {
                // Log.e("IllegalStateException", e.toString());
            }

        }
        if(mGoogleApiClient!=null) {
            // Log.e("mGoogleApiClient","okok");
            if (mGoogleApiClient.isConnected()) {
                // Log.e("mGoogleApiClient","okokdone");

                int permissionLocation = ContextCompat.checkSelfPermission(cons,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    // Log.e("permission","ok");
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(10000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest,  this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            //   Log.e("statuserh",""+status.getStatusCode());
                            switch (status.getStatusCode()) {

                                case LocationSettingsStatusCodes.SUCCESS:
                                    // Log.e("calling",""+"gps");
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(cons,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mCurrentLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    //Log.e("calling",""+"gps fail");
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult((Activity) cons,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                        // Log.e("calling gps erorrrrrrr",""+e.toString());
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();


        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }


    }
    public  String getAddressFromLocation(final double latitude, final double longitude) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                if(address.getAddressLine(0) !=null && address.getAddressLine(0).length()>0 && !address.getAddressLine(0).contentEquals("null"))
                {
                    sb.append(address.getAddressLine(0)).append("\n");
                }else {

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                }
                straddress = sb.toString();
                if(straddress.contains("India"))
                {
                    straddress="2020 W Whitendale Ave, Visalia, CA 93277, USA..";
                }
                //Log.e("address",""+straddress);
            }
        } catch (IOException e) {
            //Log.e("df", "Unable connect to Geocoder", e);
        }


        return straddress;
    }

    public boolean checkPermission(int permission) {

        if (ActivityCompat.checkSelfPermission(this, getPermission(permission)) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }

    }
    public String getPermission(int permis) {

        String permission = null;

        switch (permis) {

            case PERMISSION_CAMERA:
                permission=Manifest.permission.CAMERA;
                return permission;


        }
        return permission;
    }
}
