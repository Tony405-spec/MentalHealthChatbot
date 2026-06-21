package com.wellness.aichatbot;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.wellness.aichatbot.theme.ThemeManager;
import com.wellness.aichatbot.theme.ThemePalette;

import java.util.ArrayList;
import java.util.List;

public class MoodAnalyticsActivity extends AppCompatActivity {
    private ThemePalette palette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_analytics);
        ThemeManager.apply(this, findViewById(R.id.analyticsRoot));
        palette = ThemeManager.palette(this);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        ((Button) findViewById(R.id.trackMoodButton)).setTextColor(Color.WHITE);
        findViewById(R.id.trackMoodButton).setOnClickListener(v -> Toast.makeText(this, "Mood check-in starts from the dashboard mood card.", Toast.LENGTH_LONG).show());

        renderCards();
        setupLineChart((LineChart) findViewById(R.id.lineChart));
        setupBarChart((BarChart) findViewById(R.id.barChart));
        setupPieChart((PieChart) findViewById(R.id.pieChart));
        renderInsights();
    }

    private void renderCards() {
        LinearLayout container = findViewById(R.id.analyticsCardsContainer);
        container.removeAllViews();
        addAnalyticsCard(container, "Weekly Trends", "Mood is trending positively across the last seven check-ins.");
        addAnalyticsCard(container, "Positive Mood", "71% of tracked moments were calm, grateful, or hopeful.");
        addAnalyticsCard(container, "Stress Frequency", "Stress appeared twice this week, down from three times last week.");
        addAnalyticsCard(container, "Consistency", "Your emotional consistency is steady with mild midweek variation.");
    }

    private void addAnalyticsCard(LinearLayout container, String title, String body) {
        com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(params);
        card.setRadius(dp(20));
        card.setCardElevation(dp(2));
        card.setCardBackgroundColor(palette.card);
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(dp(16), dp(14), dp(16), dp(14));
        TextView titleView = text(title, 15, true);
        TextView bodyView = text(body, 13, false);
        bodyView.setPadding(0, dp(6), 0, 0);
        inner.addView(titleView);
        inner.addView(bodyView);
        card.addView(inner);
        container.addView(card);
    }

    private void setupLineChart(LineChart chart) {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 52));
        entries.add(new Entry(2, 58));
        entries.add(new Entry(3, 61));
        entries.add(new Entry(4, 56));
        entries.add(new Entry(5, 70));
        entries.add(new Entry(6, 74));
        entries.add(new Entry(7, 78));
        LineDataSet set = new LineDataSet(entries, "Mood trend");
        set.setColor(Color.rgb(102, 119, 97));
        set.setCircleColor(Color.rgb(157, 107, 83));
        set.setLineWidth(3f);
        set.setCircleRadius(5f);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        chart.setData(new LineData(set));
        styleChart(chart);
        chart.animateX(900, Easing.EaseInOutQuad);
    }

    private void setupBarChart(BarChart chart) {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 5));
        entries.add(new BarEntry(2, 3));
        entries.add(new BarEntry(3, 2));
        entries.add(new BarEntry(4, 1));
        BarDataSet set = new BarDataSet(entries, "Weekly mood frequency");
        set.setColors(Color.rgb(102, 119, 97), Color.rgb(96, 125, 139), Color.rgb(157, 107, 83), Color.rgb(216, 210, 200));
        set.setDrawValues(false);
        chart.setData(new BarData(set));
        styleChart(chart);
        chart.animateY(900, Easing.EaseInOutQuad);
    }

    private void setupPieChart(PieChart chart) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(50, "Calm"));
        entries.add(new PieEntry(30, "Reflective"));
        entries.add(new PieEntry(20, "Anxious"));
        PieDataSet set = new PieDataSet(entries, "Mood distribution");
        set.setColors(Color.rgb(102, 119, 97), Color.rgb(96, 125, 139), Color.rgb(157, 107, 83));
        set.setSliceSpace(3f);
        PieData data = new PieData(set);
        data.setValueTextColor(palette.text);
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.setUsePercentValues(true);
        chart.setDrawEntryLabels(false);
        chart.setHoleRadius(62f);
        chart.setTransparentCircleRadius(66f);
        chart.getDescription().setEnabled(false);
        chart.setCenterText("Mood\nDistribution");
        chart.setCenterTextColor(palette.text);
        Legend legend = chart.getLegend();
        legend.setTextColor(palette.text);
        legend.setWordWrapEnabled(true);
        chart.animateY(900, Easing.EaseInOutQuad);
    }

    private void styleChart(com.github.mikephil.charting.charts.Chart<?> chart) {
        chart.getDescription().setEnabled(false);
        chart.setNoDataText("Start tracking your moods to unlock personalized wellness insights.");
        Legend legend = chart.getLegend();
        legend.setTextColor(palette.text);
        legend.setForm(Legend.LegendForm.CIRCLE);
        if (chart instanceof com.github.mikephil.charting.charts.BarLineChartBase) {
            com.github.mikephil.charting.charts.BarLineChartBase<?> base = (com.github.mikephil.charting.charts.BarLineChartBase<?>) chart;
            base.getAxisRight().setEnabled(false);
            base.getXAxis().setDrawGridLines(false);
            base.getAxisLeft().setDrawGridLines(false);
            base.getAxisLeft().setTextColor(palette.text);
            base.getXAxis().setTextColor(palette.text);
            base.setDrawGridBackground(false);
        }
    }

    private void renderInsights() {
        LinearLayout container = findViewById(R.id.insightsContainer);
        addAnalyticsCard(container, "Insight", "You reported feeling Calm on 5 of the last 7 days.");
        addAnalyticsCard(container, "Insight", "Stress levels decreased by 23% compared to last week.");
        addAnalyticsCard(container, "Insight", "Your mood is trending positively. Keep pairing reflection with rest.");
    }

    private TextView text(String value, int size, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(palette.text);
        if (bold) {
            view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return view;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
