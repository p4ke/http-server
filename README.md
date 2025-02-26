## HTTP Server

- Einfache Implementation eines HTTP/1.1 Servers in Java
    - Siehe https://www.rfc-editor.org/rfc/rfc2616
- Ohne Nutzung von Bibliotheken in der Runtime

### Testen

Unter Windows in Powershell: `./gradlew.bat run`
Dann sollte eine Beispielwebsite im Browser unter http://127.0.0.1:8080 erreichbar sein.

### Hinweise

Dies ist keine vollständige Implementation des HTTP/1.1 Protokolls, da einige Funktionen teilweise unvollständig sind
oder komplett fehlen. Allerdings wären diese für dieses Schulprojekt nicht wirklich wichtig. Dazu zählen zum Beispiel:

- Unterstützung von Anfragen mit Binär-Inhalt
- Vernünftige Unterstützung von Charsets
- Standard-Header bei Http-Antworten
- Das Meiste im Zusammenhang mit Websockets
- Respektierung des "Accept"-Headers, siehe https://www.rfc-editor.org/rfc/rfc2616#section-14.1
- Unterstützung von "Content Codings" (u.a. Kompression), siehe https://www.rfc-editor.org/rfc/rfc2616#section-14.41
- Unterstützung von "Transfer Codings", siehe https://www.rfc-editor.org/rfc/rfc2616#section-3.6
- Persistente Verbindungen zu Browsern, siehe https://www.rfc-editor.org/rfc/rfc2616#section-8.1
- Die Http "OPTIONS"-Methode, siehe https://www.rfc-editor.org/rfc/rfc2616#section-9.2
