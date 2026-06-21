package com.example.mindcare.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mindcare.R;
import com.example.mindcare.adapters.SimpleCardAdapter;
import com.example.mindcare.models.Article;
import com.example.mindcare.repository.ArticleRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends Fragment {
    private final List<Article> articles = new ArticleRepository().articles();

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_fab, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.fab).setVisibility(View.GONE);
        ((android.widget.TextView) view.findViewById(R.id.screenTitle)).setText("Tips and Articles");
        RecyclerView recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        SimpleCardAdapter adapter = new SimpleCardAdapter();
        List<String> titles = new ArrayList<>(), subtitles = new ArrayList<>();
        for (Article article : articles) { titles.add(article.getTitle()); subtitles.add(article.getCategory() + "\n" + article.getBody()); }
        adapter.setItems(titles, subtitles);
        adapter.setClickListener(new SimpleCardAdapter.ClickListener() {
            @Override public void onClick(int position) { new MaterialAlertDialogBuilder(requireContext()).setTitle(articles.get(position).getTitle()).setMessage(articles.get(position).getBody()).setPositiveButton("Done", null).show(); }
            @Override public void onLongClick(int position) { }
        });
        recycler.setAdapter(adapter);
    }
}
