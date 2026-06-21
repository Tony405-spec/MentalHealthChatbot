Here is the professional documentation for the **Mindful Support** repository, formatted in **Roboto Mono** and structured as a formal client-facing document, following the same style as the previous examples.

---

[file name]: Mindful_Support_Product_Documentation.pdf
[file content begin]

===== Page 1 =====

**PROJECT DOCUMENTATION**

# Mindful Support: AI Mental Health Chatbot

**Android · Java · Intent Recognition · Crisis Response**

A native Android Java project implementing an intent-based mental wellness chatbot designed to provide immediate, supportive conversational interventions for users experiencing common mental health challenges.

**AUTHOR** Tony Kenga  
**ORCID** 0009-0007-6899-8590  
**ORGANISATION** skynet-datagrid-labs  
**PLATFORM** Android (Native Java)  
**STATUS** Development Ready  
**REPOSITORY** github.com/skynet-datagrid-labs/MindfulSupport

===== Page 2 =====

## 1. Project Overview

Mindful Support is a native Android application that delivers intent-based conversational support for mental wellness. The application recognises structured user intents across sixteen distinct mental health categories—including anxiety, stress, depression, burnout, grief, and academic pressure—and responds with supportive educational content, coping techniques, and mindfulness exercises.

The central goal of the project is to provide an accessible, low-friction entry point for users seeking mental wellness support while maintaining clear boundaries around clinical diagnosis. Every response includes an appropriate diagnostic disclaimer, and the application surfaces emergency support instructions and a verified helpline number (`+254 722 178177`) when crisis language is detected.

## 2. Business Context

Mental health support systems face a significant access gap in many regions, with limited clinical resources and social stigma often acting as barriers to seeking help. Mindful Support addresses this gap through a mobile-first conversational interface that offers immediate, judgement-free interaction.

The application is designed to serve as a complement to—not a replacement for—professional mental health care. Its intent recognition engine is calibrated to identify common mental health concerns and provide evidence-informed coping strategies, breathing exercises, journaling prompts, and mindfulness activities appropriate to the user's expressed state.

## 3. Core Features

The application is structured around the following functional modules:

| Module | Description |
|--------|-------------|
| **Authentication** | Login screen with local session management using SharedPreferences; dashboard sign-out functionality |
| **Dashboard** | Personalized greeting, daily quote, mood summary, quick action cards, journal prompt, analytics preview, chatbot shortcut, emergency support access, and donation entry point |
| **Chatbot Interface** | Session history with SharedPreferences persistence, message bubbles, timestamps, typing indicator, and crisis detection protocol |
| **Intent Recognition** | Structured classification for anxiety, stress, depression, loneliness, burnout, panic, overthinking, low motivation, self-esteem, sleep, academic pressure, relationships, anger, grief, and general wellness |
| **Crisis Response** | Emergency language detection with immediate helpline display: `+254 722 178177` and support instructions |
| **Theme Engine** | Dynamic theme switching with Cream Wellness, Light, and Dark themes; user preference stored in SharedPreferences |

===== Page 3 =====

## 4. Technical Architecture

The application follows a modular architecture designed for maintainability and future extensibility.

| Component | Description |
|-----------|-------------|
| **ChatEngine Interface** | Abstraction layer allowing future integration with Gemini, OpenAI API, or other LLM providers without replacing the chat user interface |
| **Local Persistence** | SharedPreferences management for session state, chat history, theme preferences, and user settings |
| **Intent Classifier** | Rule-based intent recognition engine mapping user input to one of sixteen mental health categories |
| **Response Generator** | Context-aware response system delivering coping techniques, breathing exercises, mindfulness activities, journaling prompts, article suggestions, and diagnostic disclaimers |

## 5. Payment Integration Note

The application includes M-Pesa Express donation functionality. The credentials required for this integration are stored in `MpesaConfig.java`. This is provided for development completeness only. The following security considerations apply:

| Requirement | Action |
|-------------|--------|
| **Pre-production** | Move `CUSTOMER_KEY`, `CUSTOMER_SECRET`, and `PASSKEY` to a secure backend service |
| **Production** | Never store client-side secrets in APK; Android applications can be decompiled, exposing all embedded credentials |
| **Recommended** | Implement server-side proxy for all M-Pesa API calls with request signing performed on the backend |

===== Page 4 =====

## 6. Repository Structure

The repository is organised as follows:

```
AIMentalHealthChatbot/
+-- app/
│   +-- src/
│   │   +-- main/
│   │   │   +-- java/com/example/mentalhealth/
│   │   │   │   +-- activities/           # Login, Dashboard, Chatbot, Donation screens
│   │   │   │   +-- adapters/             # Message list adapters
│   │   │   │   +-- models/               # Data models (Message, User, etc.)
│   │   │   │   +-- utils/                # Intent classifiers, response generators
│   │   │   │   +-- network/              # API clients and MpesaConfig.java
│   │   │   │   +-- ChatEngine.java       # Interface for LLM integration
│   │   │   +-- res/
│   │   │   │   +-- layout/               # Activity and fragment layouts
│   │   │   │   +-- values/               # Colors, strings, themes
│   │   │   │   +-- drawable/             # Icons and assets
│   │   │   +-- AndroidManifest.xml
│   +-- build.gradle                       # App-level dependencies
+-- gradle/
+-- build.gradle                           # Project-level configuration
+-- README.md
```

## 7. Build Instructions

| Step | Action |
|------|--------|
| **1** | Open the project folder (`AIMentalHealthChatbot/`) in Android Studio |
| **2** | Wait for Gradle sync to complete |
| **3** | Select the `app` configuration in the run menu |
| **4** | Run on an emulator or a physical Android device (API level 21+) |

**Minimum SDK:** Android 5.0 (API Level 21)  
**Target SDK:** Android 13 (API Level 33)

===== Page 5 =====

## 8. Data Privacy & Ethical Considerations

| Principle | Implementation |
|-----------|----------------|
| **Local Storage** | All chat history and user preferences are stored locally on the device using SharedPreferences; no data is transmitted to external servers without user action |
| **Diagnostic Disclaimer** | Every response includes a clear disclaimer that the chatbot is not a substitute for professional clinical care |
| **Crisis Protocol** | Emergency language detection immediately surfaces crisis resources and the national helpline number; users are encouraged to contact professional support |
| **User Autonomy** | No persistent user registration; session management is device-local and can be cleared by the user at any time |

## 9. Contribution Framework

| Action | Requirement |
|--------|-------------|
| **Proposed changes** | Open an issue with documented rationale and user impact assessment |
| **Code contributions** | Submit a pull request maintaining Java compatibility and Android framework standards |
| **Response content** | Contributions to response libraries must include evidence-based references or clinical review notes |
| **Theme additions** | Follow existing color palette structure and accessibility contrast requirements |

## 10. Supporting Resources

| Asset | Location |
|-------|----------|
| **Emergency helpline** | `+254 722 178177` |
| **M-Pesa integration** | `app/src/main/java/.../network/MpesaConfig.java` |

---

**Built using Android Native Java · Tony Kenga · ORCID: 0009-0007-6899-8590 · skynet-datagrid-labs**

[file content end]
