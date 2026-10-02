# Task Manager — laboratorio JPA e Spring Security

API didattica per task personali, usata per esplorare relazioni JPA, accesso per proprietario, autenticazione JWT e ciclo dei refresh token.

## Stato e ruolo

**Laboratorio di integrazione; non un prodotto completo.** Il codice conserva anche implementazioni sviluppate con guida. Il lavoro futuro, se scelto, riguarda prove mirate, review e debugging dei meccanismi presenti; non richiede di ricostruire il progetto né di aggiungere un altro CRUD.

## Funzionalità presenti nel codice

- Entity `User`, `Task` e `RefreshToken`, persistenza MySQL tramite Spring Data JPA.
- Registrazione con BCrypt e autenticazione tramite `AuthenticationManager`.
- Access token JWT verificati da OAuth2 Resource Server.
- CRUD task filtrato per username ricavato dal principal.
- Refresh token opaco: hash SHA-256 persistito, verifica di scadenza/revoca, rotazione e logout.
- DTO per richieste e risposte e gestione centralizzata degli errori.

## Endpoint dichiarati

| Metodo | Endpoint | Scopo |
| --- | --- | --- |
| POST | `/users/register` | Registrazione |
| POST | `/auth/login` | Access e refresh token |
| POST | `/auth/refresh` | Rinnovo e rotazione |
| POST | `/auth/logout` | Revoca refresh |
| GET | `/tasks` | Task del proprietario |
| GET | `/tasks/{id}` | Dettaglio del proprietario |
| POST | `/tasks` | Creazione |
| PUT / PATCH | `/tasks/{id}` | Modifica |
| DELETE | `/tasks/{id}` | Eliminazione |
| GET | `/csrf` | Token CSRF secondo configurazione corrente |

Il login restituisce il refresh nel JSON; refresh e logout lo ricevono nel body. **Non è implementato il trasporto tramite cookie HttpOnly.** Non confondere questo contratto con [Expense Tracker](https://github.com/fabiozagaria/expense-tracker-api).

## Configurazione e avvio

Java 21, Spring Boot 4.1.0, Maven e MySQL. La configurazione corrente punta al database `task_manager` su localhost e usa `ddl-auto=update`.

Impostare valori propri tramite `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` e `SECURITY_JWT_SECRET`; usare una chiave adeguata a HS256. Non versionare segreti o token reali.

```bash
./mvnw spring-boot:run
```

Su Windows usare `mvnw.cmd`.

## Verifiche e limiti

```bash
./mvnw test
```

La suite presente include il test del contesto; non dimostra una copertura comportamentale completa.

- La policy sessioni è `STATELESS`, ma CSRF resta attivo salvo le esclusioni per registrazione, login e refresh. Le altre operazioni mutative richiedono un flusso CSRF coerente con la configurazione; la sola presenza di Bearer non lo disabilita.
- La rotazione è presente, ma `refresh(...)` non delimita attualmente l'intera operazione con una singola transazione di service e non mostra un controllo concorrente dedicato. L'uso monouso va verificato anche con richieste simultanee.
- Il logout revoca il refresh; l'access JWT resta valido fino alla scadenza.
- Il PUT riceve direttamente un'entità: i confini dei DTO e la validazione vanno riesaminati nelle prove mirate.
- Cookie, configurazione browser/produzione e test dei casi negati restano fuori dal completamento attuale.

## Eventuale ripresa

Scegliere una sola verifica: isolamento fra utenti, refresh concorrente, logout o comportamento CSRF. Il traguardo è spiegare e verificare il meccanismo, non ampliare il dominio.
