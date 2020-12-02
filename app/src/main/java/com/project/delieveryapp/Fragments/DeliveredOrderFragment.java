package com.project.delieveryapp.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.delieveryapp.ApiService;
import com.project.delieveryapp.CustomerAdapter;
import com.project.delieveryapp.CustomerDetail;
import com.project.delieveryapp.R;
import com.project.delieveryapp.Retro;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeliveredOrderFragment extends Fragment {
    SharedPreferences sp;
    String branchid = "", staffid = "";
    ArrayList<CustomerDetail> customerdetail;
    RecyclerView recyclerView;
    Retro retrofits;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliveredorders, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        retrofits = new Retro();
        getActivity().setTitle("Delivered Orders");
        sp = getActivity().getSharedPreferences("config_info", 0);
        branchid = sp.getString("branchid", "");
        staffid = sp.getString("staffid", "");
        customerdetail = new ArrayList<>();
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        if (retrofits.isNetworkConnected(getContext())) {
            dialog.show();
            InvokeService("0", "GETUPDATEDDELIVERY");
        } else {
            Toast.makeText(getContext(), "You are not connected to network", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void InvokeService(String assignid, String condition) {
        Retrofit retrofit = retrofits.call();
        ApiService service = retrofit.create(ApiService.class);
        Call<List<CustomerDetail>> response = service.GetOrders(branchid, staffid, assignid, condition);
        response.enqueue(new Callback<List<CustomerDetail>>() {
            @Override
            public void onResponse(Call<List<CustomerDetail>> call, Response<List<CustomerDetail>> response) {
                try {
                    dialog.dismiss();
                    customerdetail.clear();
                    customerdetail.addAll(response.body());
//                    String strresponse=response.body().string();
//                    if(!strresponse.isEmpty()) {
//                        JSONArray jsonArray = new JSONArray(strresponse);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String CustomerID = jsonObject.getString("CustomerID");
//                            String CustomerName = jsonObject.getString("CustomerName");
//                            String Latitude = jsonObject.getString("Latitude");
//                            String Longitude = jsonObject.getString("Longitude");
//                            String MobileNo = jsonObject.getString("MobileNo");
//                            String Distance = jsonObject.getString("Distance");
//                            String AssignID = jsonObject.getString("AssignID");
//                            String orderid = jsonObject.getString("OrderID");
//                            String paymenttype = jsonObject.getString("PaymentType");
//                            String Address = jsonObject.getString("AddressLine1");
//                            String FirebaseId=jsonObject.getString("FirebaseId");
//                            String DeliveryBoyEmail=jsonObject.getString("DeliveryBoyEmail");
//                            String DeliveryBoyMobile=jsonObject.getString("DeliveryBoyMobile");
//                            customerdetail.add(new CustomerDetail(CustomerID, CustomerName, Latitude, Longitude, MobileNo, Distance, Address, orderid, paymenttype, AssignID,DeliveryBoyEmail,FirebaseId,DeliveryBoyMobile));
//                        }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    CustomerAdapter customerAdapter = new CustomerAdapter(getContext(), customerdetail, 0);
                    recyclerView.setAdapter(customerAdapter);
                    recyclerView.setLayoutManager(linearLayoutManager);

                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CustomerDetail>> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
