# Mindful Support: AI Mental Health Chatbot

<div align="center">

![Mindful Support Banner](https://capsule-render.vercel.app/api?type=waving&color=0:6C63FF,100:FF6B6B&height=200&section=header&text=Mindful%20Support&fontSize=48&fontColor=FFFFFF&animation=fadeIn)

<br/>

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com)
[![API Level](https://img.shields.io/badge/Min%20API-21-FF6B6B?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Status](https://img.shields.io/badge/Status-Development%20Ready-00C853?style=for-the-badge&logo=github&logoColor=white)](https://github.com/skynet-datagrid-labs/MindfulSupport)
[![License](https://img.shields.io/badge/License-MIT-FFD700?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](https://opensource.org/licenses/MIT)

<br/>

**A native Android Java application delivering intent-based mental wellness support through conversational AI.**

<br/>

[![Download APK](https://img.shields.io/badge/📥_Download_APK-v1.0.0-3DDC84?style=for-the-badge&logo=android&logoColor=white&labelColor=0D1117)](base%20(1).apk)
[![View Demo](https://img.shields.io/badge/▶️_Live_Demo-View_GIF-FF6B6B?style=for-the-badge&logo=github&logoColor=white&labelColor=0D1117)](#-live-demonstration)

</div>

---

##  Executive Summary

Mindful Support is a production-ready Android application that delivers intent-based conversational support for mental wellness. The application recognises structured user intents across **sixteen distinct mental health categories**—including anxiety, stress, depression, burnout, grief, and academic pressure—and responds with supportive educational content, coping techniques, and mindfulness exercises.

**Core Value Proposition:** Provide an accessible, low-friction entry point for users seeking mental wellness support while maintaining clear boundaries around clinical diagnosis. Every response includes an appropriate diagnostic disclaimer, and the application surfaces emergency support instructions and a verified helpline number when crisis language is detected.

| Attribute | Specification |
|:---|:---|
| **Author** | Tony Kenga |
| **ORCID** | 0009-0007-6899-8590 |
| **Organization** | skynet-datagrid-labs |
| **Platform** | Android (Native Java) |
| **Min SDK** | API Level 21 (Android 5.0) |
| **Target SDK** | API Level 33 (Android 13) |
| **Status** | Development Ready |

---

##  Live Demonstration

<div align="center">

![Mindful Support Demo](assets/MINDEASEAPP.gif)

*Interactive demonstration of the Mindful Support chatbot interface, intent recognition, and crisis response protocol.*

</div>

---

##  Project Architecture

### Business Context

Mental health support systems face a significant access gap in many regions, with limited clinical resources and social stigma often acting as barriers to seeking help. Mindful Support addresses this gap through a mobile-first conversational interface that offers immediate, judgement-free interaction.

The application is designed to serve as a complement to—not a replacement for—professional mental health care. Its intent recognition engine is calibrated to identify common mental health concerns and provide evidence-informed coping strategies, breathing exercises, journaling prompts, and mindfulness activities appropriate to the user's expressed state.

### Technical Architecture

The application follows a modular architecture designed for maintainability and future extensibility.

| Component | Description |
|:---|:---|
| **ChatEngine Interface** | Abstraction layer allowing future integration with Gemini, OpenAI API, or other LLM providers without replacing the chat user interface |
| **Local Persistence** | SharedPreferences management for session state, chat history, theme preferences, and user settings |
| **Intent Classifier** | Rule-based intent recognition engine mapping user input to one of sixteen mental health categories |
| **Response Generator** | Context-aware response system delivering coping techniques, breathing exercises, mindfulness activities, journaling prompts, article suggestions, and diagnostic disclaimers |

---

##  Core Features

| Module | Description |
|:---|:---|
| **Authentication** | Login screen with local session management using SharedPreferences; dashboard sign-out functionality |
| **Dashboard** | Personalized greeting, daily quote, mood summary, quick action cards, journal prompt, analytics preview, chatbot shortcut, emergency support access, and donation entry point |
| **Chatbot Interface** | Session history with SharedPreferences persistence, message bubbles, timestamps, typing indicator, and crisis detection protocol |
| **Intent Recognition** | Structured classification for anxiety, stress, depression, loneliness, burnout, panic, overthinking, low motivation, self-esteem, sleep, academic pressure, relationships, anger, grief, and general wellness |
| **Crisis Response** | Emergency language detection with immediate helpline display: `+254 722 178177` and support instructions |
| **Theme Engine** | Dynamic theme switching with Cream Wellness, Light, and Dark themes; user preference stored in SharedPreferences |

---

##  Repository Structure

```
MindfulSupport/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/mentalhealth/
│   │   │   │   ├── activities/           # Login, Dashboard, Chatbot, Donation screens
│   │   │   │   ├── adapters/             # Message list adapters
│   │   │   │   ├── models/               # Data models (Message, User, etc.)
│   │   │   │   ├── utils/                # Intent classifiers, response generators
│   │   │   │   ├── network/              # API clients and MpesaConfig.java
│   │   │   │   └── ChatEngine.java       # Interface for LLM integration
│   │   │   ├── res/
│   │   │   │   ├── layout/               # Activity and fragment layouts
│   │   │   │   ├── values/               # Colors, strings, themes
│   │   │   │   └── drawable/             # Icons and assets
│   │   │   └── AndroidManifest.xml
│   └── build.gradle                       # App-level dependencies
├── gradle/
├── build.gradle                           # Project-level configuration
├── base (1).apk                          # Release APK
└── README.md
```

---

##  Build Instructions

| Step | Action |
|:---|:---|
| **1** | Open the project folder (`MindfulSupport/`) in Android Studio |
| **2** | Wait for Gradle sync to complete |
| **3** | Select the `app` configuration in the run menu |
| **4** | Run on an emulator or a physical Android device (API level 21+) |

**Minimum SDK:** Android 5.0 (API Level 21)  
**Target SDK:** Android 13 (API Level 33)

---

##  Security & Payment Integration

### M-Pesa Express Donation

The application includes M-Pesa Express donation functionality. The credentials required for this integration are stored in `MpesaConfig.java`. This is provided for development completeness only.

| Requirement | Action |
|:---|:---|
| **Pre-production** | Move `CUSTOMER_KEY`, `CUSTOMER_SECRET`, and `PASSKEY` to a secure backend service |
| **Production** | Never store client-side secrets in APK; Android applications can be decompiled, exposing all embedded credentials |
| **Recommended** | Implement server-side proxy for all M-Pesa API calls with request signing performed on the backend |

---

##  Data Privacy & Ethical Considerations

| Principle | Implementation |
|:---|:---|
| **Local Storage** | All chat history and user preferences are stored locally on the device using SharedPreferences; no data is transmitted to external servers without user action |
| **Diagnostic Disclaimer** | Every response includes a clear disclaimer that the chatbot is not a substitute for professional clinical care |
| **Crisis Protocol** | Emergency language detection immediately surfaces crisis resources and the national helpline number; users are encouraged to contact professional support |
| **User Autonomy** | No persistent user registration; session management is device-local and can be cleared by the user at any time |

---

##  Contribution Framework

| Action | Requirement |
|:---|:---|
| **Proposed changes** | Open an issue with documented rationale and user impact assessment |
| **Code contributions** | Submit a pull request maintaining Java compatibility and Android framework standards |
| **Response content** | Contributions to response libraries must include evidence-based references or clinical review notes |
| **Theme additions** | Follow existing color palette structure and accessibility contrast requirements |

---

##  Supporting Resources

| Asset | Location |
|:---|:---|
| **Emergency Helpline** | `+254 722 178177` |
| **M-Pesa Integration** | `app/src/main/java/.../network/MpesaConfig.java` |
| **APK Download** | [base (1).apk](base%20(1).apk) |
| **Live Demo** | [assets/MINDEASEAPP.gif](assets/MINDEASEAPP.gif) |

---

##  Contact

| | |
|:---|:---|
| **Author** | Tony Kenga |
| **ORCID** | 0009-0007-6899-8590 |
| **Organization** | [skynet-datagrid-labs](https://github.com/skynet-datagrid-labs) |
| **Repository** | [github.com/skynet-datagrid-labs/MindfulSupport](https://github.com/skynet-datagrid-labs/MindfulSupport) |

---

<div align="center">

**Built with compassion. Engineered with precision. Deployed with purpose.**

**Mindful Support** — *AI Mental Health Chatbot*

<br/>

![Footer](https://capsule-render.vercel.app/api?type=waving&color=0:FF6B6B,100:6C63FF&height=100&section=footer)

</div>
