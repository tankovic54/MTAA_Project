# Dokumentácia
V rámci nášho projektu sme vytvorili Android aplikáciu v prostredí Android Studio a v jazyku Kotlin. Ako back-end slúži Spring framework bežiaci na localhoste napísaný v jazyku Java v prostredí IntelliJ IDEA. 
## Účel aplikácie
Účelom aplikácie s názvom Notes je, ako z názvu vyplýva, vytváranie poznámok. Užívateľ má možnosť zaregistrovať sa a následne prihlásiť, pričom na domovskej stránke si môže prezerať a upravovať svoje poznámky, prípadne vytvoriť novú stlačením príslušného tlačidla a filtrovať medzi nimi v rámci filtra Favourite Notes. 

Pri registrácií zadáva používateľ prihlasovacie údaje ako email a heslo, ako aj užívateľské meno či profilový obrázok. Ak si užívateľ nezvolí žiaden obrázok, tak sa mu automaticky priradí predvolený obrázok. Samozrejme prebieha aj validácia zadaných údajov, pričom sa kontrolujú na back-ende, ako aj v rámci aplikácie tak, aby sa vedela presne vypísať chyba. Kontroluje sa najmä to, či bol vyplnení e-mail a či je v správnom formáte. Taktiež aj užívateľské meno či bolo zvolené. Pri hesle sa kontroluje, či bolo vyplnené, či má minimálne 6 znakov a či sa zhodujú heslá v poliach Password a Confirm Password. 

Akonáhle sa užívateľ úspešne zaregistruje, tak sa dostane na obrazovku login. Tu zadáva prihlasovacie údaje, teda e-mail a heslo. Pokiaľ zadal správne údaje, dostane sa na domovskú obrazovku s poznámkami. Pokiaľ nie, vypíše sa mu chybová hláška, že zadal zlé prihlasovacie údaje. 

Samotné poznámky majú svoj názov ako aj obdobie platnosti, do kedy sú platné, pričom sa správnosť údajov kontroluje ako na back-ende tak aj v aplikačnej rovine. Následne zadáva užívateľ samotný text poznámky. Taktiež môže zvoliť, či označí poznámku za obľúbenú. Ak je takto zvolená, zobrazí sa vo filtri Favourite notes. Okrem vytvorenia poznámky má užívateľ možnosť editovať svoje poznámky kliknutím na názov poznámky v zozname a môže ju aj zmazať. 

Okrem poznámok je na domovskej obrazovke k dispozícií aj možnosť Nastavenia, kde si užívateľ môže zmeniť svoje prihlasovacie údaje, ako aj profilový obrázok alebo zobrazované užívateľské meno. Taktiež má možnosť sa odhlásiť z aplikácie, prípadne aj vymazať všetky poznámky či účet.  Taktiež sa dá nastaviť téma aplikácie, a to medzi svetlým, tmavým alebo režimom, ktorý nasleduje systémové nastavenia.

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE1NTM3MTMzNDNdfQ==
-->