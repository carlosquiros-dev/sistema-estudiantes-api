# Student System API

Spring Boot web application with two modules:

1. **Students** – a full CRUD (create, read, update, delete) of students stored in PostgreSQL.
2. **Rick and Morty** – consumes the public [Rick and Morty API](https://rickandmortyapi.com) through our own backend and shows it with a nested select, a debounced search and jQuery.

Both modules follow the **MVC** pattern (Model – View – Controller), each one in its own package.

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java 17+, Spring Boot 4, Spring Data JPA, RestClient |
| Database | PostgreSQL |
| Frontend | HTML, CSS, JavaScript, jQuery 3.7 |
| Build | Maven (wrapper included) |

## Project structure

```
src/main/java/com/uce/crudestudiantes
├── CrudEstudiantesApplication.java      # entry point (main)
├── students
│   ├── model        → Student            # JPA entity (table "students")
│   ├── repository   → StudentRepository  # database access
│   ├── service      → StudentService     # business logic
│   └── controller   → StudentController  # REST endpoints /api/students
└── rickandmorty
    ├── model        → RmCharacter, RmLocation, RmEpisode, ApiResponse, PageInfo
    ├── service      → RickAndMortyService     # calls rickandmortyapi.com
    └── controller   → RickAndMortyController  # REST endpoints /api/rickandmorty

src/main/resources/static
├── index.html                 # Page 1: students
└── rickandmorty
    ├── index.html             # Page 2: structure (view)
    ├── css/styles.css         # design
    └── js/app.js              # behavior with jQuery
```

## Requirements

- JDK 17 or newer
- PostgreSQL running on `localhost:5432`
- IntelliJ IDEA (recommended) or any IDE with Maven support

## Getting started

1. **Clone the repository**
   ```bash
   git clone https://github.com/carlosquiros-dev/sistema-estudiantes-api.git
   ```
   In IntelliJ: **File → New → Project from Version Control**, paste the URL and click **Clone**. Then click **Trust Project** and **Load Maven Project**.

2. **Create the database** in PostgreSQL:
   ```sql
   CREATE DATABASE crud_db;
   ```
   The `students` table is created automatically when the app starts.

3. **Set your database credentials** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/crud_db
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_PASSWORD
   ```

4. **Run the app**: open `CrudEstudiantesApplication` and click ▶️, or run:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Open it in the browser**: http://localhost:8080

## Pages

| URL | Description |
|---|---|
| http://localhost:8080 | Students CRUD with realtime search |
| http://localhost:8080/rickandmorty/index.html | Rick and Morty explorer |

## REST endpoints

### Students

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get one student |
| POST | `/api/students` | Create a student |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |

### Rick and Morty

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/rickandmorty/locations` | All locations (fills the second select) |
| GET | `/api/rickandmorty/episodes` | All episodes (fills the second select) |
| GET | `/api/rickandmorty/locations/{id}/residents` | Characters living in a location (**nested**) |
| GET | `/api/rickandmorty/episodes/{id}/characters` | Characters in an episode (**nested**) |
| GET | `/api/rickandmorty/characters?name=rick` | Search characters by name |

All endpoints can be tested from IntelliJ with the `test.http` file.

## Rick and Morty features

- **Nested select**: the first select chooses a category (Locations or Episodes); the second one is filled with the items of that category; choosing an item loads its related characters.
- **Nested endpoints**: the backend first gets a location or episode, reads the character URLs it contains, and then gets all those characters in a single request.
- **Debounce**: the search waits 500 ms after the user stops typing before calling the API, so typing "rick" makes 1 request instead of 4.
- **jQuery**: DOM handling, events and AJAX calls (`$.getJSON`).
- **Pagination**: the backend walks through every page of the external API to return complete lists.

## Author

Carlos Quiros – Universidad Central del Ecuador (UCE)
