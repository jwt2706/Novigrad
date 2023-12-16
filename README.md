# Government Service Application: Novigrad

## Description

This proof-of-concept Android application is designed to provide a convenient and efficient way for citizens to access government services. It aims to streamline various processes and tasks that typically require physical visits to government offices or lengthy paperwork. The application is compatible with Android 7 and above.

## Features

1. **User Registration**: Users can create accounts by providing their personal information and verifying their identity, such as email, name, driver's license (with the option for photo proof), and more.

2. **Secure Authentication**: The application ensures secure user authentication using industry-standard protocols, such as password-based authentication or multi-factor authentication, all backed by Firebase Authentication.

3. **Application Submissions**: Users can submit visit requests for various services directly through the application. The app guides users through the necessary steps and provides a clear overview of required documents and available office hours.

4. **Information Protection**: Users can securely upload supporting documents and other required information to use the service. The application ensures the privacy and integrity of information given using a secured database hosted on Google's cloud infrastructure Firebase.

5. **Service Creation**: Employees can created services and make them available to regular users. The administrator is able to overview all of these changes, and modify any if needed.

## Technologies Used

Novigrad is built using a combination of modern technologies to ensure a robust and efficient user experience.

1. **Kotlin**:  A versatile programming language that is fully compatible with the Android ecosystem.

2. **Firebase Cloud**: A cloud-based NoSQL database used to store and manage user data securely with real-time synchronization and seamless intergration.

3. **Android SDK**: Provides a comprehensive set of tools, libraries, and APIs specifically designed for Android app development.

## Installation and Usage

To install the Government Service Application, follow these steps:

1. Ensure your Android device is running Android 7 or a later version.

2. Download the lastest release (`apk`) package from the "relases" tab in this repository.

3. Enable installation from unknown sources in your device's security settings if necessary.

4. Locate the downloaded `apk` file using a file manager on your device.

5. Tap the `apk` file to begin the installation process.

6. Once installed, you can find the Government Service Application on your device's home screen or app drawer.

NOTE: The database may no longer be online.

## Testing

Most of the tests written for this application are simple unit tests that are designed to attempt different inputs for database fetching and storing methods, and noting whether the attempt was successful or not. As of the latest release, all tests have passed successfully.
The test files can be found in `app/src/test/java/com/example/novigradg15`.

## UML

Unified Modeling Language diagrams have been put together to document the application's structure and expected behavior. These can be found in the `/UML` folder.

## Disclaimer

The Government Service Application is a proof-of-concept application and may not reflect the exact features or services provided by official government applications. It is solely intended for demonstration purposes and does not handle real government data or transactions. Use the application at your own risk.
This project was originally a school group assignement. As a result of this, the database for this application may no longer be properly maintainted.

## License

This application is licensed under the [MIT License](https://opensource.org/licenses/MIT).
