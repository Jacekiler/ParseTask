package com.example.parseTask.services;

import com.example.parseTask.model.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SearchServiceTest {

    private static final String SEARCH_PHRASE = "faktura";
    private static final String EMPTY_SEARCH_PHRASE = "";

    @InjectMocks
    private SearchService searchService;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void resultListIsEmptyWithNullParam() throws JsonProcessingException {
        Optional<SearchResult> result = searchService.search(null, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void resultListIsEmptyWithEmptyParam() throws JsonProcessingException {
        Optional<SearchResult> result = searchService.search(EMPTY_SEARCH_PHRASE, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void resultListIsOk() throws JsonProcessingException {
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponse(true));
        Optional<SearchResult> result = searchService.search(SEARCH_PHRASE, 1);
        assertEquals(4, result.get().getItems().size());
    }

    @Test
    public void resultListEmptyWithMissingItems() throws JsonProcessingException {
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(mockResponse(false));
        Optional<SearchResult> result = searchService.search(SEARCH_PHRASE, 1);
        assertTrue(result.isEmpty());
    }

    private ResponseEntity<String> mockResponse(boolean valid){
        String itemsAttribute = valid ? "items" : "wrong_name";
        String body = "{\n" +
                "    \"" + itemsAttribute +"\": [\n" +
                "        {\n" +
                "            \"id\": 1072,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Faktury\",\n" +
                "            \"text\": \"Chcesz wzi???? udzia?? w konkursie, ale nie jeste?? pewien jak op??aci?? poprawnie rachunek online?  Poni??ej znajdziesz szczeg????owe informacje, co nale??y zrobi??, ??eby spe??ni?? warunki, aby wzi???? udzia?? w konkursie ???Op??aca si?? op??aca?? kart?? Visa???. Ca??y proces p??atno??ci zajmie Ci najwy??ej kilka minut, faktura zostanie ekspresowo op??acona, a Tobie pozostanie tylko wype??ni?? formularz zg??oszeniowy i oczekiwa?? na og??oszenie zwyci??zc??w. Je??eli masz inne pytania zwi??zane z fakturami - poszukaj odpowiedzi w opracowanej przez nas bazie wiedzy.\\n\",\n" +
                "            \"labels\": null,\n" +
                "            \"link\": \"/konkurs-visa-oplaca-sie/faktury\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"Tu znajdziesz wszystkie niezb??dne informacje zwi??zane z fakturami, kt??re mo??esz op??aci?? aby wzi???? udzia?? w konkursie.\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Konkurs - Op??aca si?? op??aca?? kart?? VISA\\\",\\\"url\\\":\\\"\\/konkurs-visa-oplaca-sie\\\"},{\\\"label\\\":\\\"Faktury\\\",\\\"url\\\":\\\"\\/konkurs-visa-oplaca-sie\\/faktury\\\"}]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 99\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 246,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Bezpiecze??stwo - o czym pami??ta?? p??ac??c online?\",\n" +
                "            \"text\": \"\\nP??atno??ci online to nie tylko zakupy przez internet. To r??wnie?? p??atno??ci za rachunki, do??adowania telefon??w, zakup dost??pu do us??ug multimedialnych i wiele innych. Ty r??wnie?? pewnie wi??kszo???? nale??no??ci op??acasz online, zgadza si??? Jako dostawca bramki p??atniczej dbamy o to, ??eby nasze rozwi??zania spe??nia??y wszystkie normy bezpiecze??stwa. Jest jednak kilka rzeczy, o kt??rych Ty, jako osoba zlecaj??ca p??atno???? online, r??wnie?? musisz pami??ta??.\\n Oto one.\\n\\n\\nUwa??aj na fa??szywe faktury\\n\\nCo jaki?? czas w sieci pojawiaj?? si?? ostrze??enia przed fa??szywymi fakturami, kt??re trafiaj?? na skrzynki mailowe klient??w sieci telefonicznych, dostawc??w energii i innych medi??w. Wiadomo??ci s?? zbli??one wygl??dem i zawarto??ci?? do tych, kt??re wysy??ane s?? przez us??ugodawc??w, dlatego tak wa??ne jest, ??eby dok??adnie je sprawdzi?? zanim zlecimy p??atno????.\\n\\nNa co zwr??ci?? uwag???\\n\\nPrzede wszystkim na adres e-mail nadawcy - oszu??ci cz??sto pos??uguj?? si?? adresami, kt??re od razu sygnalizuj??, ??e co?? jest nie tak, np. nbmgtllex@platnosci-play.cz. Je??li zobaczysz podobny adres, niezw??ocznie oznacz wiadomo???? jako SPAM. Je??eli nie b??dziesz mie?? pewno??ci - sprawd?? z jakiego adresu dotychczas przychodzi??y do Ciebie wiadomo??ci od us??ugodawcy, pod kt??rego chce si?? podszy?? oszust. Np. firma Play zawsze wysy??a faktury z maila: awizo@mojefinanseplay.pl.\\n\\nWarto r??wnie?? zalogowa?? si?? do eBOK-u danego us??ugodawcy i sprawdzi?? czy rzeczywi??cie znajduje si?? w nim nowa faktura do op??acenia.\\n\\nJe??eli klikniesz \\\"Zap??a??\\\" bez upewnienia si??, ??e faktura rzeczywi??cie pochodzi od firmy, z us??ug kt??rej korzystasz, bardzo mo??liwe, ??e oszust podejmie pr??b?? zainfekowania Twojego urz??dzenia. Dlatego pami??taj r??wnie?? o tym, ??eby chroni?? komputer, laptop czy smartfon odpowiednim antywirusem.\\n\\n\\n\\nUwa??aj na maile z pro??b?? o zalogowanie si?? \\n\\nTo kolejna popularna forma phishingu, czyli pr??by wy??udzenia poufnych danych. Oszu??ci podszywaj?? si?? najcz????ciej pod banki i pod b??ahym pretekstem zach??caj?? do zalogowania si?? do bankowo??ci internetowej. W takich sytuacjach warto pami??ta?? o tym, ??e banki zazwyczaj nie wysy??aj?? takich pr????b, wi??c sam fakt otrzymania takiej wiadomo??ci powinien Ci?? zaalarmowa??.\\n\\nW tym wypadku r??wnie?? sprawd?? adres e-mail, z jakiego przysz??a wiadomo???? oraz URL strony, od kt??rej pr??buje Ci?? odes??a??. Je??eli wiadomo???? rzeczywi??cie pochodzi od banku, adres e-mail powinien by?? jego w domenie, np. jakisadres@ing.pl, podobnie jak sam URL.\\n\\nJe??eli trafi do Ciebie podejrzana wiadomo???? - zg??o?? to do swojego banku, ??eby ten m??g?? ostrzec innych klient??w o podejmowanych pr??bach oszustwa.\\n\\n\\n\\nStrze?? si?? wujk??w z zagranicy lub szejk??w, kt??rzy chc?? si?? podzieli?? maj??tkiem\\n\\nSztuczka stara jak p??atno??ci online. Je??eli do Twojej skrzynki trafi wiadomo???? w j??zyku angielskim (lub przet??umaczona z u??yciem translatora na j??zyk polski), m??wi??ca o tym, ??e trafi?? do Ciebie miliony dolar??w, pod warunkiem ??e podasz sw??j numer konta i kilka innych wra??liwych danych - od razu oznacz j?? jako SPAM.\\n\\nTakie cudowne zrz??dzenia losu niestety si?? nie zdarzaj??. To zwyk??a pr??ba wy??udzenia danych.\\n\\n\\n\\nNie robi??e?? zakup??w online? To za nie nie p??a??\\n\\nKolejna popularna pr??ba oszustwa polega na wysy??ce wiadomo??ci, kt??re imituj?? potwierdzenie z??o??enia zam??wienia w sklepie internetowym wraz z pro??b?? o uregulowanie fikcyjnej nale??no??ci.\\n\\nWiemy, ??e zdarzaj?? si?? sytuacje, gdy robimy zakupy w po??piechu lub nie do ko??ca ??wiadomie, ale nawet wtedy - zawsze upewniaj si??, ??e p??acisz za co??, co rzeczywi??cie zosta??o przez Ciebie zam??wione. Je??eli cokolwiek budzi Twoje w??tpliwo??ci: adres e-mail nadawcy, nazwa sklepu internetowego, kwota zam??wienia - spr??buj skontaktowa?? si?? danym sklepem w inny spos??b i potwierdzi??, ??e to od niego pochodzi otrzymana wiadomo????. Pod ??adnym pozorem nie pobieraj plik??w do????czonych do podejrzanej wiadomo??ci i nie loguj si?? do bankowo??ci internetowej za po??rednictwem otrzymanego linka.\\n\\nTo tylko niekt??re z popularnych form wymuszenia danych lub zainfekowania Twojego urz??dzenia z??o??liwym oprogramowaniem. Cz??sto zdarza si??, ??e oszu??ci podszywaj??cy si?? pod pracownik??w banku, kontaktuj?? si?? drog?? telefoniczn?? i prosz?? o podanie kluczowych danych, w tym np. loginu i has??a do bankowo??ci. Prawdziwy konsultant nigdy nie poprosi Ci?? o te dane! Dlatego je??eli us??yszysz w s??uchawce g??os, kt??ry Ci?? o to prosi - mo??esz mie?? pewno????, ??e to oszust. Niezw??ocznie zako??cz rozmow??, zg??o?? spraw?? na policj?? i dodaj numer telefonu do listy blokowanych po????cze??.\\n\\n\\n\\n\\n\\n\",\n" +
                "            \"labels\": \"\",\n" +
                "            \"link\": \"/bezpieczenstwo/o-czym-pamietac-placac-online\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"\\\\nP??atno??ci online to nie tylko zakupy przez internet. To r??wnie?? p??atno??ci za rachunki, do??adowania telefon??w, zakup dost??pu do us??ug multimedialnych i wiele innych. Ty r??wnie?? pewnie wi??kszo???? nale??no??ci op??acasz online, zgadza si??? Jako dostawca bramki p??atniczej dbamy o to, ??\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Bezpiecze??stwo\\\",\\\"url\\\":\\\"\\/bezpieczenstwo\\\"},{\\\"label\\\":\\\"Bezpiecze??stwo - o czym pami??ta?? p??ac??c online?\\\",\\\"url\\\":\\\"\\/bezpieczenstwo\\/o-czym-pamietac-placac-online\\\"}]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 99\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 240,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Jak otrzyma?? faktur?? za prowizj?? pobieran?? podczas p??atno??ci online?\",\n" +
                "            \"text\": \"\\n    Proces jest bardzo prosty i ca??o???? zajmie Ci zaledwie kilka minut.\\n\\nWystarczy, ??e skorzystasz z poni??szego formularza i wype??nisz wszystkie pola, podaj??c dane niezb??dne do wystawienia faktury, czyli:\\n\\nnazw?? firmy lub imi?? i nazwisko \\nNIP\\nadres\\nID transakcji\\n\\n\\nNie zapomnij o podaniu adresu e-mail, na kt??ry mamy wys??a?? faktur??. Do formularza do????cz potwierdzenie przelewu (dokument do pobrania z Twojej bankowo??ci internetowej).\\n\\n\\n\\n\\n    \\n\\n\\n\\n    Faktura za prowizj?? od p??atno??ci online trafi do Twojej skrzynki pocztowej maksymalnie w ci??gu kilku dni roboczych od przes??ania przez Ciebie pro??by. Wysoko???? prowizji zale??y od us??ugodawcy - najcz????ciej wynosi od 1 do 5 PLN.\\n \",\n" +
                "            \"labels\": \"\",\n" +
                "            \"link\": \"/instrukcje/jak-otrzymac-fakture-za-pobrana-prowizje-za-platnosc-online\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"Zajrzyj do naszej instrukcji i sprawd??, co zrobi??, ??eby otrzyma?? faktur?? za prowizj?? pobieran?? podczas p??atno??ci online.\\\\n\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Instrukcje\\\",\\\"url\\\":\\\"\\/instrukcje\\\"},{\\\"label\\\":\\\"Jak otrzyma?? faktur?? za prowizj?? pobieran?? podczas p??atno??ci online?\\\",\\\"url\\\":\\\"\\/instrukcje\\/jak-otrzymac-fakture-za-pobrana-prowizje-za-platnosc-online\\\"}],\\\"detail_1\\\":\\\"2020-03-25T09:43:48.000Z\\\",\\\"detail_2\\\":null,\\\"detail_3\\\":null,\\\"detail_4\\\":null,\\\"detail_5\\\":null,\\\"tags\\\":[]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 97\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 239,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Jak otrzyma?? faktur?? za do??adowanie telefonu?\",\n" +
                "            \"text\": \"Wype??nij ten formularz, podaj??c dane niezb??dne do wystawienia faktury, czyli numer do??adowanego telefonu, dat?? do??adowania, imi??, nazwisko oraz adres, je??eli chcesz otrzyma?? faktur?? na osob?? fizyczn??. Je??li chcesz, ??eby faktura by??a wystawiona na firm?? ??? dodaj r??wnie?? NIP (je??li firma jest zagraniczna ??? tutaj znajdziesz instrukcj??). \\n\\nNie zapomnij o podaniu adresu e-mail ??? dokument trafi do Twojej skrzynki pocztowej maksymalnie w ci??gu 7 dni roboczych od zg??oszenia. Od momentu rejestracji numeru kolejne faktury b??d?? wysy??ane automatycznie na wskazane dane. Na dokumencie nie b??dzie widoczny numer do??adowanego telefonu, b??dzie natomiast identyfikator transakcji.\\n\\nJe??eli chcesz otrzyma?? faktur?? za do??adowanie, kt??re zosta??o zrealizowane ju?? jaki?? czas temu ??? musisz wiedzie?? kilku rzeczach, kt??re opisali??my tutaj.\\n\\nWskaz??wka: Je??eli formularz nie przyjmuje wprowadzonych przez Ciebie danych ??? upewnij si??, czy do??adowanie zosta??o ju?? zrealizowane, a je??li tak, to czy na pewno za po??rednictwem Blue Media. Je??li zleci??e?? do??adowania kilku numer??w ??? ka??dy z nich musi by?? wprowadzony do formularza osobno, mo??esz to zrobi?? za pomoc?? tego formularza.\\nWa??ne: je??eli do??adowanie nie zosta??o wykonane dla konkretnego numeru telefonu, w??wczas nie mo??na go doda?? do bazy, na podstawie kt??rej s?? wystawiane faktury VAT.\\n\\nJe??li szukasz informacji jak zmieni?? b????dne dane na fakturze ??? tutaj znajdziesz potrzebne informacje. \\n \",\n" +
                "            \"labels\": \"\",\n" +
                "            \"link\": \"/instrukcje/jak-otrzymac-fakture-za-doladowania\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"Zajrzyj do naszej instrukcji i sprawd??, co zrobi??, ??eby otrzyma?? faktur?? za do??adowanie telefonu\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Instrukcje\\\",\\\"url\\\":\\\"\\/instrukcje\\\"},{\\\"label\\\":\\\"Jak otrzyma?? faktur?? za do??adowanie telefonu?\\\",\\\"url\\\":\\\"\\/instrukcje\\/jak-otrzymac-fakture-za-doladowania\\\"}],\\\"detail_1\\\":\\\"2020-03-25T10:09:13.000Z\\\",\\\"detail_2\\\":null,\\\"detail_3\\\":null,\\\"detail_4\\\":null,\\\"detail_5\\\":null,\\\"tags\\\":[]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 97\n" +
                "        }\n" +
                "    ],\n" +
                "    \"_links\": {\n" +
                "        \"self\": {\n" +
                "            \"href\": \"https://pomoc.bluemedia.pl/search-engine/search?query=faktura&page=1\"\n" +
                "        },\n" +
                "        \"first\": {\n" +
                "            \"href\": \"https://pomoc.bluemedia.pl/search-engine/search?query=faktura&page=1\"\n" +
                "        },\n" +
                "        \"last\": {\n" +
                "            \"href\": \"https://pomoc.bluemedia.pl/search-engine/search?query=faktura&page=1\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"_meta\": {\n" +
                "        \"totalCount\": 4,\n" +
                "        \"pageCount\": 1,\n" +
                "        \"currentPage\": 1,\n" +
                "        \"perPage\": 20\n" +
                "    }\n" +
                "}";
        return ResponseEntity.ok().body(body);
    }

}