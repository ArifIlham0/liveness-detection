# Liveness Detection Library

Library ini menyediakan fitur deteksi liveness untuk aplikasi Android. Library ini dapat digunakan untuk memastikan bahwa wajah yang terdeteksi berasal dari manusia yang hidup, bukan dari foto atau video.

## Fitur
- Deteksi liveness berbasis kamera
- Integrasi mudah ke aplikasi Android
- Mendukung OpenCV (opencv-4.12.0.aar sudah disediakan)

## Instalasi
### 1. Instalasi Library

1. **Tambahkan repository JitPack di root `build.gradle` atau `settings.gradle.kts`:**
   ```kotlin
   dependencyResolutionManagement {
       repositories {
           maven { url = uri("https://jitpack.io") }
           // ...repository lain...
       }
   }
   ```
2. **Tambahkan dependency di `app/build.gradle.kts`:**
   ```kotlin
   dependencies {
       implementation("com.github.ArifIlham0:liveness-detection:1.0.0")
   }
   ```
   > Ganti `ArifIlham0` dan versi sesuai dengan repo dan release/tag yang Anda gunakan

## Penggunaan
1. **Inisialisasi Library**
   Biasanya dilakukan di Activity atau ViewModel:
   ```kotlin
   // Contoh inisialisasi (ganti sesuai API library)
   val livenessDetector = LivenessDetector(context)
   livenessDetector.startDetection(cameraView, callback)
   ```
2. **Implementasi Callback**
   Implementasikan callback untuk menerima hasil deteksi:
   ```kotlin
   val callback = object : LivenessCallback {
       override fun onLivenessDetected(result: Boolean) {
           if (result) {
               // Liveness terdeteksi
           } else {
               // Liveness tidak terdeteksi
           }
       }
   }
   ```

## Catatan
- Pastikan aplikasi Anda sudah memiliki permission kamera di AndroidManifest.xml:
  ```xml
  <uses-permission android:name="android.permission.CAMERA" />
  ```
- Jika menggunakan OpenCV, pastikan dependensi OpenCV sudah benar dan tidak terjadi konflik versi.


## Lisensi
Lisensi mengikuti ketentuan yang berlaku di repository ini.
