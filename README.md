# Cool Chess Mobile

> Select a language: [PL](#polish) / [EN](#english)

## English

**Cool Chess Mobile** is a companion mobile app developed as a school project in ZSK. While users may not always have time to sit down and play chess, this app ensures they can still stay updated and connected with the most important features of the platform — anywhere, anytime.

### 🔍 What are the functionalities?

This app doesn't support playing chess directly — instead, it focuses on powerful and useful features for every user of the Cool Chess web app:

- 🏆 **View Rankings** – Check how you stack up against other players.
- 👥 **User List** – Explore profiles of other users in the system.
- ➕ **Manage Friends & Followings** – Follow your favorite players and manage your chess circle.
- 📊 **Analyze Game History** – Revisit your previous matches and analyze your moves.
- 🎨 **Customize Appearance** – Change the look and feel of the app in Settings.

### 🛠️ Built With

- **Kotlin** & **Jetpack Compose** – Modern UI and app's logic.
- **Room Database** – Stores user preferences locally for fast access and persistence.
- **Custom Backend API** – Connects directly with our backend to fetch and send data. [LINK](https://github.com/accodash/cool-chess-backend)

### 🚀 Setup Instructions

To run the app locally, make sure you have the following entries added to your `local.properties` file in the root of your project directory:
```
auth0Domain=AUTH0_DOMAIN
auth0ClientId=AUTH0_CLIENT
auth0Audience=AUTH0_AUDIENCE

backendUrl=BACKEND_URL_WITH_PORT (example: http://192.168.91.242:8000/)
imageUrlsPrefix=http://localhost
```

---

## Polish

**Cool Chess Mobile** to mobilna aplikacja towarzysząca, stworzona jako projekt szkolny w ZSK. Choć nie zawsze możesz usiąść i zagrać w szachy, ta aplikacja pozwala Ci pozostać na bieżąco z najważniejszymi informacjami i funkcjami platformy — gdziekolwiek jesteś.

### 🔍 Jakie mamy funkcjonalności?

Aplikacja nie obsługuje rozgrywania samej w sobie partii szachów, ale oferuje inne przydatne funkcje użytkownikom webowej wersji Cool Chess:

- 🏆 **Przeglądanie Rankingu** – Zobacz swoją pozycję wśród innych graczy.
- 👥 **Lista Użytkowników** – Odkrywaj profile innych graczy.
- ➕ **Zarządzanie Znajomymi i Obserwowanymi** – Twórz swoją szachową społeczność.
- 📊 **Analiza Historii Gier** – Analizuj swoje rozegrane partie i wykonane ruchy.
- 🎨 **Personalizacja Wyglądu** – Dostosuj wygląd aplikacji do własnych preferencji.

### 🛠️ Technologie

- **Kotlin** z **Jetpack Compose** – Interfejs i logika aplikacji.
- **Room Database** – Lokalna baza danych do przechowywania ustawień użytkownika.
- **Backend API** – Integracja z naszym serwerem do pobierania i wysyłania danych. [LINK](https://github.com/accodash/cool-chess-backend)

### 🚀 Uruchomienie

Aby uruchomić aplikację lokalnie, dodaj następujące linijki do pliku `local.properties`:
```
auth0Domain=AUTH0_DOMAIN
auth0ClientId=AUTH0_CLIENT
auth0Audience=AUTH0_AUDIENCE

backendUrl=BACKEND_URL_WITH_PORT (example: http://192.168.91.242:8000/)
imageUrlsPrefix=http://localhost
```
