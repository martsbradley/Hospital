import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class MyClient
{
    public static void main(String args[]) throws Exception
    {
        Client client = ClientBuilder.newClient();
        
        String response = client.target("http://localhost:8080/firstcup/rest/hospital/1")
                                .request()
                                .get(String.class);

        System.out.println("Response is ..." );
        System.out.println(response);
        System.out.println("________________");
    }
}
