package com.isoft.trucksoft_autoreceptionist;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.isoft.trucksoft_autoreceptionist.service.ServiceGenerator;
import com.isoft.trucksoft_autoreceptionist.service.Service_Api;
import com.squareup.picasso.Picasso;

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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Logout_visitor  extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Context context;
    Preference pref;
    LinearLayout mContent;
    signature mSignature;
    View mView;
    private ImageView imgsign;
    private Bitmap mBitmap;
    private String image_string3;
    private byte[] image;
    private TextView btnsubmitmno,btncancelmno;
    private LinearLayout lhead;
    private int mStatusCode;
    ProgressDialog dialog;
    Service_Api api;
    private EditText search_mno;
    private ImageView img_logout;
    private ImageView imglogo;

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
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logoutvisitor);
        context=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pref=Preference.getInstance(context);
        lhead=findViewById(R.id.lhead);
        lhead=findViewById(R.id.lhead);
        search_mno=findViewById(R.id.search_mno);
        search_mno.addTextChangedListener(new PhoneNumberFormattingTextWatcher(search_mno));
//        final int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            if(pref.getString("cd").contentEquals("wwe")||pref.getString("cd").contentEquals("WWE")) {
//                lhead.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.wpress));
//                lhead.getBackground().setAlpha(120);
//            }else{
//                lhead.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.wpack));
//                lhead.getBackground().setAlpha(120);
//            }
//        } else {
//            if(pref.getString("cd").contentEquals("wwe")||pref.getString("cd").contentEquals("WWE")) {
//                lhead.setBackground(ContextCompat.getDrawable(context, R.drawable.wpress));
//                lhead.getBackground().setAlpha(120);
//            }else{
//                lhead.setBackground(ContextCompat.getDrawable(context, R.drawable.wpack));
//                lhead.getBackground().setAlpha(120);
//            }
//        }
        img_logout=findViewById(R.id.logout);
        imgsign=findViewById(R.id.imgs);
        imglogo=findViewById(R.id.logo);
        String logo = pref.getString("company_logo");
        Picasso.with(getApplicationContext()).load(logo).into(imglogo);
        btnsubmitmno=findViewById(R.id.btn_submitmno);
        btncancelmno=findViewById(R.id.btn_cancelmno);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            insertDummyContactWrapper();

        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        CheckGpsStatus();
        if (!GpsStatus) {
            getMyLocation(context);
        }
        imgsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callcamerad();
            }
        });

        img_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pref.putString("logged", "logged_out");
                pref.putString(Constant.BASE_URL_GUEST, "http://trucksoft.net/guestsignin/");
                Intent mIntent = new Intent(
                        Logout_visitor.this,
                        Authentication.class);

                startActivity(mIntent);


                finish();
            }
        });
        api = ServiceGenerator.createService(Service_Api.class,context);
        btncancelmno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Logout_visitor.this,
                        Dashboard.class);
                mIntent.putExtra("sk",1);

                startActivity(mIntent);


                finish();
            }
        });
     btnsubmitmno.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String strmno=search_mno.getText().toString().trim();


             String pm=null;
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

             search_mno.setError(pm);
             if(pm==null && image_string3!=null)
             {
                 String phn=search_mno.getText().toString();
                 setlogout(phn);
             }else {
                 Toast.makeText(context,"Please put your signature",Toast.LENGTH_SHORT).show();
             }
         }
     });
    }
