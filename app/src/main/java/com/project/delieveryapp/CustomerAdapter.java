package com.project.delieveryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewholder> {
 ArrayList<CustomerDetail>detaillist=new ArrayList<>();
 Context mcontext;
    Retro retrofits;
    SharedPreferences sp;
    String branchid="",vendorid="",staffid="";
    int ID;
    String Message="";

    public CustomerAdapter(Context applicationContext, ArrayList<CustomerDetail> customerdetail,int id) {
        this.detaillist=customerdetail;
        this.mcontext=applicationContext;
        sp = mcontext.getSharedPreferences("config_info", 0);
        branchid=sp.getString("branchid","");
        vendorid=sp.getString("vendorid","");
        staffid= sp.getString("staffid","");
        retrofits=new Retro();
        ID=id;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.customer_row,viewGroup,false);
        MyViewholder viewholder=new MyViewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int i) {
         final CustomerDetail detail=detaillist.get(i);
          if(ID==0)
          {
              holder.deliver.setVisibility(View.GONE);
              holder.delivered.setVisibility(View.GONE);
          }
          else{
              holder.deliver.setVisibility(View.VISIBLE);
              holder.delivered.setVisibility(View.VISIBLE);
          }

           holder.name.setText(detail.getCustomerName());
           holder.address.setText(detail.getAddressLine1() + " , "+detail.getAddressLine2());
           holder.mobile.setText(detail.getMobileNo());
           holder.paymenttype.setText(detail.getPaymentType());
           holder.orderid.setText(detail.getOrderID());


           holder.deliver.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   /*Uri gmmIntentUri = Uri.parse("geo:17.4222120,78.3808145");
                   Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                   mapIntent.setPackage("com.google.android.apps.maps");
                      mcontext. startActivity(mapIntent);*/
                  //  String uri="http://maps.google.com/maps?&daddr="+detail.getLatitude()+","+detail.getLongitude();
                   try {
                       String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.valueOf(detail.getLatitude()), Float.valueOf(detail.getLongitude()), "Where the party is at");
                       // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                       Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                               Uri.parse(uri));
                       intent.setPackage("com.google.android.apps.maps");
                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       mcontext. startActivity(intent);
                       //   http://maps.google.com/maps?&daddr=17.4222120,78.3808145

                   }
                   catch (Exception e)
                   {
                       e.printStackTrace();
                   }

               }
           });
           holder.delivered.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                  // InvokeServiceOrder(detail.getOrderid(),"5",detail.getAssignID());
                   if (retrofits.isNetworkConnected(mcontext)) {
                       InvokeServiceOrder(detail.getOrderID(),"5",detail.getAssignID());
                   }
                   else {
                       Toast.makeText(mcontext, "You are not connected to network", Toast.LENGTH_SHORT).show();
                   }

               }
           });
           holder.mobile.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String number="tel:"+detail.getMobileNo();
                   Intent intent = new Intent(Intent.ACTION_DIAL);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                   intent.setData(Uri.parse(number));
                  mcontext. startActivity(intent);
               }
           });
    }

    @Override
    public int getItemCount() {
        return detaillist.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView name,address,mobile,paymenttype,orderid;
        Button deliver,delivered;
      CardView cardview_customerrow;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
           name=itemView.findViewById(R.id.txtview_name);
           address=itemView.findViewById(R.id.txtview_address);
           mobile=itemView.findViewById(R.id.txtview_mobile);
           deliver=itemView.findViewById(R.id.btn_ideliver);
           paymenttype=itemView.findViewById(R.id.txtview_paymenttype);
           orderid=itemView.findViewById(R.id.txtview_orderid);
           delivered=itemView.findViewById(R.id.btn_delivered);
        }
    }

    public void InvokeServiceOrder(String orderid, String statusid, final String assignid) {
        Retro retrofits = new Retro();
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.GrocOrderStatus(orderid,statusid,vendorid,branchid);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                    InvokeService(assignid,"UPDATEORDERSTATUS");

                   /* Intent intent = new Intent(mcontext,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(intent);*/


                } catch (IOException e) {
                    Toast.makeText(mcontext,mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (t instanceof SocketTimeoutException) {
                  //  dialog.dismiss();
                    Toast.makeText(mcontext, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                   // dialog.dismiss();
                    Toast.makeText(mcontext,mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void InvokeService(String assignid,String condition)
    {
        Retrofit retrofit = retrofits.call1();
        ApiService service = retrofit.create(ApiService.class);
        Call<ResponseBody> response = service.UpdateStaffDeviceId(branchid,staffid,assignid,condition);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String strresponse=response.body().string();
                  /*  JSONArray jsonArray=new JSONArray(strresponse);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Message=jsonObject.getString("MESSAGE");
                    }*/
                  JSONObject jsonObject = new JSONObject(strresponse);
                  String status = jsonObject.getString("status");
                  String message = jsonObject.getString("message");
                    if(message.equals("Success"))
                    {
                        Toast.makeText(mcontext,"Status is updated successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(mcontext,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mcontext.startActivity(intent);
                    }
                   else
                    {
                        Toast.makeText(mcontext,message,Toast.LENGTH_SHORT).show();
                    }


                }
                catch (IOException e) {
                    Toast.makeText(mcontext,mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (JSONException e) {
                    Toast.makeText(mcontext,mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    //  dialog.dismiss();
                    Toast.makeText(mcontext, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    // dialog.dismiss();
                    Toast.makeText(mcontext,mcontext.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
