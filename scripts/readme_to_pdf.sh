#!/usr/bin/env bash

# Ein Skript, womit das Markdown-Dokument "README.md" mithilfe des Linux-Programms
# "pandoc" und "typst" zu einem PDF-Dokument konvertiert wird.
# Dies ist nur zur konvertierung der README-Datei da und gehört nicht zum eigentlichen Projekt
# der Http-Server-Implementation.

# Es werden mehr Fehlerinformationen aktiviert
set -ex

# Egal von wo das Skript ausgeführt wird, es wird zuerst in das Projektverzeichnis gewechselt.
cd "$(dirname "$0")/.."

# Generiert aus dem Markdown-Dokument "README.md" das PDF-Dokument "README.pdf" mithilfe von "Typst"
exec pandoc \
  --pdf-engine="typst" \
  -V "title:HTTP Server" \
  -V "subtitle:Einfacher HTTP/1.1-Server in Java, ohne Laufzeit-Bibliotheken" \
  -V "mainfont:DejaVu Sans" \
  -V "fontsize:9pt" \
  -V "colorlinks:true" \
  --from="gfm" --to="pdf" \
  "README.md" -o "README.pdf"
