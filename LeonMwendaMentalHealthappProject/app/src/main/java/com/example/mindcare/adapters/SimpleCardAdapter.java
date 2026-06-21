package com.example.mindcare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mindcare.R;
import java.util.ArrayList;
import java.util.List;

public class SimpleCardAdapter extends RecyclerView.Adapter<SimpleCardAdapter.Holder> {
    public interface ClickListener { void onClick(int position); void onLongClick(int position); }
    private final List<String> titles = new ArrayList<>();
    private final List<String> subtitles = new ArrayList<>();
    private ClickListener listener;

    public void setItems(List<String> newTitles, List<String> newSubtitles) {
        titles.clear();
        subtitles.clear();
        titles.addAll(newTitles);
        subtitles.addAll(newSubtitles);
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener listener) { this.listener = listener; }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.subtitle.setText(subtitles.get(position));
        holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(position); });
        holder.itemView.setOnLongClickListener(v -> { if (listener != null) listener.onLongClick(position); return true; });
    }

    @Override public int getItemCount() { return titles.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            subtitle = itemView.findViewById(R.id.subtitleText);
        }
    }
}
