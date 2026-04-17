# 🏠 Piattaforma per la gestione di servizi immobiliari

Progetto universitario per l’esame di **Ingegneria del Software** (a.a. 2024/2025).  
Consente di visualizzare inserzioni immobiliari, proporre offerte, valutare la vicinanza a servizi tramite mappa interattiva.

## 📌 Funzionalità principali

- Autenticazione utenti (login/registrazione) tramite **AWS Cognito**
- Visualizzazione di annunci immobiliari (lettura da database)
- Proposta di offerte su singoli immobili
- Mappa interattiva che mostra la distanza dai servizi (scuole, trasporti, negozi)
- Back-end monolitico con architettura a strati

## 🛠️ Tecnologie utilizzate

| Area | Tecnologie |
|------|-------------|
| **Linguaggio** | Java (JDK 17) |
| **Interfaccia grafica** | Swing |
| **Database** | PostgreSQL su AWS RDS |
| **Autenticazione** | AWS Cognito |
| **IDE** | Eclipse |
| **Versionamento** | Git / GitHub |

## 🚀 Come eseguire il progetto (sviluppatori)

### Prerequisiti
- Java 17+
- Connessione Internet (per AWS)
- Credenziali valide per AWS Cognito e RDS (fornite separatamente)

### Configurazione
1. Clona il repository:
   ```bash
   git clone https://github.com/g-visconti/CodProgettoIngSw055_public.git
