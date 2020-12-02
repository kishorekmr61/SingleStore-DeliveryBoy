package com.project.delieveryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    AwesomeValidation awesomeValidation;
    EditText edttxt_username, edttxt_password;
    Button btn_login;
    Retro retrofits;
    SharedPreferences sp;
    ImageView imgview_logo;
    TextView txtview_forgotpassword;
    SharedPreferences.Editor editor;
    String vendorid = "", branchid = "", staffid = "", deviceid;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config_info", 0);
        branchid = sp.getString("branchid", "");
        staffid = sp.getString("staffid", "");
        if (!staffid.equalsIgnoreCase("") && !branchid.equalsIgnoreCase("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        edttxt_username = findViewById(R.id.edttxt_username);
        edttxt_password = findViewById(R.id.edttxt_password);
        txtview_forgotpassword = findViewById(R.id.txtview_forgotpassword);
        imgview_logo = findViewById(R.id.imgview_logo);
        retrofits = new Retro();
        editor = sp.edit();
        vendorid = sp.getString("vendorid", "");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        Glide.with(LoginActivity.this).load(R.drawable.molslogo).into(imgview_logo);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeValidation.addValidation(LoginActivity.this, R.id.edttxt_username, RegexTemplate.NOT_EMPTY
                        , R.string.mobileerror);
                awesomeValidation.addValidation(LoginActivity.this, R.id.edttxt_password, RegexTemplate.NOT_EMPTY
                        , R.string.Passworderror);

                if (awesomeValidation.validate()) {
                    if (retrofits.isNetworkConnected(getApplicationContext())) {
                        dialog.show();
                        InvokeService(edttxt_username.getText().toString(), edttxt_password.getText().toString());
                    } else {
                        Toast.makeText(LoginActivity.this, "You are not connected to network", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        txtview_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

    public void InvokeService(final String mobileno, String psd) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.LoginUser(mobileno, psd);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String strresponse = response.body().string();
                        if (!strresponse.isEmpty()) {
                            JSONObject jsonObject = new JSONObject(strresponse);
                            String name = jsonObject.getString("StaffName");
                            String vendorid = jsonObject.getString("VendorID");
                            String branchid = jsonObject.getString("BranchID");
                            String staffid = jsonObject.getString("StaffID");
                            editor.putString("vendorid", vendorid);
                            editor.putString("branchid", branchid);
                            editor.putString("staffid", staffid);
                            editor.putString("mobileno", mobileno);
                            editor.commit();
                            InvokeService(staffid);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect mobile number and password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid mobileno and password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void InvokeService(String staffid) {
        deviceid = sp.getString("TOKEN", "");
        Retro retrofits = new Retro();
        Retrofit retrofit = retrofits.call1();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateStaffDeviceId(staffid, deviceid, "A", "1");
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String strresponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(strresponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("100")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if (view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if (viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if (rect.contains(x, y)) {
                        return consumed;
                    }
                } else if (viewNew instanceof EditText) {//|| viewNew instanceof CustomEditText
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
