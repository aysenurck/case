package requests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import org.apache.xpath.operations.String;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class AllServices extends JUnit4CitrusTestRunner {

    @Autowired
    HttpClient generalClient;

    String author;
    String title;
    int id;
    String payloadId =null;


    // Senaryo 1 -- Hiç eleman yok(başlangıç)
    @Test
    @CitrusTest
    public void getAllBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .get("api/book") //endpoint
                .contentType("application/json"));
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(200)
                        .status(HttpStatus.NO_CONTENT));
        echo("List is empty");
        traceVariables();
    }

  // Senaryo 2 -- listeye eleman ekleme requirements(author) kontrol

    @Test
    @CitrusTest
    public void checkAuthorRequirementsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/") //endpoint
                .payload("title=${title}"));
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("Required author");
    }

    // Senaryo 3 -- listeye eleman ekleme requirements(title) kontrol
    @Test
    @CitrusTest
    public void checkTitleRequirementsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .put("/api/books/") //endpoint
                .payload("author=${author}"));
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("Required title");
    }

    // Senaryo 4 -- listeye eleman ekleme requirements(title, author) kontrol
    @Test
    @CitrusTest
    public void checkRequirementsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .put("/api/books/<book_id>/") //endpoint
                .payload("title=${title}&author=${author}"));
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("Required title and author");
    }


    // Senaryo 5 -- listeye eleman ekleme requirements value kontrol

    @Test
    @CitrusTest
    public void checkDescriptonRequirementsFieldsTest() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/") //endpoint
                .payload("title=${}&author=${}"));
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(400));
        traceVariables();
        echo("'Title' and 'Author' cannot be empty");
    }


    // Senaryo 6 -- listeye eleman ekleme (yeni kitap)
    @Test
    @CitrusTest
    public void addNewBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)
                .send()
                .put("/api/books/") //endpoint
                .payload("title=${title}&author=${author}"));
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(200)
                        .extractFromPayload("$.Id", payloadId)
                        .extractFromPayload("$.title", title)// check new books info
                        .extractFromPayload("author", author));// check new books info

        traceVariables();

    }


    // Senaryo 7 -- seçili kitabı getirme
    @Test
    @CitrusTest
    public void getSelectedBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .get("/api/books/"+payloadId)); // üstteki servisten alınan id ile istek gönderiliyor
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(200)
                        .status(HttpStatus.FOUND)
                        .validate("${title}", "title" ));
        echo("found");
        traceVariables();
    }


    // Senaryo 8 -- seçili kitabı getirme -- listede bulunmayan id
    @Test
    @CitrusTest
    public void getSelectedNotExistBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .get("/api/books/id")); //endpoint
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response()
                        .statusCode(404)
                        .status(HttpStatus.NOT_FOUND));
        echo("book is not exist");
        traceVariables();
    }



    // Senaryo 9 -- var olan bir kitabı ekleme
    @Test
    @CitrusTest
    public void addDublicateBook() {
        http(httpActionBuilder -> httpActionBuilder.client(generalClient)// istek atılacak base url
                .send()
                .put("/api/books/") //endpoint
                .payload("{ \"title\": \"${title}\", \"author\": \"${author})")); // farklı bir json formatı
        http(httpActionBuilder ->
                httpActionBuilder.client(generalClient)
                        .receive()
                        .response().reasonPhrase("duplicate book")// tekrar bak
                        .statusCode(400));

        traceVariables();
    }

}
