package com.isoft.trucksoft_autoreceptionist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.isoft.trucksoft_autoreceptionist.service.Constant;
import com.squareup.picasso.Picasso;

public class Dashboard extends AppCompatActivity {
    private TextView texttitle;
    Preference pref;
    private Context context;
    private String logo,title;
    private ImageView imglogo;
    private ImageView img_logout;
    private TextView tsignin,tsignout;
    private LinearLayout lhead;
    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashbrd);
        context=this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pref=Preference.getInstance(context);
        lhead=findViewById(R.id.lhead);
//        final int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            if(pref.getString("cd").contentEquals("wwe")||pref.getString("cd").contentEquals("WWE")) {
//                lhead.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.wpress));
//
//            }else{
//                lhead.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.wpack));
//
//            }
//        } else {
//            if(pref.getString("cd").contentEquals("wwe")||pref.getString("cd").contentEquals("WWE")) {
//                lhead.setBackground(ContextCompat.getDrawable(context, R.drawable.wpress));
//
//            }else{
//                lhead.setBackground(ContextCompat.getDrawable(context, R.drawable.wpack));
//
//            }
//        }

        img_logout=findViewById(R.id.logout);
        texttitle=findViewById(R.id.ttle);
        imglogo=findViewById(R.id.logo);
        tsignin=findViewById(R.id.tsign);
        tsignout=findViewById(R.id.tsignout);
        logo = pref.getString("company_logo");
        Log.e("logo","@"+logo);
        title= pref.getString("cc");
        Picasso.with(getApplicationContext()).load(""+logo).into(imglogo);
        texttitle.setText("Welcome To : "+title);

        img_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pref.putString("logged", "logged_out");
                pref.putString(Constant.BASE_URL_GUEST, "http://trucksoft.net/guestsignin/");
                Intent mIntent = new Intent(
                        Dashboard.this,
                        Authentication.class);

                startActivity(mIntent);


                finish();
            }
        });

        tsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Dashboard.this,
                        Home_page.class);
                mIntent.putExtra("sk",0);
                startActivity(mIntent);


                finish();
            }
        });

        tsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(
                        Dashboard.this,
                        Logout_visitor.class);
                mIntent.putExtra("sk",1);

                startActivity(mIntent);


                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to close this page?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
