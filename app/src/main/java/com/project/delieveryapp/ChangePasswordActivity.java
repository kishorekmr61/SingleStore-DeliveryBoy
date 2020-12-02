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
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edttxt_password, edttxt_repassword;
    Button btn_submit;
    AwesomeValidation awesomeValidation;
    String mobileno;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        edttxt_password = findViewById(R.id.edttxt_password);
        edttxt_repassword = findViewById(R.id.edttxt_repassword);
        btn_submit = findViewById(R.id.btn_submit);
        sp = getSharedPreferences("config_info", 0);
        editor = sp.edit();
        mobileno = getIntent().getStringExtra("mobileno");
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeValidation.addValidation(ChangePasswordActivity.this, R.id.edttxt_password, RegexTemplate.NOT_EMPTY
                        , R.string.Passworderror);

                awesomeValidation.addValidation(ChangePasswordActivity.this, R.id.edttxt_repassword, RegexTemplate.NOT_EMPTY
                        , R.string.repassord);

                if (awesomeValidation.validate()) {
                    if (edttxt_password.getText().toString().equals(edttxt_repassword.getText().toString())) {
                        progressDialog.show();
                        InvokeService(mobileno, edttxt_password.getText().toString());
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    public void InvokeService(String mobileno, String password) {
        Retro retrofits = new Retro();
        Retrofit retrofit = retrofits.call1();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.ChangeStaffPassword(mobileno, password);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    String strresponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(strresponse);
                    String status = jsonObject.getString("status");
                    if (status.equals("100")) {
                        editor.putString("vendorid", "");
                        editor.putString("branchid", "");
                        editor.commit();
                        Toast.makeText(ChangePasswordActivity.this, "Password  changed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ChangePasswordActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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
