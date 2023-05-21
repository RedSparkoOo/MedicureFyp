package com.example.docportal.Patient;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

public class PatientOnlineConsultation extends AppCompatActivity {
    private static final String TAG = "CheckoutActivity";
    private static final String BACKEND_URL = "http://192.168.186.51:4242";
    EditText rec_code;
    Button rec_start;
    URL rec_server_url;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    private String paymentIntentClientSecret;
    private PaymentSheet paymentSheet;

    private Button payButton;

    // Change Class name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_online_consultation);

        rec_code = findViewById(R.id.rec_code);
        rec_start = findViewById(R.id.rec_start);
        rec_start.setEnabled(false);

        fetchPaymentIntent();

        try {
            rec_server_url = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions OpConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(rec_server_url)

                    .build();
            JitsiMeet.setDefaultConferenceOptions(OpConferenceOptions);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51N3Xz8Dai2cvOdS6q9gxkqKNhmtPxgSS5ilaZfAkXbP4JSTTkIQYsLKruu9a1ddQxWv2KnR1BpjDlhSZ8jeu3wBL00BwhaOVni"
        );

        // Hook up the pay button
        payButton = findViewById(R.id.pay_button1);
        payButton.setOnClickListener(this::onPayClicked);
        payButton.setEnabled(false);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        // Hook up the address button
        rec_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rec_code.length() < 16) {
                    Toast.makeText(PatientOnlineConsultation.this, "Incorrect code", Toast.LENGTH_SHORT).show();
                } else {

                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(rec_code.getText().toString())

                            .build();

                    JitsiMeetActivity.launch(PatientOnlineConsultation.this, options);
                }


            }
        });


    }


    private void showAlert(String title, @Nullable String message) {
        runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ok", null)
                    .create();
            if (!PatientOnlineConsultation.this.isFinishing()) {
                dialog.show();
            }


        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    private void fetchPaymentIntent() {
        // final String shoppingCartContent = "{\"items\": [ {\"id\":\"xl-tshirt\"}]}";
        double amount = 20 * 100;
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
                            payButton.setBackgroundResource(R.drawable.but);
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
            Transaction();

            rec_start.setEnabled(true);
            rec_start.setBackgroundResource(R.drawable.but);

        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.i(TAG, "Payment canceled!");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Throwable error = ((PaymentSheetResult.Failed) paymentSheetResult).getError();
            showAlert("Payment failed", error.getLocalizedMessage());
        }
    }


    private void Transaction() {
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(currentTime);
        CollectionReference transactionRef = firestoreHandler.getFirestoreInstance().collection("Transaction");
        showToast("Payment complete!");


        // Get the data for the current item

        Map<String, Object> data = new HashMap<>();
        data.put("id", firestoreHandler.getCurrentUser());
        data.put("item", "Online Consultation");
        data.put("seller", "N/A");
        data.put("time", formattedTime);
        data.put("price", "20.00$");

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