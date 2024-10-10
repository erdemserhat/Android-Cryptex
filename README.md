
# Encryptext: Text Encryptor

Encryptext is a modern Android application that allows you to securely encrypt and decrypt your texts, supporting both Turkish and English languages. Thanks to Google ML Kit integration, you can read texts through your camera and perform encryption or decryption operations.

<div align="center">

[![psico](https://github.com/user-attachments/assets/701a02fc-2daa-4a81-b911-2009b0cd08a7)](https://play.google.com/store/apps/details?id=com.asteriatech.androidcryptex)

(The app is expected to be available on the Play Store around 09/07/2024.)

</div>

## Features

- **Text Reading via Camera:** With Google ML Kit integration, you can encrypt and decrypt texts using your camera.
- **Secure Encryption:** Your texts are uniquely encrypted according to a key value you set, preventing anyone else with the app from decrypting your text.
- **Sharing:** Directly share the encrypted text through the app.
- **Ease of Use:** A simple and user-friendly interface for encryption and decryption operations.
- **Copy and Paste:** Easily copy and paste the encrypted or decrypted text into other applications.
- **No Local Storage:** For your security, no texts are stored locally.
- **Language Support:** Available in Turkish and English.

## Application Screenshots

![ui](https://github.com/user-attachments/assets/73ba28bb-354d-4868-a3e6-19cb160e8933)

## Installation

1. Download the app from the [Google Play Store](https://play.google.com/store/apps/details?id=com.asteriatech.androidcryptex) or the [Direct APK](https://drive.google.com/file/d/1jn7Fz2jAHXPG7JlySVjEtEx3ZP9HB6SM/view?usp=drive_link).
2. Open the app and either manually enter the text you want to encrypt or use your camera to capture it.
3. Press the "Encrypt" button to encrypt the text or the "Decrypt" button to view the decrypted text.

## Usage

1. **Manual Text Encryption:**
   - On the main screen of the app, enter the text you want to encrypt. You can also set a specific key value for the entered text; both the app and the key value will be necessary for decryption.
   - Press the "Encrypt" button.
   - You can copy or share the encrypted text.

2. **Manual Decryption:**
   - Paste the text that needs to be decrypted on the main screen of the app and enter the key value if required.
   - Press the "Decrypt" button.
   - You can view and copy the decrypted text.

3. **Text Reading and Encryption via Camera:**
   - Tap the camera button in the app to switch to text recognition mode and enter the key value if necessary.
   - Point your camera at the text you want to encrypt and press the "Encrypt" button once the text is recognized.
   - You can copy or share the encrypted text.

4. **Text Reading and Decryption via Camera:**
   - Point your camera at the text that needs to be decrypted and press the "Decrypt" button once the text is recognized.
   - You can view and copy the decrypted text.

## Development Process

Encryptext has been developed using the Kotlin programming language and Jetpack Compose. The project follows the MVVM architectural structure and is optimized with modern Android development techniques and Google ML Kit integration.

### Technologies Used

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **Encryption:** Shifting
- **Text Recognition:** Google ML Kit

## Contributing

If you would like to contribute to this project, please open a pull request or report an issue via the [GitHub repository](#).

## License

This project is licensed under the [MIT License](LICENSE). Please refer to the `LICENSE` file for details.

## Contact

If you have any questions about the project or need support, please reach out to me via [erdemserhat.com](https://erdemserhat.com).

---

Thank you! We appreciate your use of Encryptext. We look forward to your feedback.
