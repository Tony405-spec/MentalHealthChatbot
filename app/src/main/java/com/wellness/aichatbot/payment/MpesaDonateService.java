package com.wellness.aichatbot.payment;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MpesaDonateService {
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface DonationCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public void initiateDonation(String phone, String amount, DonationCallback callback) {
        getAccessToken(new DonationCallback() {
            @Override
            public void onSuccess(String token) {
                performStkPush(token, phone, amount, callback);
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() -> callback.onError("Auth failed: " + error));
            }
        });
    }

    private void getAccessToken(DonationCallback callback) {
        String keys = MpesaConfig.CUSTOMER_KEY + ":" + MpesaConfig.CUSTOMER_SECRET;
        String auth = "Basic " + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP);

        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .addHeader("Authorization", auth)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (Response closeableResponse = response) {
                    if (closeableResponse.isSuccessful() && closeableResponse.body() != null) {
                        JsonObject json = JsonParser.parseString(closeableResponse.body().string()).getAsJsonObject();
                        if (json.has("access_token")) {
                            callback.onSuccess(json.get("access_token").getAsString());
                        } else {
                            callback.onError("Access token missing from response.");
                        }
                    } else {
                        callback.onError("HTTP " + closeableResponse.code());
                    }
                }
            }
        });
    }

    private void performStkPush(String token, String phone, String amount, DonationCallback callback) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        String password = generatePassword(MpesaConfig.BUSINESS_SHORTCODE, MpesaConfig.PASSKEY, timestamp);

        JsonObject body = new JsonObject();
        body.addProperty("BusinessShortCode", MpesaConfig.BUSINESS_SHORTCODE);
        body.addProperty("Password", password);
        body.addProperty("Timestamp", timestamp);
        body.addProperty("TransactionType", "CustomerPayBillOnline");
        body.addProperty("Amount", amount);
        body.addProperty("PartyA", phone);
        body.addProperty("PartyB", MpesaConfig.BUSINESS_SHORTCODE);
        body.addProperty("PhoneNumber", phone);
        body.addProperty("CallBackURL", MpesaConfig.CALLBACK_URL);
        body.addProperty("AccountReference", "MindfulSupport");
        body.addProperty("TransactionDesc", "Donation to Mindful Support");

        RequestBody reqBody = RequestBody.create(body.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .addHeader("Authorization", "Bearer " + token)
                .post(reqBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (Response closeableResponse = response) {
                    String respStr = closeableResponse.body() != null ? closeableResponse.body().string() : "Empty response";
                    boolean successful = closeableResponse.isSuccessful();
                    mainHandler.post(() -> {
                        if (successful) {
                            callback.onSuccess("STK Push initiated successfully. Please check your phone to enter M-Pesa PIN.");
                        } else {
                            callback.onError("STK Push failed: " + respStr);
                        }
                    });
                }
            }
        });
    }

    private String generatePassword(String shortcode, String passkey, String timestamp) {
        String str = shortcode + passkey + timestamp;
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }
}
