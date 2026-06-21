package com.example.mindcare.repository;

import android.util.Base64;
import androidx.annotation.NonNull;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MpesaRepository {
    public static final String CUSTOMER_KEY = "viZkCP8SeeTbYwzgx7DK64lHAmHXSC57WAu67nh5satvqtHW";
    public static final String CUSTOMER_SECRET = "WIx8N63IYbMGH7uYfp8HI2FPhsFE4pCMelxafa7g0Z5Fwbl9KoqxZHkLd2rUonch";
    public static final String BUSINESS_SHORTCODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CALLBACK_URL = "https://mydomain.com/mpesa-express-simulate/";

    private static final String BASE_URL = "https://sandbox.safaricom.co.ke";
    private final OkHttpClient client = new OkHttpClient();

    public interface DonateCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public void donate(String phone, int amount, DonateCallback callback) {
        Request tokenRequest = new Request.Builder()
                .url(BASE_URL + "/oauth/v1/generate?grant_type=client_credentials")
                .header("Authorization", Credentials.basic(CUSTOMER_KEY, CUSTOMER_SECRET))
                .build();
        client.newCall(tokenRequest).enqueue(new Callback() {
            @Override public void onFailure(@NonNull Call call, @NonNull IOException e) { callback.onError(e.getMessage()); }
            @Override public void onResponse(@NonNull Call call, @NonNull Response response) {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody != null) {
                        JSONObject body = new JSONObject(responseBody.string());
                        stkPush(body.getString("access_token"), phone, amount, callback);
                    } else {
                        callback.onError("Empty response from server.");
                    }
                } catch (Exception e) {
                    callback.onError("Could not start M-Pesa payment.");
                }
            }
        });
    }

    private void stkPush(String token, String phone, int amount, DonateCallback callback) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
            String password = Base64.encodeToString((BUSINESS_SHORTCODE + PASSKEY + timestamp).getBytes(), Base64.NO_WRAP);
            JSONObject payload = new JSONObject();
            payload.put("BusinessShortCode", BUSINESS_SHORTCODE);
            payload.put("Password", password);
            payload.put("Timestamp", timestamp);
            payload.put("TransactionType", "CustomerPayBillOnline");
            payload.put("Amount", amount);
            payload.put("PartyA", phone);
            payload.put("PartyB", BUSINESS_SHORTCODE);
            payload.put("PhoneNumber", phone);
            payload.put("CallBackURL", CALLBACK_URL);
            payload.put("AccountReference", "MindCare");
            payload.put("TransactionDesc", "MindCare donation");
            Request request = new Request.Builder()
                    .url(BASE_URL + "/mpesa/stkpush/v1/processrequest")
                    .header("Authorization", "Bearer " + token)
                    .post(RequestBody.create(payload.toString(), MediaType.parse("application/json")))
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(@NonNull Call call, @NonNull IOException e) { callback.onError(e.getMessage()); }
                @Override public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            callback.onSuccess(responseBody.string());
                        } else {
                            callback.onSuccess("Payment initiated (empty response)");
                        }
                    } catch (Exception e) {
                        callback.onError("Failed to read response.");
                    }
                }
            });
        } catch (Exception e) {
            callback.onError("Payment setup failed.");
        }
    }
}
