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

### ✨ Navigation Scheme

> On the first start of the app, user needs to log in or sign up on our Auth0 service.<br>The screen he sees only contains welcome words and a button that opens browser window with Auth0.

These bullets are accessible from the bottom bar after logging in:

- **Home** – displays current user's profile and stats
  - **Followers** (through Followers button) – displays current user's followers in a list
  - **Followings** (through Followings button) – displays current user's followed users in a list
  - **Edit profile** (through Edit profile button) – allows to update current user's profile (for new image to be displayed, restart is required)
- **History** – displays matches that were played by current user
  - **Move history** (through Match card) – allows analytics of moves played
- **Ranking** – displays users ranked by their ELO in each mode
  - **User profile** (through User card) – displays selected user's profile and stats
    - **Followers** (through Followers button) – displays selected user's followers in a list
    - **Followings** (through Followings button) – displays selected user's followed users in a list
    - **Edit profile** (through Edit profile button) – allows to update selected user's profile (for new image to be displayed, restart is required)
- **Social** – displays 4 tabs: **Friends**, **Received requests** *(with options to either Accept or Reject each request)*, **Sent requests** *(with option to cancel each)*, **All users**
  - **User profile** (through User card) – displays selected user's profile and stats
    - **Followers** (through Followers button) – displays selected user's followers in a list
    - **Followings** (through Followings button) – displays selected user's followed users in a list
    - **Edit profile** (through Edit profile button) – allows to update selected user's profile (for new image to be displayed, restart is required)
- **More** – allows to change the app's look or sign out

### 🗃️ Databases Schema

> Graphical schema available here: [LINK]()

#### Backend – PostgreSQL

The backend uses a relational PostgreSQL database to manage all user data, matches, moves, and social interactions. Here's an overview of the main tables:

#### 🧑‍💼 `user`
Stores information about each user:
- `uuid` – unique user identifier (PK)
- `username`, `image_url`, `created_at`

#### 📊 `rating`
Stores a user's rating for a specific game mode:
- `user_uuid` – links to the `user` table (FK)
- `rating`, `mode`

#### 🧩 `match`
Represents a completed or in-progress match:
- `id` – match identifier (PK)
- `white_uuid`, `black_uuid` – players (FK → `user`)
- `start_at`, `end_at`, `is_ranked`, `is_completed`, `winner_uuid`, `mode`

#### ➡️ `move`
Records each move made during a match:
- `match_id` – link to the `match` table (FK)
- `from`, `to` – board positions
- `uuid` – user who made the move (FK → `user`)
- `moved_at`, `time_left`

#### 👥 `friend_relation`
Stores friendships between users:
- `first_uuid`, `second_uuid` – users involved in the friendship (FK → `user`)
- `created_at`, `befriended_at`

#### 👣 `following`
Tracks user follow relationships:
- `follower_uuid`, `followed_user_uuid` – users in a follow relationship (FK → `user`)

#### Mobile – SQLite

The mobile app uses a lightweight local SQLite database to store user preferences for the application.

#### ⚙️ `user_preference`
Stores local app settings such as UI theme preferences:
- `key` – preference key (PK)
- `value` – stored value for the given key

<br>
<br>

---

<br>
<br>

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

### ✨ Schemat Nawigacji

> Przy pierwszym uruchomieniu aplikacji użytkownik musi się zalogować lub zarejestrować przez usługę Auth0.<br>Ekran powitalny zawiera jedynie przywitanie oraz przycisk otwierający przeglądarkę z logowaniem Auth0.

Poniższe sekcje są dostępne z dolnego paska nawigacji po zalogowaniu:

- **Home** – wyświetla profil oraz statystyki aktualnie zalogowanego użytkownika
  - **Followers** (przez przycisk Followers) – lista obserwujących bieżącego użytkownika
  - **Followings** (przez przycisk Followings) – lista użytkowników obserwowanych przez bieżącego użytkownika
  - **Edit profile** (przez przycisk Edit profile) – umożliwia edycję profilu bieżącego użytkownika (aby nowe zdjęcie się pojawiło, wymagane jest ponowne uruchomienie aplikacji)
