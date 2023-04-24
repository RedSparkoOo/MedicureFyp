package com.example.docportal.Patient;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.paymentsheet.addresselement.AddressDetails;
import com.stripe.android.paymentsheet.addresselement.AddressLauncher;
import com.stripe.android.paymentsheet.addresselement.AddressLauncherResult;


import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CheckoutActivityJava extends AppCompatActivity {
    RecyclerView _cartList;
    AddToCartAdapter addToCartAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Integer count;

    TextView TotalPrice;

    FirebaseAuth firebaseAuth;
    Object currentUserId;
    double totalPrice, _totalPrice;
    CollectionReference noteBookref = firestore.collection("Cart");

    private static final String TAG = "CheckoutActivity";
    private static final String BACKEND_URL = "http://10.0.2.2:4242";

    private String paymentIntentClientSecret;
    private PaymentSheet paymentSheet;

    private Button payButton;

    private AddressLauncher addressLauncher;

    private AddressDetails shippingDetails;

    private Button addressButton;

    private final AddressLauncher.Configuration configuration =
            new AddressLauncher.Configuration.Builder()
                    .additionalFields(
                            new AddressLauncher.AdditionalFieldsConfiguration(
                                    AddressLauncher.AdditionalFieldsConfiguration.FieldConfiguration.REQUIRED
                            )
                    )
                    .allowedCountries(new HashSet<>(Arrays.asList("US", "CA", "GB")))
                    .title("Shipping Address")
                    .googlePlacesApiKey("(optional) YOUR KEY HERE")
                    .build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_checkout);
        TotalPrice = findViewById(R.id.tprice);

        firebaseAuth= FirebaseAuth.getInstance();

        Object currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!= null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
           setUpRecyclerView();


        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51MxSIWIwnwbkCpSTcbeqEZPR6i6rbjMHc5RdodkjLFI7mzc7hoO2P1OZtcciSFx9EdCBx2dVZARCpc5MI9BTf1vD0042mbYWDW"
        );

        // Hook up the pay button
        payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(this::onPayClicked);
        payButton.setEnabled(false);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        // Hook up the address button
        addressButton = findViewById(R.id.address_button);
        addressButton.setOnClickListener(this::onAddressClicked);
        addressLauncher = new AddressLauncher(this, this::onAddressLauncherResult);


    }
    private void setUpRecyclerView(){
        Query query = noteBookref.orderBy("Title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query, Medicine.class).build();



        addToCartAdapter = new AddToCartAdapter(options);
        count = addToCartAdapter.getItemCount();
        _cartList = findViewById(R.id.addToCartRecycler);
        _cartList.setLayoutManager(new WrapContentLinearLayoutManager(CheckoutActivityJava.this,LinearLayoutManager.VERTICAL, false ));
        String aquery = currentUserId.toString();
        Query filteredQuery = noteBookref.orderBy("id", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
        FirestoreRecyclerOptions<Medicine> optionss = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(filteredQuery, Medicine.class).build();
        filteredQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(CheckoutActivityJava.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                totalPrice = 0;
                for (QueryDocumentSnapshot document : value) {
                    Medicine item = document.toObject(Medicine.class);
                    totalPrice += Double.parseDouble(item.getPrice());
                }
                TotalPrice.setText(String.valueOf(totalPrice) );
                _totalPrice = totalPrice;
                fetchPaymentIntent();

            }

        });
        addToCartAdapter.updateOptions(optionss);
        _cartList.setAdapter(addToCartAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                addToCartAdapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(_cartList);


    }
    @Override
    protected void onStart() {
        super.onStart();
        addToCartAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        addToCartAdapter.stopListening();
    }


    private void showAlert(String title, @Nullable String message) {
        runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create();
            if(!((Activity) CheckoutActivityJava.this).isFinishing())
            {
                dialog.show();
            }


        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    private void fetchPaymentIntent() {
       // final String shoppingCartContent = "{\"items\": [ {\"id\":\"xl-tshirt\"}]}";
        double amount =  _totalPrice*100;
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String,Object>> itemList = new ArrayList<>();
        itemMap.put("currency","usd");
        itemMap.put("id","xl-tshirt");
        itemMap.put("amount",amount);
        itemList.add(itemMap);
        payMap.put("items", itemList);
        String shoppingCartContent = new Gson().toJson(payMap);

        final RequestBody requestBody = RequestBody.create(
                shoppingCartContent,
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BACKEND_URL + "/create-payment-intent")
                .post(requestBody)
                .build();

        new OkHttpClient()
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        showAlert("Failed to load data", "Error: " + e.toString());
                    }

                    @Override
                    public void onResponse(
                            @NonNull Call call,
                            @NonNull Response response
                    ) throws IOException {
                        if (!response.isSuccessful()) {
                            showAlert(
                                    "Failed to load page",
                                    "Error: " + response.toString()
                            );
                        } else {
                            final JSONObject responseJson = parseResponse(response.body());
                            paymentIntentClientSecret = responseJson.optString("clientSecret");
                            runOnUiThread(() -> payButton.setEnabled(true));
                            Log.i(TAG, "Retrieved PaymentIntent");
                        }
                    }
                });
    }

    private JSONObject parseResponse(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new JSONObject(responseBody.string());
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Error parsing response", e);
            }
        }

        return new JSONObject();
    }

    private void onPayClicked(View view) {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("Example, Inc.");

        // Present Payment Sheet
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private void onAddressClicked(View view) {
        addressLauncher.present(
                "pk_test_51MxSIWIwnwbkCpSTcbeqEZPR6i6rbjMHc5RdodkjLFI7mzc7hoO2P1OZtcciSFx9EdCBx2dVZARCpc5MI9BTf1vD0042mbYWDW"
        );
    }

    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showToast("Payment complete!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.i(TAG, "Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            showAlert("Payment failed", error.getLocalizedMessage());
        }
    }

    private void onAddressLauncherResult(AddressLauncherResult result) {
        // TODO: Handle result and update your UI
        if (result instanceof AddressLauncherResult.Succeeded) {
            shippingDetails = ((AddressLauncherResult.Succeeded) result).getAddress();
        } else if (result instanceof AddressLauncherResult.Canceled) {
            // TODO: Handle cancel
        }
    }
}