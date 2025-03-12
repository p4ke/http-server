# HTTP Server

- Einfache Implementation eines HTTP/1.1 Servers in Java
- Ohne Nutzung von Bibliotheken in der Runtime

## Testen

Unter Windows in Powershell: `./gradlew.bat run`.

Dann sollte eine Beispielwebsite im Browser unter http://127.0.0.1:8080 erreichbar sein.

## Hinweise

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

## Vorgehensweise

TODO

## Probleme

TODO

## Quellen

- [RFC 2616: Hypertext Transfer Protocol -- HTTP/1.1](https://www.rfc-editor.org/rfc/rfc2616)
  - Zugriff am 12.03.2025 um 10:09 Uhr CET
  - Siehe auch Datei "sources" -> "rfc2616.txt"
- [Common MIME types - HTTP | MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/MIME_types/Common_types)
  - Zugriff am 12.03.2025 um 10:17 Uhr CET
  - Siehe auch Datei "sources" -> "MDN_HTTP_Common_MIME_types.png"
- [HTTP response status codes - HTTP | MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
  - Zugriff am 12.03.2025 um 10:14 Uhr CET
  - Siehe auch Datei "sources" -> "MDN_HTTP_HTTP_response_status_codes.png" 

Falls relevant, werden im Programmiercode teilweise auch direkt spezielle Quellen erwähnt.
