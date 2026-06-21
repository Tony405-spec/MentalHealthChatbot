package com.example.mindcare.repository;

import com.example.mindcare.models.Article;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {
    public List<Article> articles() {
        List<Article> items = new ArrayList<>();
        items.add(new Article("Stress management", "Reset your nervous system", "Try a 4-6 breath: inhale for four, exhale for six, and repeat for three minutes."));
        items.add(new Article("Anxiety", "Grounding in five senses", "Name five things you see, four you feel, three you hear, two you smell, and one you taste."));
        items.add(new Article("Depression awareness", "Tiny steps still count", "Low energy is real. Choose one small action: water, light, shower, message, or a five-minute walk."));
        items.add(new Article("Sleep improvement", "A softer landing into sleep", "Dim screens, keep the room cool, and repeat a calming routine at the same time nightly."));
        items.add(new Article("Self-care", "Care without perfection", "Self-care can be practical: eating, resting, asking for help, boundaries, and gentler self-talk."));
        return items;
    }
}
