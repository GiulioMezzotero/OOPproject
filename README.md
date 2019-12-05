# OOPproject
###### di Giulio Mezzotero e Giovanni Alessandro Clini

Esame di "Programmazione ad Oggetti", insegnamento presente all'interno del corso di Laurea triennale in Ingegneria Informatica e dell’Automazione presso l'Università Politecnica delle Marche, a.a. 2018/2019.
Il progetto presente nella repository consiste in un'applicazione Java basata sul framework Spring che restituisce tramite API REST GET o POST dati e analisi in formato JSON a partire da un dataset assegnatoci in formato TSV. Il progetto può essere compilato attraverso framework (ad es. Maven) che gestiscono l'importazione delle librerie Spring.

## Funzionamento all'avvio
L'applicazione, una volta lanciata, esegue il download di un dataset in formato TSV contenuto in un JSON fornito tramite un [URL](http://data.europa.eu/euodp/data/api/3/action/package_show?id=GeGKzwDc03b3j0olhD5DQ). Il download del dataset viene salvato nella cartella del progetto con il nome di dataset.tsv. Successivamente viene effettuato il parsing del file TSV in modo da poter creare le istanze del modello. Inoltre il programma avvia un web-server in locale sulla porta 8080 che riceve richieste dall'utente.