- **History** – pokazuje rozegrane partie bieżącego użytkownika
  - **Move history** (poprzez kartę meczu) – umożliwia analizę wykonanych ruchów
- **Ranking** – wyświetla użytkowników uporządkowanych według ELO w każdym trybie
  - **User profile** (poprzez kartę użytkownika) – pokazuje profil oraz statystyki wybranego użytkownika
    - **Followers** (przez przycisk Followers) – lista obserwujących wybranego użytkownika
    - **Followings** (przez przycisk Followings) – lista użytkowników obserwowanych przez wybranego użytkownika
    - **Edit profile** (przez przycisk Edit profile) – umożliwia edycję profilu wybranego użytkownika (aby nowe zdjęcie się pojawiło, wymagane jest ponowne uruchomienie aplikacji)
- **Social** – zawiera 4 zakładki: **Friends**, **Received requests** *(z opcją Akceptuj lub Odrzuć każde zaproszenie)*, **Sent requests** *(z możliwością anulowania)*, **All users**
  - **User profile** (poprzez kartę użytkownika) – pokazuje profil oraz statystyki wybranego użytkownika
    - **Followers** (przez przycisk Followers) – lista obserwujących wybranego użytkownika
    - **Followings** (przez przycisk Followings) – lista użytkowników obserwowanych przez wybranego użytkownika
    - **Edit profile** (przez przycisk Edit profile) – umożliwia edycję profilu wybranego użytkownika (aby nowe zdjęcie się pojawiło, wymagane jest ponowne uruchomienie aplikacji)
- **More** – umożliwia zmianę wyglądu aplikacji oraz wylogowanie się

### 🗃️ Schemat baz danych

> Graficzny schemat dostępny pod linkiem: [LINK]()

#### Backend – PostgreSQL

Backend korzysta z relacyjnej bazy danych PostgreSQL i zarządza wszystkimi danymi użytkowników, meczów, ruchów oraz relacji społecznych. Oto przegląd najważniejszych tabel:

#### 🧑‍💼 `user`
Przechowuje dane każdego użytkownika:
- `uuid` – unikalny identyfikator użytkownika (PK)
- `username`, `image_url`, `created_at`

#### 📊 `rating`
Przechowuje ranking użytkownika dla danego trybu gry:
- `user_uuid` – powiązanie z tabelą `user` (FK)
- `rating`, `mode`

#### 🧩 `match`
Reprezentuje rozegrany mecz:
- `id` – identyfikator meczu (PK)
- `white_uuid`, `black_uuid` – gracze (FK → `user`)
- `start_at`, `end_at`, `is_ranked`, `is_completed`, `winner_uuid`, `mode`

#### ➡️ `move`
Rejestruje ruchy wykonane w ramach meczu:
- `match_id` – powiązanie z tabelą `match` (FK)
- `from`, `to` – pozycje na szachownicy
- `uuid` – kto wykonał ruch (FK → `user`)
- `moved_at`, `time_left`

#### 👥 `friend_relation`
Przechowuje relacje znajomości między użytkownikami:
- `first_uuid`, `second_uuid` – użytkownicy (FK → `user`)
- `created_at`, `befriended_at`

#### 👣 `following`
Rejestruje, kto kogo obserwuje:
- `follower_uuid`, `followed_user_uuid` – użytkownicy (FK → `user`)

#### Mobile – SQLite

W aplikacji mobilnej wykorzystywana jest lekka lokalna baza SQLite, która przechowuje preferencje użytkownika.

#### ⚙️ `user_preference`
Służy do zapisu lokalnych ustawień aplikacji, takich jak tryb ciemny, kolorystyka:
- `key` – klucz preferencji (PK)
- `value` – wartość przypisana do klucza
