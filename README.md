# OOPproject
###### di Giulio Mezzotero e Giovanni Alessandro Clini

Esame di "Programmazione ad Oggetti", insegnamento presente all'interno del corso di Laurea triennale in Ingegneria Informatica e dell’Automazione presso l'Università Politecnica delle Marche, a.a. 2018/2019.
Il progetto presente nella repository consiste in un'applicazione Java basata sul framework Spring che restituisce tramite API REST (GET o POST) dati e analisi in formato JSON a partire da un dataset assegnatoci in formato TSV. Il progetto può essere compilato attraverso framework (ad es. Maven) che gestiscono l'importazione delle librerie Spring.

-----

## Funzionamento all'avvio
L'applicazione, una volta lanciata, esegue il download di un dataset in formato TSV contenuto in un JSON fornito tramite un [URL](http://data.europa.eu/euodp/data/api/3/action/package_show?id=GeGKzwDc03b3j0olhD5DQ). Il download del dataset viene salvato nella cartella del progetto con il nome di dataset.tsv. Successivamente viene effettuato il parsing del file TSV in modo da poter creare le istanze del modello. Inoltre il programma avvia un web-server in locale sulla porta 8080 che riceve richieste dall'utente.

-----

## Interpretazione modello e dati
I dati sono tratti dal sito dell'Unione Europea. Il dataset TSV contiene:

| Campo | Descrizione |
| - | - |
| **indic_bt** | Indicatore del Business Trend. |
| **nace_r2** | Classificazione delle attività economiche.  |
| **s_adj** | Sigla che informa se il dato è "destagionalizzato" o meno (Seasonal Adjustment). |
| **unit** | Unità di misura. |
| **geo/time (country)** | Entità geopolitica di interesse. |
| **dati annuali** | Array di indici suddivisi per anno dal 1992 al 2018. |

-----

## Packages e classi
Il progetto presenta un package principale  `univpm.op.project` che contiene la classe main `Application` che avvia il server Spring. Le altre classi sono divise in 5 package:

-   `data`: contiene le classi  `Data` e `StringName`. La prima gestisce i dati in formato TSV, la seconda è astratta e contiene le stringhe con l'indirizzo dei dati e i nomi dei due file necessari allo sviluppo del progetto;
-   `entity`: contiene la classe  `Entity`, che struttura il nuovo tipo di dati necessario per l'immagazzinamento delle informazioni derivanti dal dataset;
-   `controller`: contiene la classe  `Controller`che gestisce le routes dell'applicazione;
-   `exception`: contiene la classe `InvalidFilterException` che gestisce le eccezioni per i filtri;
-   `utils`: contiene le classi `Utils` e `NumericAnalysis`. La prima, astratta, contiene i metodi utili allo sviluppo dell'applicazione, la seconda estrae le analisi dai dati.

Visionare il JavaDoc per informazioni più specifiche su classi e relativi metodi.

-----

## Routes dell'applicazione
Gli endpoint specificati nell'applicazione per effettuare le varie richieste sono i seguenti:

| Metodo | Endpoint | Descrizione |
| - | - | - |
| **GET** | **/** | Route che restituisce il numero di Entità del dataset. |
| **GET** | **/full** | Route che restituisce i dati del dataset in formato JSON. |
| **GET** | **/getMetadata** | Route che restituisce i metadati in formato JSON. |
| **GET** | **/getAnalytics** | Route che restituisce le analisi sui dati JSON. |
| **POST** | **/getAnalytics/filtered** | Route che mostra i dati recuperati dal TSV, eventulmente filtrati, in formato JSON. <br/> In particolare: <br/> **1. Corpo della richiesta non presente:** in questo caso verranno restituiti tutti i dati appartenenti al dataset, senza applicare alcun filtro. <br/> **2. Corpo della richiesta contenente il filtro:** in questo caso il dato verrà filtrato secondo secondo i filtri specificati. |

-----

## Filtri
Il filtro va inserito nel body della richiesta POST. I filtri che possono essere applicati sono i seguenti: 
 
| Tipo operatore | Operatore | Descrizione | Esempio di applicazione |
| - | - | - | - |
| **Logico** | **$not** | Indica se il valore associato al campo è diverso da quello indicato nel filtro. | `{ "indic_bt": { "$not": "PNUM" } }` |
| **Logico** | **$in** | Specificato un insieme di valori, indica se il valore associato al campo è uno di questi valori. | `{ "indic_bt": { "$in": [ "PNUM", "PSQM" ] } }` |
| **Logico** | **$nin** | Specificato un insieme di valori, indica se il valore associato al campo non è nessuno di questi valori. | `{ "indic_bt": { "$nin": [ "PNUM", "PSQM" ] } }`  |
| **Logico** | **$or** | Specificato un insieme di filtri il dato è accettato se almeno uno dei filtri è soddisfatto. | `{ "$or": [ { "indic_bt": "PNUM" }, {"s_adj": "NSA" } ]` |
| **Logico** | **$and** | Specificato un insieme di filtri il dato è accettato se tutti i filtri sono soddisfatti. | `{ "$and": [ { "indic_bt": "PSQM" }, {"s_adj": "SA" } ]` |
| **Condizionale** | **$gt** | Indica se il valore associato al filtro è più grande rispetto a quello indicato nel campo. | `{ "2012": { "$gt": 110 } }` |
| **Condizionale** | **$gte** | Indica se il valore associato al filtro è uguale o più grande rispetto a quello indicato nel campo. | `{ "1998": { "$gte": 52 } }` |
| **Condizionale** | **$lt** | Indica se il valore associato al filtro è più piccolo rispetto a quello indicato nel campo. | `{ "2016": { "$lt": 94 } }` |
| **Condizionale** | **$lte** | Indica se il valore associato al filtro è uguale o più piccolo rispetto a quello indicato nel campo. | `{ "1997": { "$lte": 120 } }` |
| **Condizionale** | **$bt** | Specificati due valori numerici, indica se il valore associato al campo è compreso tra questi due valori. | `{ "2018": { "$bt": [ 10, 60 ] } }` |

-----

## Esempi di richieste GET/POST
Tramite Advanced REST Client

[localhost:8080/full](https://github.com/GiulioMezzotero/OOPproject/blob/master/ImmagineFull.png)
[localhost:8080/getAnalytics/filtered](https://github.com/GiulioMezzotero/OOPproject/blob/master/ImmagineDatiFiltrati.PNG)

-----

## Metodi di analisi implementati
I metodi di analisi implementati sono i seguenti:

####  Numerici
 - MEDIA;
 - MINIMO;
 - MASSIMO;
 - DEVIAZIONE STANDARD;
 - SOMMA;
 - CONTEGGIO.
 
 #### Su stringhe
 - CONTEGGIO ELEMENTI UNICI.
 
 -----
 
 ## UML
 
