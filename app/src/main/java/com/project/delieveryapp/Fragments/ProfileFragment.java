package com.project.delieveryapp.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.delieveryapp.ApiService;
import com.project.delieveryapp.R;
import com.project.delieveryapp.Retro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    TextView txtview_name,txtview_mobileno,txtview_email,txtview_address;
    ImageView imgview_user;
    SharedPreferences sp;
    String mobileno;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_profile,container,false);
      txtview_name=view.findViewById(R.id.txtview_name);
      txtview_mobileno=view.findViewById(R.id.txtview_mobileno);
      txtview_email=view.findViewById(R.id.txtview_email);
      txtview_address=view.findViewById(R.id.txtview_address);
      imgview_user=view.findViewById(R.id.imgview_user);
      sp=getActivity().getSharedPreferences("config_info", 0);
      mobileno=sp.getString("mobileno","");
      progressDialog=new ProgressDialog(getContext());
      progressDialog.setMessage("Please wait...");
      progressDialog.setCancelable(false);
      progressDialog.show();
      InvokeService(mobileno);
      return view;
    }


    public void InvokeService(String mobileno) {
        Retro retrofits = new Retro();
        Retrofit retrofit = retrofits.call1();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GetStaffByID(mobileno);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response.isSuccessful())
                {
                    try {
                        String strresponse=response.body().string();
                        JSONObject jsonObject=new JSONObject(strresponse);
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        if(message.equals("Success"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONObject("objresult").getJSONArray("table");
                                JSONObject object=jsonArray.getJSONObject(0);
                                String staffName=object.getString("staffName");
                                String mobileNo=object.getString("mobileNo");
                                String address=object.getString("address");
                                String email=object.getString("email");
                                String image=object.getString("avatar");
                        Glide.with(getContext()).load(image).placeholder(R.drawable.ic_usericoncolor).into(imgview_user);
                        txtview_name.setText(staffName);
                        txtview_mobileno.setText(mobileNo);
                        txtview_address.setText(address);
                        txtview_email.setText(email);

                        }

                        else
                        {
                            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                        }
                    }

                    catch (IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                    catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.getMessage();
            }
        });


    }


}
