# Seher Aloyon & Maison Aster - Android App

هذا المشروع مهيأ ومربوط بالكامل مع **Firebase** وجاهز للاستخراج كملف **APK**.

---

## ما تم إعداده وتعديله:
1. **ربط Firebase**:
   - تم تضمين ملف `google-services.json` داخل مسار `app/google-services.json`.
   - تم تحديث معرّف التطبيق (`applicationId`) في `app/build.gradle.kts` ليطابق الحزمة المسجلة في فايربيس: `com.aistudio.maisonaster.kpxqza`.
2. **إصلاح إعدادات البناء (Build Configuration)**:
   - تم تعديل وضع الـ `debug` في `app/build.gradle.kts` ليعتمد التوقيع التلقائي القياسي دون أخطاء.
   - تم إعداد ملف `.env` الافتراضي.
3. **أتمتة البناء (GitHub Actions)**:
   - تم إضافة ملف سير العمل في `.github/workflows/build-apk.yml` لبناء ملف الـ APK تلقائياً إذا رغبت في رفعه على GitHub.

---

## خطوات استخراج ملف الـ APK:

### الطريقة الأولى: عبر Android Studio (الأسهل والأسرع على جهازك)
1. قم بفك ضغط هذا المجلد على جهازك.
2. افتح برنامج **Android Studio**.
3. اختر **Open** وحدد هذا المجلد.
4. انتظر حتى ينتهي البرنامج من مزامنة المشروع (Gradle Sync).
5. من القائمة العلوية، اضغط على:
   **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**.
6. بمجرد انتهاء البناء، ستظهر رسالة بالأسفل تضغط فيها على **locate** للوصول لملف الـ APK المباشر (`app-debug.apk`) في المسار:
   `app/build/outputs/apk/debug/app-debug.apk`.

### الطريقة الثانية: بناء تلقائي عبر GitHub
1. ارفع هذا المشروع إلى مستودع (Repository) خاص أو عام على GitHub.
2. توجه إلى تبويب **Actions** في المستودع.
3. سيتم تشغيل خطوة البناء تلقائياً، وبعد دقيقتين ستجد ملف الـ APK جاهزاً للتحميل تحت قسم **Artifacts** باسم `seher-aloyon-debug-apk`.
