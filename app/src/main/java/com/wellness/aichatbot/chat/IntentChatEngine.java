package com.wellness.aichatbot.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IntentChatEngine implements ChatEngine {
    private static final String HELPLINE = "+254 722 178177";
    private final CrisisDetector crisisDetector = new CrisisDetector();
    private final List<IntentPattern> patterns = new ArrayList<>();

    public IntentChatEngine() {
        patterns.add(new IntentPattern(IntentType.ANXIETY, response(
                "Anxiety can make ordinary moments feel urgent. Try a 4-6 breath: inhale for 4, exhale for 6, repeat five times.",
                "Coping idea: name five things you can see, four you can feel, three you can hear, two you can smell, and one you can taste.",
                "Journal prompt: What is the worry predicting, and what evidence supports a kinder possibility?",
                "Article to read: NIMH - Anxiety Disorders overview."),
                "anxious", "anxiety", "worried", "nervous", "fear", "scared", "uneasy"));
        patterns.add(new IntentPattern(IntentType.STRESS, response(
                "Stress is often a signal that your system has carried too much for too long.",
                "Try a two-minute reset: unclench your jaw, lower your shoulders, breathe slowly, then pick only the next small task.",
                "Journal prompt: What is urgent, what is important, and what can wait until tomorrow?",
                "Article to read: WHO - stress management guidance."),
                "stress", "stressed", "pressure", "overwhelmed", "too much", "tense"));
        patterns.add(new IntentPattern(IntentType.DEPRESSION, response(
                "I am sorry it feels heavy right now. Low mood deserves care, patience, and support.",
                "Try one tiny anchor: drink water, open a curtain, or send one honest message to someone safe.",
                "This is not a diagnosis. If this has lasted more than two weeks or affects daily life, consider speaking with a mental health professional.",
                "Article to read: WHO - depression fact sheet."),
                "depressed", "depression", "hopeless", "empty", "worthless", "sad", "numb"));
        patterns.add(new IntentPattern(IntentType.LONELINESS, response(
                "Loneliness can hurt even when people are nearby. Your need for connection is valid.",
                "Try a low-pressure reach-out: send 'Thinking of you. Can we talk this week?' to one person.",
                "Mindfulness activity: place a hand on your chest and say, 'This is a hard moment, and I deserve kindness.'",
                "Article to read: Mental Health Foundation - loneliness."),
                "lonely", "alone", "isolated", "no friends", "left out"));
        patterns.add(new IntentPattern(IntentType.BURNOUT, response(
                "Burnout is not laziness. It is often prolonged stress meeting too little recovery.",
                "Try the three-list reset: must do, can delegate, can pause. Protect one recovery block today.",
                "Journal prompt: Where am I spending energy without replenishment?",
                "Article to read: WHO - burnout occupational phenomenon."),
                "burnout", "burned out", "exhausted", "drained", "fatigued", "can't keep up"));
        patterns.add(new IntentPattern(IntentType.PANIC, response(
                "Panic can feel frightening, but the wave usually rises, peaks, and passes.",
                "Breathing exercise: inhale 4, hold 2, exhale 6. Put both feet on the floor and describe the room out loud.",
                "If chest pain, fainting, or severe physical symptoms are present, seek urgent medical care.",
                "Article to read: NHS - panic attacks."),
                "panic", "panic attack", "heart racing", "can't breathe", "dizzy"));
        patterns.add(new IntentPattern(IntentType.OVERTHINKING, response(
                "Overthinking often tries to protect you by rehearsing every outcome.",
                "Try a worry window: write the thought down, set a 10-minute timer, then choose one action or let it rest.",
                "Journal prompt: Is this problem solvable now, or is my mind asking for reassurance?",
                "Article to read: APA - managing rumination."),
                "overthinking", "ruminating", "can't stop thinking", "spiral", "what if"));
        patterns.add(new IntentPattern(IntentType.LOW_MOTIVATION, response(
                "Low motivation is information, not a character flaw.",
                "Try the five-minute start: choose the smallest useful action and stop after five minutes if needed.",
                "Journal prompt: What would make this task 20 percent easier?",
                "Article to read: Better Health - motivation and habits."),
                "unmotivated", "no motivation", "lazy", "procrastinate", "can't start"));
        patterns.add(new IntentPattern(IntentType.SELF_ESTEEM, response(
                "Self-critical thoughts can sound convincing without being fair.",
                "Try talking to yourself as you would to a close friend in the same situation.",
                "Journal prompt: What are three qualities I showed this week, even quietly?",
                "Article to read: NHS - raising low self-esteem."),
                "self esteem", "hate myself", "not good enough", "insecure", "ugly", "failure"));
        patterns.add(new IntentPattern(IntentType.SLEEP, response(
                "Sleep struggles are frustrating, especially when your mind is still busy.",
                "Try a wind-down cue: dim lights, put the phone away, and do 10 slow breaths before bed.",
                "Journal prompt: What is my mind trying to solve tonight that can be parked until morning?",
                "Article to read: Sleep Foundation - sleep hygiene."),
                "sleep", "insomnia", "can't sleep", "tired", "nightmares", "awake"));
        patterns.add(new IntentPattern(IntentType.ACADEMIC_PRESSURE, response(
                "Academic pressure can make your worth feel tied to performance. You are more than a grade.",
                "Try a study sprint: 25 minutes focused, 5 minutes recovery. Start with the easiest visible task.",
                "Journal prompt: What support, clarification, or extension could I ask for?",
                "Article to read: UNICEF - exam stress tips."),
                "exam", "school", "university", "assignment", "grades", "academic", "study"));
        patterns.add(new IntentPattern(IntentType.RELATIONSHIP, response(
                "Relationship challenges can stir up fear, anger, and grief at the same time.",
                "Try a calmer script: 'When this happened, I felt __. What I need is __.'",
                "Journal prompt: What boundary or honest conversation would protect my wellbeing?",
                "Article to read: HelpGuide - healthy relationships."),
                "relationship", "partner", "breakup", "friend", "family", "argument", "conflict"));
        patterns.add(new IntentPattern(IntentType.ANGER, response(
                "Anger often points to a boundary, hurt, or need that matters.",
                "Try the pause: step away, cool your body with water, breathe slowly, then decide what response matches your values.",
                "Journal prompt: What is underneath the anger: hurt, fear, shame, or exhaustion?",
                "Article to read: APA - controlling anger before it controls you."),
                "angry", "anger", "rage", "furious", "mad", "irritated"));
        patterns.add(new IntentPattern(IntentType.GRIEF, response(
                "Grief has its own rhythm. There is no perfect way to miss someone or something important.",
                "Try one gentle ritual: light a candle, write a memory, or talk to someone who can sit with the loss.",
                "Journal prompt: What do I wish others understood about this grief?",
                "Article to read: Mind - bereavement and grief."),
                "grief", "grieving", "loss", "died", "death", "mourning"));
    }

    @Override
    public BotReply replyTo(String userMessage) {
        String normalized = normalize(userMessage);
        if (crisisDetector.isCrisis(normalized)) {
            return new BotReply(IntentType.CRISIS, crisisResponse(), true);
        }

        IntentPattern best = null;
        int bestScore = 0;
        for (IntentPattern pattern : patterns) {
            int score = pattern.score(normalized);
            if (score > bestScore) {
                best = pattern;
                bestScore = score;
            }
        }

        if (best != null && bestScore > 0) {
            return new BotReply(best.getType(), best.getResponse() + disclaimer(), false);
        }
        return new BotReply(IntentType.GENERAL_WELLNESS, response(
                "Thank you for sharing that. I can help you reflect, slow down, and choose a next step.",
                "Try a one-minute check-in: What am I feeling? Where do I feel it in my body? What do I need next?",
                "Journal prompt: What would support look like today?",
                "Article to read: WHO - mental health and wellbeing.") + disclaimer(), false);
    }

    private String normalize(String message) {
        return message == null ? "" : message.toLowerCase(Locale.US).trim();
    }

    private String response(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (builder.length() > 0) {
                builder.append("\n\n");
            }
            builder.append(line);
        }
        return builder.toString();
    }

    private String disclaimer() {
        return "\n\nI cannot provide a diagnosis. If this feels persistent, intense, or unsafe, please contact a qualified professional or trusted support person.";
    }

    private String crisisResponse() {
        return "I am really glad you said this. If you might hurt yourself or someone else, please seek immediate support now.\n\n"
                + "Call or message the mental health helpline: " + HELPLINE + "\n\n"
                + "If there is immediate danger, contact local emergency services or go to the nearest hospital/emergency department.\n\n"
                + "While you reach support: move away from anything you could use to harm yourself, stay near another person if possible, and take slow breaths with both feet on the floor.";
    }
}
