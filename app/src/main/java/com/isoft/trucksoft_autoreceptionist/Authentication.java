package com.isoft.trucksoft_autoreceptionist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.isoft.trucksoft_autoreceptionist.service.Constant;
import com.isoft.trucksoft_autoreceptionist.service.OnlineCheck;

import com.isoft.trucksoft_autoreceptionist.service.ServiceGenerator;
import com.isoft.trucksoft_autoreceptionist.service.Service_Api;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authentication extends Activity {
	private Button btnsubmit;
	private EditText edtcode;
	private Preference pref;
	ProgressDialog dialog;
	String stcode;
	final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

	Service_Api api;
	private int mStatusCode;
private Context context;
	ReviewManager reviewManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		btnsubmit = (Button) findViewById(R.id.login_bt_submit);
		edtcode = (EditText) findViewById(R.id.edt_code);
		edtcode.addTextChangedListener(tt);
		//edtcode.setText("WW9530");
		context=this;
		pref = Preference.getInstance(this);
		reviewManager = ReviewManagerFactory.create(Authentication.this);
		if(pref.getString(Constant.BASE_URL_GUEST)==null || pref.getString(Constant.BASE_URL_GUEST).length()==0) {
			pref.putString(Constant.BASE_URL_GUEST, "http://trucksoft.net/guestsignin/");
		}
		api = ServiceGenerator.createService(Service_Api.class,context);
		String str=pref.getString("logged");
		//callrating();
		if(str !=null && str.length()>0)
		{
			if(str.contentEquals("logged_in")){
			

//				Intent mIntent = new Intent(
//						Authentication.this,
//						Registration.class);
//
//				startActivity(mIntent);

				Intent mIntent = new Intent(
						Authentication.this,
						Dashboard.class);

				startActivity(mIntent);
				
				
				finish();
			}
		}
		insertDummyContactWrapper();
		
		//getLocalIpAddress();
		//Log.e("ipo", ""+getLocalIpAddress());

		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str_code = edtcode.getText().toString().trim();
				stcode= edtcode.getText().toString().trim();
				if (str_code != null && str_code.length() > 0) {

//					if(str_code.contentEquals("wwp") || str_code.contentEquals("WWP"))
//					{
//						pref.putString(Constant.BASE_URL_GUEST,"http://wildwoodpacking.com/guestsignin/");
//					}else{
						pref.putString(Constant.BASE_URL_GUEST,"http://trucksoft.net/guestsignin/");
//					}
					//login_validation(str_code);
					userLogin(str_code);
				} else {
					edtcode.setError("Enter your company code");
				}
			}
		});

	}




	/**
	 * A placeholder fragment containing a simple view.
	 */
	
	TextWatcher tt=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			String str_code=edtcode.getText().toString().trim();
			
			if(str_code !=null && str_code.length()>0)
			{
				edtcode.setError(null);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};

	private void userLogin(String ccode) {
		//dialog = new ProgressDialog(LoginActivity.this,
		//       AlertDialog.THEME_HOLO_LIGHT);
		dialog= new ProgressDialog(Authentication.this);
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


			RequestBody ccbody = RequestBody.create(MediaType.parse("text/plain"), ccode);

			Call<ResponseBody> call = api.myLogin(ccbody,ccbody);
			call.enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					try {
						mStatusCode = response.code();
					//	Log.e("mStatusCode", "mStatusCode==" + mStatusCode);
						if (mStatusCode == Constant.SUCEESSRESPONSECODE) {
							dialog.dismiss();
							JSONObject result = new JSONObject(response.body().string());

							//Log.e("RESULT", "" + result);
							String status = result.getString("status");
							// {"status":1,"id":"1","username":"apple","useremail":"krishnadhas.m@gmail.com"}
							if (status.contentEquals("1")) {

								String logo = result
										.getString("logo");

								String cname = result
										.getString("company_name");

								pref.putString("company_logo",
										logo);
								pref.putString("cd",
										stcode);
								pref.putString("cc",
										cname);

								pref.putString("logged", "logged_in");
Log.e("vccc","@"+result
		.getString("baseurl"));
								if(result.has("baseurl"))
								{
									String burl=result
											.getString("baseurl");
									pref.putString(Constant.BASE_URL_GUEST,""+burl);
								}


//								Intent mIntent = new Intent(
//										Authentication.this,
//										Registration.class);
//
//								startActivity(mIntent);

								Intent mIntent = new Intent(
										Authentication.this,
										Dashboard.class);

								startActivity(mIntent);


								finish();

							} else {
								if(result.has("company_name")) {
									String message = result.getString("company_name");

									Toast.makeText(context,""+message,Toast.LENGTH_SHORT).show();
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
						Log.v("SocketTimeOut", "SocketTimeOutError");
						Toast.makeText(Authentication.this, "Oops! Timeout Error!"
								, Toast.LENGTH_SHORT).show();
					}
					dialog.dismiss();
				}
			});
		}

	}
	
