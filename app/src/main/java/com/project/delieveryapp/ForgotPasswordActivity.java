package com.project.delieveryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edttxt_mobileno;
    Button btn_submit;
    AwesomeValidation validation;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        edttxt_mobileno = findViewById(R.id.edttxt_mobileno);
        btn_submit = findViewById(R.id.btn_submit);
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (edttxt_mobileno.getText().toString().length() == 10) {
            validation.addValidation(ForgotPasswordActivity.this, R.id.edttxt_mobileno, RegexTemplate.NOT_EMPTY
                    , R.string.mobileerror);
            if (validation.validate()) {
                progressDialog.show();
                InvokeService(edttxt_mobileno.getText().toString());

            }
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
        }

    }

    public void InvokeService(final String mobileno) {
        Retro retrofits = new Retro();
        Retrofit retrofit = retrofits.call1();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.VerifyStaff(mobileno);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                try {
                    String strresponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(strresponse);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("100")) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("mobileno", mobileno);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(ForgotPasswordActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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
