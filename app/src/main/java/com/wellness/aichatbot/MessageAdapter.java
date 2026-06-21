package com.wellness.aichatbot;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wellness.aichatbot.data.ChatMessage;
import com.wellness.aichatbot.theme.ThemePalette;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<ChatMessage> messages;
    private final ThemePalette palette;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public MessageAdapter(List<ChatMessage> messages, ThemePalette palette) {
        this.messages = messages;
        this.palette = palette;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.body.setText(message.getText());
        holder.time.setText(timeFormat.format(new Date(message.getTimestamp())));

        Typeface font = null;
        try {
            font = ResourcesCompat.getFont(holder.itemView.getContext(), R.font.roboto_mono);
        } catch (Exception ignored) {}

        if (font != null) {
            holder.body.setTypeface(font);
            holder.time.setTypeface(font);
        }

        LinearLayout row = (LinearLayout) holder.itemView;
        row.setGravity(message.isUser() ? Gravity.END : Gravity.START);
        holder.body.setBackgroundResource(message.isUser() ? R.drawable.message_user_bg : R.drawable.message_bot_bg);
        holder.body.setTextColor(message.isUser() ? Color.WHITE : Color.rgb(18, 18, 18));
        holder.time.setTextColor(palette.secondary);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void add(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void replaceAll(List<ChatMessage> newMessages) {
        int oldSize = messages.size();
        messages.clear();
        if (oldSize > 0) {
            notifyItemRangeRemoved(0, oldSize);
        }
        messages.addAll(newMessages);
        if (!messages.isEmpty()) {
            notifyItemRangeInserted(0, messages.size());
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        final TextView body;
        final TextView time;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            body = itemView.findViewById(R.id.messageBody);
            time = itemView.findViewById(R.id.messageTime);
        }
    }
}