//	private void login_validation(String ccode) {
//		dialog = new ProgressDialog(Authentication.this,
//				AlertDialog.THEME_HOLO_LIGHT);
//
//		if (OnlineCheck.isOnline(this)) {
//			dialog.setMessage("Please wait...");
//			dialog.setCancelable(false);
//			dialog.show();
//			WebServices.chkcompanycode(this, ccode,
//					new JsonHttpResponseHandler() {
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//                                              String responseString, Throwable throwable) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers,
//									responseString, throwable);
//
//							dialog.dismiss();
//							// CommonMethod.showMsg(getActivity(), ""+
//							// responseString);
//						}
//
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//                                              Throwable throwable, JSONArray errorResponse) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers, throwable,
//									errorResponse);
//						//	Log.e("sds1", ""+errorResponse);
//							dialog.dismiss();
//							// CommonMethod.showMsg(getActivity(), ""+
//							// errorResponse);
//						}
//
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//                                              Throwable throwable, JSONObject errorResponse) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers, throwable,
//									errorResponse);
//							//Log.e("sds2", ""+errorResponse);
//							dialog.dismiss();
//
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								JSONArray response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							dialog.dismiss();
//							if (response != null) {
//
//							}
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								JSONObject response) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, response);
//							dialog.dismiss();
//
//							Log.e("response", ""+response.toString());
//							try {
//								if (response != null) {
//
//
//
//
//
//
//									String logo = response
//											.getString("logo");
//
//										String cname = response
//												.getString("company_name");
//
//										pref.putString("company_logo",
//												logo);
//										pref.putString("cd",
//												stcode);
//										pref.putString("cc",
//												cname);
//
//										pref.putString("logged", "logged_in");
//
//
//										Intent mIntent = new Intent(
//												Authentication.this,
//												Registration.class);
//
//										startActivity(mIntent);
//
//
//										finish();
//
//
//									// CommonMethod.showMsg(getActivity(), ""+
//									// message);
//
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//						}
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								String responseString) {
//							// TODO Auto-generated method stub
//							super.onSuccess(statusCode, headers, responseString);
//							dialog.dismiss();
//							// CommonMethod.showMsg(getActivity(), ""+
//							// responseString);
//						}
//
//					});
//		}
//
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public void checkAvailableConnection() {
//        ConnectivityManager connMgr = (ConnectivityManager) this
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        final android.net.NetworkInfo wifi = connMgr
//                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        final android.net.NetworkInfo mobile = connMgr
//                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        if (wifi.isAvailable()) {
//
//            WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
//            int ipAddress = myWifiInfo.getIpAddress();
//            Log.e(" ip ", ""+ipAddress);
//            System.out.println("WiFi address is "
//                    + android.text.format.Formatter.formatIpAddress(ipAddress));
//
//        } else if (mobile.isAvailable()) {
//
//            GetLocalIpAddress();
//            Log.e("mobile ip ", ""+GetLocalIpAddress());
//            Toast.makeText(this, "3G Available", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "No Network Available", Toast.LENGTH_LONG)
//                    .show();
//        }
//    }

//    private String GetLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf
//                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            return "ERROR Obtaining IP";
//        }
//        return "No IP Available";
//    }

	
	
	
//	 public String getLocalIpAddress()
//	  {
//	          try {
//	              for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//	                  NetworkInterface intf = en.nextElement();
//	                  for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//	                      InetAddress inetAddress = enumIpAddr.nextElement();
//	                      if (!inetAddress.isLoopbackAddress()) {
//	                          return inetAddress.getHostAddress().toString();
//	                      }
//	                  }
//	              }
//	          } catch (Exception ex) {
//	              Log.e("IP Address", ex.toString());
//	          }
//	          return null;
//	      }	
//	
//	






	private void insertDummyContactWrapper() {
		List<String> permissionsNeeded = new ArrayList<>();

		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			permissionsNeeded.add("Write Contacts");
//		if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//			permissionsNeeded.add("Read phone state");
		if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
			permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		if (!addPermission(permissionsList, Manifest.permission.CAMERA))
			permissionsNeeded.add("Read Contacts");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
			permissionsNeeded.add("GPS");
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
		boolean bool=false;
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				permissionsList.add(permission);
				// Check for Rationale Option
				if (!shouldShowRequestPermissionRationale(permission))
					bool= false;
			}
			bool=true;
		}
		return bool;
	}


//	private void callrating()
//	{
//		Log.e("callingz","review");
//		Task<ReviewInfo> request = reviewManager.requestReviewFlow();request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
//		@Override
//		public void onComplete(@NonNull Task<ReviewInfo> task) {
//			if(task.isSuccessful()){
//				ReviewInfo reviewInfo = task.getResult();
//				Task<Void> flow = reviewManager.launchReviewFlow(Authentication.this, reviewInfo);
//				flow.addOnCompleteListener(new OnCompleteListener<Void>() {
//					@Override
//					public void onComplete(@NonNull Task<Void> task) {// The flow has finished. The API does not indicate whether the user
//						//reviewed or not, or even whether the review dialog was shown. Thus, no matter the result, we continue our app flow //                }
//					}
//				});
//			}
//		}
//	});
//	}

}
