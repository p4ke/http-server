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

Zuerst habe ich mir dieses Projekt ausgesucht - da das Http-Protokoll ein wichtiger Baustein
des heutigen Internets ist und HTTP/1.1 ein einfaches, rein textbasiertes Protokoll ist, habe ich mich
bei diesem Schulprojekt für eine einfache Implementation des HTTP/1.1-Protokolls entschieden.

Bei der Umsetzung dieses Programmierprojekts habe ich zuerst damit angefangen, ein Logging-System zu
programmieren. Danach habe ich angefangen, einen "HttpReader"-Helfer umzusetzen, da das Http-Protokoll
einige immer wieder auftretende Elemente hat.

Danach habe ich generelle Strukturen des Http-Protokolls umgesetzt wie Anfragen, Header, Status, Version und
Antworten. Schließlich habe ich den Http-TCP-Socket-Server umgesetzt und die zugehörige Logik für einen einfachen
Datei-Server geschrieben.

Am Ende habe ich einige Stellen noch ausgebessert und den Quellcode mit Kommentaren versehen, damit
auch andere den Quellcode verstehen können.

## Probleme

Wirklich große Probleme gab es bei diesem Projekt nicht. Allerdings musste ich zwischendurch den
"HttpReader"-Helfer stream-basiert umschreiben, da ich vorher davon ausgegangen war, dass der Browser
den vollständigen Inhalt absendet und die Verbindung danach direkt schließt. Der Browser lässt
die Verbindung allerdings noch offen.

## Quellen

- [RFC 2616: Hypertext Transfer Protocol -- HTTP/1.1](https://www.rfc-editor.org/rfc/rfc2616)
  - Zugriff am 12.03.2025 um 10:09 Uhr CET
  - Siehe auch Datei "sources" → "rfc2616.txt"
- [Common MIME types - HTTP | MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/MIME_types/Common_types)
  - Zugriff am 12.03.2025 um 10:17 Uhr CET
  - Siehe auch Datei "sources" → "MDN_HTTP_Common_MIME_types.png"
- [HTTP response status codes - HTTP | MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
  - Zugriff am 12.03.2025 um 10:14 Uhr CET
  - Siehe auch Datei "sources" → "MDN_HTTP_HTTP_response_status_codes.png" 

Falls relevant, werden im Programmiercode teilweise auch direkt spezielle Quellen erwähnt.
