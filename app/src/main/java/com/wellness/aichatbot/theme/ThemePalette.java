package com.wellness.aichatbot.theme;

public class ThemePalette {
    public final int background;
    public final int card;
    public final int text;
    public final int secondary;
    public final int accent;
    public final boolean dark;

    public ThemePalette(int background, int card, int text, int secondary, int accent, boolean dark) {
        this.background = background;
        this.card = card;
        this.text = text;
        this.secondary = secondary;
        this.accent = accent;
        this.dark = dark;
    }
}
