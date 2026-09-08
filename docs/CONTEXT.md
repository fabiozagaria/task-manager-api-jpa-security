# Contesto tecnico — Task Manager

Aggiornato: 2026-09-08

## Natura del repository
Laboratorio integrato su JPA/Spring Data JPA, Spring Security, JWT e Testing. Rimane `In corso`, ma dal 08/09 non è più la prova primaria di autonomia: quella viene svolta su mini-progetti separati come Verifica Sec.

## Stato tecnico registrato
- Entity `User` e `Task` con relazione Task→User.
- CRUD owner-aware.
- `CustomUserDetailsService`, BCrypt e registrazione.
- Access JWT verificato end-to-end con Bearer e `SessionCreationPolicy.STATELESS`.
- Refresh token opaco con hash SHA-256 nel DB, validazione, `POST /auth/refresh`, rotation one-time e revoca.
- Logout con `POST /auth/logout`, revoca tramite `revokedAt` in transazione e `204 No Content` senza body.

## Ruolo attuale
Conservare il valore di laboratorio storico e usarlo soprattutto per integrazione più ampia e Testing dopo le prove autonome separate.

## Punto di ripresa
Non ricominciare Security/JPA da zero. Tornare su questo repository dopo le verifiche autonome, con focus su Testing e integrazione dei meccanismi già presenti.

## Nota metodologica
Il codice presente può derivare anche da sessioni guidate precedenti. La presenza di una funzionalità qui non equivale automaticamente a competenza autonoma: la valutazione resta in Notion.