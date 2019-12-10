# OOPproject
###### di Giulio Mezzotero e Giovanni Alessandro Clini

Esame di "Programmazione ad Oggetti", insegnamento presente all'interno del corso di Laurea triennale in Ingegneria Informatica e dell’Automazione presso l'Università Politecnica delle Marche, a.a. 2018/2019.
Il progetto presente nella repository consiste in un'applicazione Java basata sul framework Spring che restituisce tramite API REST GET o POST dati e analisi in formato JSON a partire da un dataset assegnatoci in formato TSV. Il progetto può essere compilato attraverso framework (ad es. Maven) che gestiscono l'importazione delle librerie Spring.

## Funzionamento all'avvio
L'applicazione, una volta lanciata, esegue il download di un dataset in formato TSV contenuto in un JSON fornito tramite un [URL](http://data.europa.eu/euodp/data/api/3/action/package_show?id=GeGKzwDc03b3j0olhD5DQ). Il download del dataset viene salvato nella cartella del progetto con il nome di dataset.tsv. Successivamente viene effettuato il parsing del file TSV in modo da poter creare le istanze del modello. Inoltre il programma avvia un web-server in locale sulla porta 8080 che riceve richieste dall'utente.

## Interpretazione modello e dati
I dati sono tratti dal sito dell'Unione Europea. Il dataset TSV contiene:
| Campo | Descrizione |
| - | - |
| **indic_bt** | Indicatore del Business Trend. |
| **nace_r2** | Classificazione delle attività economiche.  |
| **s_adj** | Sigla che informa se il dato è "destagionalizzato" o meno (Seasonal Adjustment). |
| **unit** | Unità di misura. |
| **geo_time (country)** | Entità geopolitica di interesse. |
| **dati annuali** | Array di indici suddivisi per anno dal 1992 al 2018. |

## Packages e classi
Il progetto presenta un package principale  `univpm.op.project` che contiene la classe main `Application` che avvia il server Spring. Le altre classi sono divise in 5 package:

-   `data`: contiene le classi  `Data` e `StringName`. La prima gestisce i dati in formato TSV, la seconda è astratta e contiene le stringhe con l'indirizzo dei dati e i nomi dei due file necessari allo sviluppo del progetto;
-   `entity`: contiene la classe  `Entity`, che struttura il nuovo tipo di dati necessario per l'immagazzinamento delle informazioni derivanti dal dataset;
-   `controller`: contiene la classe  `Controller`che gestisce le routes dell'applicazione;
-   `exception`: contiene la classe `InvalidFilterException` che gestisce le eccezioni per i filtri;
-   `utils`: contiene le classi `Utils` e `NumericAnalysis`. La prima, astratta, contiene i metodi utili allo sviluppo dell'applicazione, la seconda estrae le analisi dai dati.

Visionare il JavaDoc per informazioni più specifiche su classi e relativi metodi.

