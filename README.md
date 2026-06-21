# Mindful Support - AI Mental Health Chatbot

Native Android Java project implementing an intent-based mental wellness chatbot.

## Features

- Login screen with local SharedPreferences session, plus dashboard sign-out.
- Dashboard with personalized greeting, daily quote, mood summary, quick actions, journal prompt, analytics preview, chatbot shortcut, emergency support, and donation access.
- Chatbot screen with session history, local SharedPreferences persistence, message bubbles, timestamps, typing indicator, and crisis detection.
- Structured intent recognition for anxiety, stress, depression, loneliness, burnout, panic, overthinking, low motivation, self-esteem, sleep, academic pressure, relationships, anger, grief, and general wellness.
- Supportive educational responses with coping techniques, breathing exercises, mindfulness activities, journaling prompts, article suggestions, and diagnosis disclaimers.
- Emergency language immediately surfaces support instructions and the helpline: `+254 722 178177`.
- Dynamic theme switching with Cream Wellness, Light, and Dark themes stored in SharedPreferences.
- `ChatEngine` interface allows future Gemini/OpenAI API integration without replacing the chat UI.
- `Support US/Donate` screen prepares M-Pesa Express donation details.

## Important Payment Note

The requested M-Pesa credentials are included in `MpesaConfig.java` for project completeness. Before production release, move `CUSTOMER_KEY`, `CUSTOMER_SECRET`, and `PASSKEY` to a secure backend. Android APKs can be decompiled, so client-side secrets are not safe.

## Build

Open this folder in Android Studio:

```text
outputs/AIMentalHealthChatbot
```

Then sync Gradle and run the `app` configuration on an emulator or Android device.
