# MindCare

MindCare is a Java Android mental wellness app using Material Design 3, Firebase Authentication, Firestore, local reminders, SharedPreferences, RecyclerView lists, and an intent-based wellness assistant.

## Setup

1. Open this folder in Android Studio.
2. In the Firebase console, create/select a project.
3. Add an Android app with package name `com.example.mindcare`.
4. Download the real `google-services.json`.
5. Replace `app/google-services.json` in this project with that downloaded file.
6. Enable **Authentication > Sign-in method > Email/Password** in Firebase.
7. Create Firestore in test mode for development or add production rules for `moods` and `journals`.
8. Clean and rebuild the project in Android Studio.
9. Run on Android 6.0+.

If account creation says the API key is invalid, the app is still using the placeholder `google-services.json`. Firebase registration cannot work until the real Firebase config file is added.

## M-Pesa

The dashboard includes **Support Us / Donate** using Safaricom sandbox STK Push. The credentials are isolated in `MpesaRepository`. For production, move these secrets to a trusted backend and call that backend from the app.
