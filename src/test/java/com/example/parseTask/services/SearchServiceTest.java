package com.example.parseTask.services;

import com.example.parseTask.model.SearchResult;
import com.example.parseTask.model.SearchResultItem;
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

import java.util.List;
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
                "            \"text\": \"Chcesz wziąć udział w konkursie, ale nie jesteś pewien jak opłacić poprawnie rachunek online?  Poniżej znajdziesz szczegółowe informacje, co należy zrobić, żeby spełnić warunki, aby wziąć udział w konkursie „Opłaca się opłacać kartą Visa”. Cały proces płatności zajmie Ci najwyżej kilka minut, faktura zostanie ekspresowo opłacona, a Tobie pozostanie tylko wypełnić formularz zgłoszeniowy i oczekiwać na ogłoszenie zwycięzców. Jeżeli masz inne pytania związane z fakturami - poszukaj odpowiedzi w opracowanej przez nas bazie wiedzy.\\n\",\n" +
                "            \"labels\": null,\n" +
                "            \"link\": \"/konkurs-visa-oplaca-sie/faktury\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"Tu znajdziesz wszystkie niezbędne informacje związane z fakturami, które możesz opłacić aby wziąć udział w konkursie.\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Konkurs - Opłaca się opłacać kartą VISA\\\",\\\"url\\\":\\\"\\/konkurs-visa-oplaca-sie\\\"},{\\\"label\\\":\\\"Faktury\\\",\\\"url\\\":\\\"\\/konkurs-visa-oplaca-sie\\/faktury\\\"}]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 99\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 246,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Bezpieczeństwo - o czym pamiętać płacąc online?\",\n" +
                "            \"text\": \"\\nPłatności online to nie tylko zakupy przez internet. To również płatności za rachunki, doładowania telefonów, zakup dostępu do usług multimedialnych i wiele innych. Ty również pewnie większość należności opłacasz online, zgadza się? Jako dostawca bramki płatniczej dbamy o to, żeby nasze rozwiązania spełniały wszystkie normy bezpieczeństwa. Jest jednak kilka rzeczy, o których Ty, jako osoba zlecająca płatność online, również musisz pamiętać.\\n Oto one.\\n\\n\\nUważaj na fałszywe faktury\\n\\nCo jakiś czas w sieci pojawiają się ostrzeżenia przed fałszywymi fakturami, które trafiają na skrzynki mailowe klientów sieci telefonicznych, dostawców energii i innych mediów. Wiadomości są zbliżone wyglądem i zawartością do tych, które wysyłane są przez usługodawców, dlatego tak ważne jest, żeby dokładnie je sprawdzić zanim zlecimy płatność.\\n\\nNa co zwrócić uwagę?\\n\\nPrzede wszystkim na adres e-mail nadawcy - oszuści często posługują się adresami, które od razu sygnalizują, że coś jest nie tak, np. nbmgtllex@platnosci-play.cz. Jeśli zobaczysz podobny adres, niezwłocznie oznacz wiadomość jako SPAM. Jeżeli nie będziesz mieć pewności - sprawdź z jakiego adresu dotychczas przychodziły do Ciebie wiadomości od usługodawcy, pod którego chce się podszyć oszust. Np. firma Play zawsze wysyła faktury z maila: awizo@mojefinanseplay.pl.\\n\\nWarto również zalogować się do eBOK-u danego usługodawcy i sprawdzić czy rzeczywiście znajduje się w nim nowa faktura do opłacenia.\\n\\nJeżeli klikniesz \\\"Zapłać\\\" bez upewnienia się, że faktura rzeczywiście pochodzi od firmy, z usług której korzystasz, bardzo możliwe, że oszust podejmie próbę zainfekowania Twojego urządzenia. Dlatego pamiętaj również o tym, żeby chronić komputer, laptop czy smartfon odpowiednim antywirusem.\\n\\n\\n\\nUważaj na maile z prośbą o zalogowanie się \\n\\nTo kolejna popularna forma phishingu, czyli próby wyłudzenia poufnych danych. Oszuści podszywają się najczęściej pod banki i pod błahym pretekstem zachęcają do zalogowania się do bankowości internetowej. W takich sytuacjach warto pamiętać o tym, że banki zazwyczaj nie wysyłają takich próśb, więc sam fakt otrzymania takiej wiadomości powinien Cię zaalarmować.\\n\\nW tym wypadku również sprawdź adres e-mail, z jakiego przyszła wiadomość oraz URL strony, od której próbuje Cię odesłać. Jeżeli wiadomość rzeczywiście pochodzi od banku, adres e-mail powinien być jego w domenie, np. jakisadres@ing.pl, podobnie jak sam URL.\\n\\nJeżeli trafi do Ciebie podejrzana wiadomość - zgłoś to do swojego banku, żeby ten mógł ostrzec innych klientów o podejmowanych próbach oszustwa.\\n\\n\\n\\nStrzeż się wujków z zagranicy lub szejków, którzy chcą się podzielić majątkiem\\n\\nSztuczka stara jak płatności online. Jeżeli do Twojej skrzynki trafi wiadomość w języku angielskim (lub przetłumaczona z użyciem translatora na język polski), mówiąca o tym, że trafią do Ciebie miliony dolarów, pod warunkiem że podasz swój numer konta i kilka innych wrażliwych danych - od razu oznacz ją jako SPAM.\\n\\nTakie cudowne zrządzenia losu niestety się nie zdarzają. To zwykła próba wyłudzenia danych.\\n\\n\\n\\nNie robiłeś zakupów online? To za nie nie płać\\n\\nKolejna popularna próba oszustwa polega na wysyłce wiadomości, które imitują potwierdzenie złożenia zamówienia w sklepie internetowym wraz z prośbą o uregulowanie fikcyjnej należności.\\n\\nWiemy, że zdarzają się sytuacje, gdy robimy zakupy w pośpiechu lub nie do końca świadomie, ale nawet wtedy - zawsze upewniaj się, że płacisz za coś, co rzeczywiście zostało przez Ciebie zamówione. Jeżeli cokolwiek budzi Twoje wątpliwości: adres e-mail nadawcy, nazwa sklepu internetowego, kwota zamówienia - spróbuj skontaktować się danym sklepem w inny sposób i potwierdzić, że to od niego pochodzi otrzymana wiadomość. Pod żadnym pozorem nie pobieraj plików dołączonych do podejrzanej wiadomości i nie loguj się do bankowości internetowej za pośrednictwem otrzymanego linka.\\n\\nTo tylko niektóre z popularnych form wymuszenia danych lub zainfekowania Twojego urządzenia złośliwym oprogramowaniem. Często zdarza się, że oszuści podszywający się pod pracowników banku, kontaktują się drogą telefoniczną i proszą o podanie kluczowych danych, w tym np. loginu i hasła do bankowości. Prawdziwy konsultant nigdy nie poprosi Cię o te dane! Dlatego jeżeli usłyszysz w słuchawce głos, który Cię o to prosi - możesz mieć pewność, że to oszust. Niezwłocznie zakończ rozmowę, zgłoś sprawę na policję i dodaj numer telefonu do listy blokowanych połączeń.\\n\\n\\n\\n\\n\\n\",\n" +
                "            \"labels\": \"\",\n" +
                "            \"link\": \"/bezpieczenstwo/o-czym-pamietac-placac-online\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"\\\\nPłatności online to nie tylko zakupy przez internet. To również płatności za rachunki, doładowania telefonów, zakup dostępu do usług multimedialnych i wiele innych. Ty również pewnie większość należności opłacasz online, zgadza się? Jako dostawca bramki płatniczej dbamy o to, ż\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Bezpieczeństwo\\\",\\\"url\\\":\\\"\\/bezpieczenstwo\\\"},{\\\"label\\\":\\\"Bezpieczeństwo - o czym pamiętać płacąc online?\\\",\\\"url\\\":\\\"\\/bezpieczenstwo\\/o-czym-pamietac-placac-online\\\"}]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 99\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 240,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Jak otrzymać fakturę za prowizję pobieraną podczas płatności online?\",\n" +
                "            \"text\": \"\\n    Proces jest bardzo prosty i całość zajmie Ci zaledwie kilka minut.\\n\\nWystarczy, że skorzystasz z poniższego formularza i wypełnisz wszystkie pola, podając dane niezbędne do wystawienia faktury, czyli:\\n\\nnazwę firmy lub imię i nazwisko \\nNIP\\nadres\\nID transakcji\\n\\n\\nNie zapomnij o podaniu adresu e-mail, na który mamy wysłać fakturę. Do formularza dołącz potwierdzenie przelewu (dokument do pobrania z Twojej bankowości internetowej).\\n\\n\\n\\n\\n    \\n\\n\\n\\n    Faktura za prowizję od płatności online trafi do Twojej skrzynki pocztowej maksymalnie w ciągu kilku dni roboczych od przesłania przez Ciebie prośby. Wysokość prowizji zależy od usługodawcy - najczęściej wynosi od 1 do 5 PLN.\\n \",\n" +
                "            \"labels\": \"\",\n" +
                "            \"link\": \"/instrukcje/jak-otrzymac-fakture-za-pobrana-prowizje-za-platnosc-online\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"Zajrzyj do naszej instrukcji i sprawdź, co zrobić, żeby otrzymać fakturę za prowizję pobieraną podczas płatności online.\\\\n\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Instrukcje\\\",\\\"url\\\":\\\"\\/instrukcje\\\"},{\\\"label\\\":\\\"Jak otrzymać fakturę za prowizję pobieraną podczas płatności online?\\\",\\\"url\\\":\\\"\\/instrukcje\\/jak-otrzymac-fakture-za-pobrana-prowizje-za-platnosc-online\\\"}],\\\"detail_1\\\":\\\"2020-03-25T09:43:48.000Z\\\",\\\"detail_2\\\":null,\\\"detail_3\\\":null,\\\"detail_4\\\":null,\\\"detail_5\\\":null,\\\"tags\\\":[]}\",\n" +
                "            \"enabled\": 1,\n" +
                "            \"position\": 97\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 239,\n" +
                "            \"type\": \"page\",\n" +
                "            \"title\": \"Jak otrzymać fakturę za doładowanie telefonu?\",\n" +
                "            \"text\": \"Wypełnij ten formularz, podając dane niezbędne do wystawienia faktury, czyli numer doładowanego telefonu, datę doładowania, imię, nazwisko oraz adres, jeżeli chcesz otrzymać fakturę na osobę fizyczną. Jeśli chcesz, żeby faktura była wystawiona na firmę – dodaj również NIP (jeśli firma jest zagraniczna – tutaj znajdziesz instrukcję). \\n\\nNie zapomnij o podaniu adresu e-mail – dokument trafi do Twojej skrzynki pocztowej maksymalnie w ciągu 7 dni roboczych od zgłoszenia. Od momentu rejestracji numeru kolejne faktury będą wysyłane automatycznie na wskazane dane. Na dokumencie nie będzie widoczny numer doładowanego telefonu, będzie natomiast identyfikator transakcji.\\n\\nJeżeli chcesz otrzymać fakturę za doładowanie, które zostało zrealizowane już jakiś czas temu – musisz wiedzieć kilku rzeczach, które opisaliśmy tutaj.\\n\\nWskazówka: Jeżeli formularz nie przyjmuje wprowadzonych przez Ciebie danych – upewnij się, czy doładowanie zostało już zrealizowane, a jeśli tak, to czy na pewno za pośrednictwem Blue Media. Jeśli zleciłeś doładowania kilku numerów – każdy z nich musi być wprowadzony do formularza osobno, możesz to zrobić za pomocą tego formularza.\\nWażne: jeżeli doładowanie nie zostało wykonane dla konkretnego numeru telefonu, wówczas nie można go dodać do bazy, na podstawie której są wystawiane faktury VAT.\\n\\nJeśli szukasz informacji jak zmienić błędne dane na fakturze – tutaj znajdziesz potrzebne informacje. \\n \",\n" +
                "            \"labels\": \"\",\n" +
                "            \"link\": \"/instrukcje/jak-otrzymac-fakture-za-doladowania\",\n" +
                "            \"preview\":\"{\\\"text\\\":\\\"Zajrzyj do naszej instrukcji i sprawdź, co zrobić, żeby otrzymać fakturę za doładowanie telefonu\\\",\\\"breadcrumbs\\\":[{\\\"label\\\":\\\"Instrukcje\\\",\\\"url\\\":\\\"\\/instrukcje\\\"},{\\\"label\\\":\\\"Jak otrzymać fakturę za doładowanie telefonu?\\\",\\\"url\\\":\\\"\\/instrukcje\\/jak-otrzymac-fakture-za-doladowania\\\"}],\\\"detail_1\\\":\\\"2020-03-25T10:09:13.000Z\\\",\\\"detail_2\\\":null,\\\"detail_3\\\":null,\\\"detail_4\\\":null,\\\"detail_5\\\":null,\\\"tags\\\":[]}\",\n" +
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