private void setlogout(String phn)
{
    // Log.e("msgdetailsms","@"+msgdetailsms);
    String ip=getLocalIpAddress();
    dialog= new ProgressDialog(Logout_visitor.this);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    if(lat !=null) {
        getAddressFromLocation(Double.parseDouble(lat), Double.parseDouble(lon));
    }

    dialog.setIndeterminate(false);
    if (OnlineCheck.isOnline(this)) {
        dialog.setMessage(" You have been sign out,  Please wait! Thank you.");
        dialog.setCancelable(false);
        dialog.show();

        RequestBody companybody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody namebody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody phonebody = RequestBody.create(MediaType.parse("text/plain"), ""+phn);
        RequestBody embody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody emnamebody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody skbody = RequestBody.create(MediaType.parse("text/plain"), "2");
        RequestBody rowbody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody reasonbody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody ipaddressbody = RequestBody.create(MediaType.parse("text/plain"), ""+ip);
        RequestBody msgdetailbody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody ccbody = RequestBody.create(MediaType.parse("text/plain"), ""+pref.getString("cd"));
        RequestBody smsmsgbody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody guestimgbody = RequestBody.create(MediaType.parse("text/plain"), ""+image_string3);
        RequestBody profimgbody = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody latbody = RequestBody.create(MediaType.parse("text/plain"), ""+lat);
        RequestBody lonbody = RequestBody.create(MediaType.parse("text/plain"), ""+lon);
        RequestBody addressbody = RequestBody.create(MediaType.parse("text/plain"), ""+straddress);

//Log.e("kk",""+image_stringprofile);

//        Log.e("url",""+"http://trucksoft.net/guestsignin/guestinsertdetail.php?companyname="
//                        +"&name="
//                        +"&phone="+phn
//                        +"&employee="
//                        +"&reason="
//                        +"&ipaddress="+ip
//                        +"&msgdetail="
//                        +"&cc="+pref.getString("cd")
//                        +"&msgdetailsms="
//                //+"&guestimg="+image_string3
//        );
//        Log.e("url",""+"http://trucksoft.net/guestsignin/guestinsertdetail.php?companyname="
//                        +"&name="
//                        +"&phone="+phn
//                        +"&employee="
//                        +"&reason="
//                        +"&sign_detail=2"
//                        +"&ipaddress="+ip
//                        +"&msgdetail="
//                        +"&cc="+pref.getString("cd")
//                        +"&msgdetailsms="
//                            +"&lat="+lat
//                            +"&lon="+lon
//                            +"&address="+straddress
//                    //+"&guestimg="+image_string3
//            );


        Call<ResponseBody> call = api.saveGuestentrydetails(companybody,namebody,phonebody,embody,
                reasonbody,ipaddressbody,msgdetailbody,ccbody,guestimgbody,smsmsgbody,skbody,rowbody,latbody,lonbody,addressbody,emnamebody,profimgbody);
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
//                                Toast.makeText(Logout_visitor.this, ""+msg
//                                        , Toast.LENGTH_SHORT).show();

                                calldialogg();
                            }
                        }else{
//								String msg=result.getString("message");
//								Toast.makeText(Registration.this, ""+msg
//										, Toast.LENGTH_SHORT).show();
                            String msg=result.getString("message");
//                            Toast.makeText(Logout_visitor.this, ""+msg
//                                    , Toast.LENGTH_SHORT).show();

                        calldialogg();
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
//                        Toast.makeText(Home_page.this, "Oops! Timeout Error!"
//                                , Toast.LENGTH_SHORT).show();
                  calldialogg();
                }
                dialog.dismiss();
            }
        });
    }

}

    private void calldialogg()
    {
        View view = View.inflate(context, R.layout.thankdialog, null);
        final Dialog dialogcl = new Dialog(context, R.style.DialogTheme);
        dialogcl.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcl.setContentView(view);
        dialogcl.show();


        try {
            Uri alarmSound = null;

            alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/thankk");

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                dialogcl.dismiss();
                Intent mIntent2 = new Intent(Logout_visitor.this,
                        Dashboard.class);

                startActivity(mIntent2);
                finish();
            }
        }.start();

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

                mView.setDrawingCacheEnabled(true);
                mSignature.save(mView);
                imgsign.setImageBitmap(mBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image = stream.toByteArray();

                image_string3 = Base64
                        .encodeToString(image, Base64.DEFAULT);

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
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(
                Logout_visitor.this,
                Dashboard.class);
        mIntent.putExtra("sk",1);

        startActivity(mIntent);


        finish();
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
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
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION);
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


}
