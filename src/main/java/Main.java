import java.io.IOException;
import java.net.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import Controller.SocialMediaController;
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

        app.stop();
    }
}
