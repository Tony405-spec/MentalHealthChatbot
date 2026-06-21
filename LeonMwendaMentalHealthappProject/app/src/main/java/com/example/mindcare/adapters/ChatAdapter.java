package com.example.mindcare.adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mindcare.R;
import com.example.mindcare.models.ChatMessage;
import com.example.mindcare.utils.TimeUtils;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {
    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.text.setText((message.isUser() ? "You" : "MindCare") + " - " + TimeUtils.pretty(message.getTimestamp()) + "\n" + message.getText());
        holder.text.setGravity(message.isUser() ? Gravity.END : Gravity.START);
        holder.text.setBackgroundColor(message.isUser() ? Color.rgb(216, 232, 248) : Color.rgb(244, 246, 248));
    }

    @Override public int getItemCount() { return messages.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView text;
        Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
        }
    }
}
