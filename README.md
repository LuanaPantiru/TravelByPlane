# TravelByPlane

## Utilitate
Aeasta aplicatie are ca scop gestionarea activitatiolor care pot fi facute de catre un posibil administrator sau posibil client cand vine vorba de calatoritul cu avionul. Se pot realiza actiuni legate de aeroporturi, zborurile dintre doua aeroporturi, avioanele care fac posibila realizarea unui zbor, angajatii care se afla la bordul avionului pe parcursul unui zbor si clientii care pot sa cumbere bilete de avion pentru un anumit zbor sau doar sa poata afla informatiile dorite pentru un zbor.

## Reguli de business
* Administrator:
1. Poate sa faca urmatoarele operatii pentru un aeroport: adauge un aeroport nou; editeze; stearga un aeroport in cazul in care nu are niciun zbor in legatura si sa vizualizeze zborurile care pleaca dintr-un anumirt aeroport
2. Poate sa faca urmatoarele operatii legate de un angajat: adauge un angajat nou; editeze; elimine un angajat daca nu este asignat niciunui zbor; sa vizualizeze toti angajatii si sa vada toate zborurile la care e asignat un angajat
3. Poate sa faca urmatoarele operatii la nivelul unui zbor: sa aduage un zbor nou doar in cazut in care aeroporturile din care pleaca, respectiv ajunge exista; elimina un zbor in cazur in care nu are pasageri(adica nu s-au cumparat bilete); editeze si sa vizualizeze toate zborurile
4. Poate sa asigneze un avion disponibil la un zbor
5. Poate sa asigneze un angajat care e disponibil la un zbor
6. Poate sa vizualizeze biletete cumparate pentru un anumit zbor
7. Poate sa vizualizeze toate biletele cumparate in ordine alfabetica
8. Poate sa faca urmatoarele operatii pentru un avion: sa adauge o canfiguratie nou pentru un model de avion din cadrul unei companii; editeze; sa eleimine avionul in cazul in care nu realizeaza niciun zbor si sa vizualizeaza toate avioanele
9. Poate sa vada toti clientii care si-au facut cont sau care nu au cont dar au macar un bilet de avion

* Client:
1. Poate sa isi faca cont pentru a putea afla informatiile dorite sau sa isi cumpere bilet
2. Poate sa isi modifice contul sau sa il stearga daca nu are bilete cumparate
3. Poate sa caute zborurile pentru o anumita data de plecare, incluzand orasul de plecare si orasul in care sa ajunga(se afiseaza zborurile care au un avion asignat si un pilot si un insotitor de zbor senior, de asemenea se afiseaza si cazurile de escala)
4. Poate sa cumpere unul sau mai multe bilete pentru un zbor, chiar si pentru alte persoane(acele persoane se inregistrea a fi clienti fara cont)
5. Poate sa anuleze propriile bilete

## Functionalitatile principale
1. Concepte crud pentru aeroporturi, avioane, zboruri, angajazi, clienti
2. Afisarea zborurilor pentru un oras de plecare si un oras destinatie pentru o data anume
3. Cumpararea biletelor de avion pentru un zbor
4. Vizualizarea biletelor pentru un anumit zbor
5. Vizualizarea zborurilor care pleaca dintr-un anumit aeroport
