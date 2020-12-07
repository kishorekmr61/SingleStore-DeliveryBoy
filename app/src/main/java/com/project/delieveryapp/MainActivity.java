package com.project.delieveryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.project.delieveryapp.Fragments.DeliveredOrderFragment;
import com.project.delieveryapp.Fragments.ProfileFragment;
import com.project.delieveryapp.Notification.NotificationActivity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    ArrayList<CustomerDetail> customerdetail;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    FrameLayout frameLayout;
    LinearLayout lnrlayout;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Retro retrofits;
    String vendorid = "", branchid = "", staffid = "", mobileno;
    ProgressDialog dialog;
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        drawerLayout = findViewById(R.id.drawerlyt_main);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        frameLayout = findViewById(R.id.framelayout);
        lnrlayout = findViewById(R.id.lnrlayout_main);
        setSupportActionBar(toolbar);
        retrofits = new Retro();
        sp = getSharedPreferences("config_info", 0);
        editor = sp.edit();
        vendorid = sp.getString("vendorid", "");
        branchid = sp.getString("branchid", "");
        staffid = sp.getString("staffid", "");
        mobileno = sp.getString("mobileno", "");
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.my_orders, R.string.my_orders);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setTitle("My Orders");
        customerdetail = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.header, null, false);
        final ImageView imgview_header = view.findViewById(R.id.imgview_header);
        Glide.with(getApplicationContext())
                .load(R.drawable.molslogo)
                .placeholder(R.mipmap.ic_launcher)
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imgview_header.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        navigationView.addHeaderView(view);
       /* LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        CustomerAdapter customerAdapter=new CustomerAdapter(getApplicationContext(),customerdetail);
        recyclerView.setAdapter(customerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);*/
        navigationView.setNavigationItemSelectedListener(this);
      /*  Intent intent=new Intent(MainActivity.this,TrackerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);*/
        Tracker();
        if (retrofits.isNetworkConnected(getApplicationContext())) {
            dialog.show();
            InvokeService("0", "GETDELIVERYORDERS");
        } else {
            Toast.makeText(MainActivity.this, "You are not connected to network", Toast.LENGTH_SHORT).show();
        }


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
                    if (response.body().size() > 0) {
                        customerdetail.addAll(response.body());
                    }

//                  String strresponse=response.body().string();
//                    if(!strresponse.isEmpty() && !strresponse.equalsIgnoreCase("[]")) {
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
//                            String Address = jsonObject.getString("AddressLine1")+jsonObject.getString("AddressLine2");
//                            String FirebaseId=jsonObject.getString("FirebaseId");
//                            String DeliveryBoyEmail=jsonObject.getString("DeliveryBoyEmail");
//                            String DeliveryBoyMobile=jsonObject.getString("DeliveryBoyMobile");
//                            customerdetail.add(new CustomerDetail(CustomerID, CustomerName, Latitude, Longitude, MobileNo, Distance, Address, orderid, paymenttype, AssignID,DeliveryBoyEmail,FirebaseId,DeliveryBoyMobile));
//                        }
                    startTrackerService();
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    CustomerAdapter customerAdapter = new CustomerAdapter(getApplicationContext(), customerdetail, 1);
                    recyclerView.setAdapter(customerAdapter);
                    recyclerView.setLayoutManager(linearLayoutManager);

                   /*  Intent intent=new Intent(MainActivity.this,TrackerActivity.class);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                     startActivity(intent);*/


//                }
//                    else{
//                    Toast.makeText(MainActivity.this, "No Records Found", Toast.LENGTH_SHORT).show();
//                }


                } catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CustomerDetail>> call, Throwable t) {

                if (t instanceof SocketTimeoutException) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Request Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_orders:
                drawerLayout.closeDrawers();
                frameLayout.setVisibility(View.GONE);
                lnrlayout.setVisibility(View.VISIBLE);
                Intent intent1 = getIntent();
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
                // finish();
                break;


         /*   case R.id.item_rewards:   drawerLayout.closeDrawers();
                frameLayout.setVisibility(View.VISIBLE);
                lnrlayout.setVisibility(View.GONE);
                lnrlayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new RewardsFragment()).addToBackStack(null).commit();
                break;*/


            case R.id.item_delivered:
                drawerLayout.closeDrawers();
                frameLayout.setVisibility(View.VISIBLE);
                lnrlayout.setVisibility(View.GONE);
                lnrlayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new DeliveredOrderFragment()).addToBackStack(null).commit();
                break;

            case R.id.item_changepassword:
                drawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mobileno", mobileno);
                startActivity(intent);
                break;

            case R.id.item_profile:
                drawerLayout.closeDrawers();
                frameLayout.setVisibility(View.VISIBLE);
                lnrlayout.setVisibility(View.GONE);
                lnrlayout.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).addToBackStack(null).commit();
                break;


            case R.id.item_logout:
                drawerLayout.closeDrawers();
                editor.putString("vendorid", "");
                editor.putString("branchid", "");
                editor.commit();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                finish();

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog();
    }

    public void AlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure you want to close the App");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void Tracker() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //turnGPSOn();
            //  Toast.makeText(MainActivity.this,"Enable gps icon",Toast.LENGTH_SHORT).show();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void startTrackerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (customerdetail.size() > 0) {
                String email = customerdetail.get(0).getEmail();
                Intent intent = new Intent(MainActivity.this, TrackerService.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("email", customerdetail.get(0).getEmail());
                intent.putExtra("id", customerdetail.get(0).getEmail());
                intent.putExtra("mobileno", customerdetail.get(0).getMobileNo());
                startForegroundService(intent);
            }
        } else {

            Intent intent = new Intent(MainActivity.this, TrackerService.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (customerdetail.size() > 0) {
                intent.putExtra("email", customerdetail.get(0).getEmail());
                intent.putExtra("id", customerdetail.get(0).getFirebaseID());
                intent.putExtra("mobileno", customerdetail.get(0).getMobileNo());
                startService(intent);
            }
        }

        //   finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            //  startTrackerService();
        } else {
            // finish();
        }
    }

    private void turnGPSOn() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_notification:
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }

        return true;
    }
}
