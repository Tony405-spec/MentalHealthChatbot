package com.wellness.aichatbot.payment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wellness.aichatbot.R;
import com.wellness.aichatbot.theme.ThemeManager;

public class DonateActivity extends AppCompatActivity {
    private final MpesaDonateService donateService = new MpesaDonateService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        ThemeManager.apply(this, findViewById(R.id.donateRoot));

        EditText phoneInput = findViewById(R.id.phoneInput);
        EditText amountInput = findViewById(R.id.amountInput);
        TextView statusText = findViewById(R.id.paymentStatusText);
        View payButton = findViewById(R.id.payButton);
        ((TextView) payButton).setTextColor(Color.WHITE);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        payButton.setOnClickListener(v -> {
            String phone = phoneInput.getText().toString().trim();
            String amount = amountInput.getText().toString().trim();

            if (TextUtils.isEmpty(phone) || !phone.startsWith("254") || phone.length() < 12) {
                statusText.setText(R.string.err_invalid_phone);
                statusText.setTextColor(Color.RED);
                return;
            }

            double parsedAmount;
            try {
                parsedAmount = Double.parseDouble(amount);
            } catch (NumberFormatException e) {
                parsedAmount = 0;
            }

            if (TextUtils.isEmpty(amount) || parsedAmount <= 0) {
                statusText.setText(R.string.err_invalid_amount);
                statusText.setTextColor(Color.RED);
                return;
            }

            statusText.setTextColor(ThemeManager.palette(this).text);
            statusText.setText(R.string.connecting_mpesa);
            payButton.setEnabled(false);

            donateService.initiateDonation(phone, amount, new MpesaDonateService.DonationCallback() {
                @Override
                public void onSuccess(String message) {
                    statusText.setText(message);
                    statusText.setTextColor(Color.parseColor("#4CAF50")); // Green
                    payButton.setEnabled(true);
                }

                @Override
                public void onError(String error) {
                    statusText.setText(getString(R.string.payment_error, error));
                    statusText.setTextColor(Color.RED);
                    payButton.setEnabled(true);
                }
            });
        });
    }
}
