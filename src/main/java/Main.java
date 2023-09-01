import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Controller.SocialMediaController;
import Model.Message;
import Util.ConnectionUtil;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {

    static HttpClient webClient = HttpClient.newHttpClient();
    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException, InterruptedException {
        SocialMediaController controller = new SocialMediaController();
        Javalin app = controller.startAPI();
        app.start(8080);

        //getAllMessagesMessagesAvailable();
        getMessageGivenMessageIdMessageFound();

        app.stop();
    }

    public static void getMessageGivenMessageIdMessageFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages/1"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        System.out.println("/////////////////\n/*STATUS RESULT*/\n/////////////////");
        System.out.println("EXPECTED: 200");
        System.out.println("REAL: "+status);

        Message expectedResult = new Message(1, 1, "test message 1", 1669947792);
        Message actualResult = objectMapper.readValue(response.body().toString(), Message.class);
        //Assert.assertEquals(expectedResult, actualResult);
        System.out.println("//////////////////\n/*PRINTED RESULT*/\n//////////////////");
        System.out.println("EXPECTED: "+expectedResult);
        System.out.println("REAL: "+actualResult);

        System.out.println("//////////////\n/*END PRINTS*/\n//////////////");
    }

    public static void getAllMessagesMessagesAvailable() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/messages"))
                .build();
        HttpResponse response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        //Assert.assertEquals(200, status);
        System.out.println("/////////////////\n/*STATUS RESULT*/\n/////////////////");
        System.out.println("EXPECTED: 200");
        System.out.println("REAL: "+status);

        List<Message> expectedResult = new ArrayList<>();
        expectedResult.add(new Message(1, 1, "test message 1", 1669947792));
        List<Message> actualResult = objectMapper.readValue(response.body().toString(), new TypeReference<List<Message>>(){});
        //Assert.assertEquals(expectedResult, actualResult);
        System.out.println("//////////////////\n/*PRINTED RESULT*/\n//////////////////");
        System.out.println("EXPECTED: "+expectedResult);
        System.out.println("REAL: "+actualResult);

        System.out.println("//////////////\n/*END PRINTS*/\n//////////////");
    }
}
