# WeatherApp

WeatherApp is an Android application that provides weather updates for the current location. The app fetches weather data from the OpenWeatherMap API and displays information such as temperature, humidity, weather condition, and location details.

## Features

- Fetches current weather data using OpenWeatherMap API.
- Displays temperature, humidity, and weather description.
- Shows the current location (latitude, longitude) and address.
- Updates weather information periodically based on location updates.
- Displays the current system time.

## Setup Instructions

Follow these steps to set up and run the WeatherApp on your local machine.

### Prerequisites

- Android Studio installed on your machine.
- An Android device or emulator to run the application.
- OpenWeatherMap API key. You can obtain it by signing up at [OpenWeatherMap](https://home.openweathermap.org/users/sign_up).

### Installation

1. **Clone the repository:**
    ```sh
    git clone https://github.com/yourusername/weatherapp.git
    cd weatherapp
    ```

2. **Open the project in Android Studio:**
    - Open Android Studio.
    - Select `Open an existing Android Studio project`.
    - Navigate to the cloned repository and select the project.

3. **Add your OpenWeatherMap API key:**
    - Open `MainActivity.java`.
    - Replace the `API_ID` value with your actual OpenWeatherMap API key:
      ```java
      final String API_ID = "your_api_key_here";
      ```

4. **Run the application:**
    - Connect your Android device or start an emulator.
    - Click on the `Run` button in Android Studio or press `Shift + F10`.

## Usage

1. **Permissions:**
    - The app requests location permissions on startup. Grant the permissions to allow the app to access the device's location.

2. **Weather Updates:**
    - The app automatically fetches and displays weather updates for the current location.

3. **UI Elements:**
    - Temperature, humidity, and weather condition are displayed.
    - Latitude, longitude, and address of the current location are shown.
    - The current system time is updated periodically.

## Additional Features

- Uses `EdgeToEdge` library to support edge-to-edge display.
- Handles location permissions and provides feedback to the user.
- Geocodes the location to get the address using `Geocoder`.
