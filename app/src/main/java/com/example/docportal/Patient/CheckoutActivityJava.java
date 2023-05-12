package com.example.docportal.Patient;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CheckoutActivityJava extends AppCompatActivity {
    private static final String TAG = "CheckoutActivity";
    private static final String BACKEND_URL = "http://10.0.2.2:4242";
    RecyclerView _cartList;
    AddToCartAdapter addToCartAdapter;
    Integer count;
    TextView TotalPrice;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    double totalPrice, _totalPrice;
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Cart");
    private String paymentIntentClientSecret;
    private PaymentSheet paymentSheet;

    private Button payButton, easypaisa, cashOnDelivery;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_checkout);
        TotalPrice = findViewById(R.id.tprice);


        easypaisa = findViewById(R.id.easy_button);
        cashOnDelivery = findViewById(R.id.delivery_button);
        cashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivityJava.this);
                builder.setTitle("User Billing Address");

                // Set the custom layout for the dialog
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_user_info, null);
                builder.setView(dialogView);

                // Find the dialog's views
                EditText editTextFullName = dialogView.findViewById(R.id.editText_full_name);
                EditText editTextCity = dialogView.findViewById(R.id.editText_city);
                EditText editTextAddress = dialogView.findViewById(R.id.editText_address_line);
                EditText editTextZipCode = dialogView.findViewById(R.id.editText_zip_code);
                EditText editTextState = dialogView.findViewById(R.id.editText_state);
                EditText editTextPhoneNumber = dialogView.findViewById(R.id.editText_phone_number);


                // Set any additional properties or validations for the fields if needed

                // Add the Submit button
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // Handle the Submit button click event
                        String fullName = editTextFullName.getText().toString().trim();
                        String city = editTextCity.getText().toString().trim();
                        String address = editTextAddress.getText().toString().trim();
                        String zipCode = editTextZipCode.getText().toString().trim();
                        String state = editTextState.getText().toString().trim();
                        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                        if (fullName.isEmpty() || city.isEmpty() || address.isEmpty() || zipCode.isEmpty() || state.isEmpty() || phoneNumber.isEmpty()) {
                            Toast.makeText(CheckoutActivityJava.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        } else {

                            // Create a Firestore document with the entered data
                            Map<String, Object> data = new HashMap<>();
                            data.put("id", firestoreHandler.getCurrentUser());
                            data.put("fullName", fullName);
                            data.put("city", city);
                            data.put("address", address);
                            data.put("zipCode", zipCode);
                            data.put("state", state);
                            data.put("phoneNumber", phoneNumber);

                            // Get a reference to the Firestore collection "Billing Address"
                            CollectionReference billingAddressCollection = firestoreHandler.getFirestoreInstance().collection("Billing Address");

                            // Insert the data into Firestore
                            billingAddressCollection.add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            deleteCart();
                                            Transaction();
                                            // Data inserted successfully
                                            Toast.makeText(CheckoutActivityJava.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error occurred while inserting data
                                            Toast.makeText(CheckoutActivityJava.this, "Failed to insert data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // Close the dialog
                            dialog.dismiss();
                        }
                    }

                });

                // Add a Cancel button if needed
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        easypaisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the phone number from your data source or any other way
                String phoneNumber = "1234567890"; // Replace with your actual phone number

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivityJava.this);
                builder.setTitle("Phone Number");
                builder.setMessage("The phone number is: " + phoneNumber);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        payButton.setEnabled(false);
                        cashOnDelivery.setEnabled(false);
                        Transaction();
                        deleteCart();

                        //startActivity(new Intent(CheckoutActivityJava.this, TransactionHistory.class));
                    }
                }); // You can add a listener to perform an action on button click if needed

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


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


    }

    private void setUpRecyclerView() {
        String aquery = firestoreHandler.getCurrentUser();
        Query query = noteBookref.orderBy("id", Query.Direction.DESCENDING).startAt(aquery).endAt(aquery);
        System.out.println(query);
        FirestoreRecyclerOptions<Medicine> options = new FirestoreRecyclerOptions.Builder<Medicine>()
                .setQuery(query, Medicine.class).build();


        addToCartAdapter = new AddToCartAdapter(options);
        count = addToCartAdapter.getItemCount();
        _cartList = findViewById(R.id.addToCartRecycler);
        _cartList.setLayoutManager(new WrapContentLinearLayoutManager(CheckoutActivityJava.this, LinearLayoutManager.VERTICAL, false));
//
//        Query filteredQuery = noteBookref.orderBy("id", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
//        FirestoreRecyclerOptions<Medicine> optionss = new FirestoreRecyclerOptions.Builder<Medicine>()
//                .setQuery(filteredQuery, Medicine.class).build();
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                TotalPrice.setText(String.valueOf(totalPrice));
                _totalPrice = totalPrice;
                fetchPaymentIntent();

            }

        });
        addToCartAdapter.updateOptions(options);
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
            if (!CheckoutActivityJava.this.isFinishing()) {
                dialog.show();
            }


        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    private void fetchPaymentIntent() {
        // final String shoppingCartContent = "{\"items\": [ {\"id\":\"xl-tshirt\"}]}";
        double amount = _totalPrice * 100;
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        itemMap.put("currency", "usd");
        itemMap.put("id", "xl-tshirt");
        itemMap.put("amount", amount);
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
                        showAlert("Failed to load data", "Error: " + e);
                    }

                    @Override
                    public void onResponse(
                            @NonNull Call call,
                            @NonNull Response response
                    ) throws IOException {
                        if (!response.isSuccessful()) {
                            showAlert(
                                    "Failed to load page",
                                    "Error: " + response
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


    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            cashOnDelivery.setEnabled(false);
            easypaisa.setEnabled(false);
            Transaction();

            deleteCart();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.i(TAG, "Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            showAlert("Payment failed", error.getLocalizedMessage());
        }
    }


    private void deleteCart() {
        CollectionReference collectionRef = firestoreHandler.getFirestoreInstance().collection("Cart");

        Query query = collectionRef.whereEqualTo("id", firestoreHandler.getCurrentUser());

// Step 4: Fetch the document(s) that match the query criteria
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Documents that match the query criteria exist
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            // Access the document data using documentSnapshot.getData()
                            System.out.println("Document data: " + documentSnapshot.getData());

                            // Step 5: Delete the document
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> deleteTask) {
                                    if (deleteTask.isSuccessful()) {
                                        System.out.println("Document deleted successfully.");
                                    } else {
                                        System.out.println("Failed to delete document: " + deleteTask.getException());
                                    }
                                }
                            });
                        }
                    } else {
                        // No documents match the query criteria
                        System.out.println("No documents found.");
                    }
                } else {
                    // Failed to fetch documents
                    System.out.println("Failed to fetch documents: " + task.getException());
                }
            }
        });
    }

    private void Transaction() {
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(currentTime);
        CollectionReference transactionRef = firestoreHandler.getFirestoreInstance().collection("Transaction");
        showToast("Payment complete!");
        for (int i = 0; i < addToCartAdapter.getItemCount(); i++) {

            // Get the data for the current item
            Medicine model = addToCartAdapter.getItem(i);
            Map<String, Object> data = new HashMap<>();
            data.put("id", firestoreHandler.getCurrentUser());
            data.put("item", model.getTitle());
            data.put("seller", model.getSeller());
            data.put("time", formattedTime);
            data.put("price", model.getPrice());

            // Create a new document in the Firestore collection
            transactionRef.add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID: " + documentReference.getId());


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }


}