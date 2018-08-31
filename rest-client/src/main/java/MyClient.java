import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MyClient
{
    public static void main(String args[]) throws Exception
    {
        MyClient client = new MyClient();
        if (args.length > 0)
        {
            if ("get".equals(args[0])) 
            {
                if (args.length == 2)
                {
                    client.getPatient(Long.valueOf(args[1]));
                }
                else
                {
                    System.out.println("Usage: get needs two args");
                }
            }
            else if ("post".equals(args[0]) && args.length == 3)
            {
                client.addPatient(args);
            }
        }
    }

    private void getPatient(Long id)
    {
        Client client = ClientBuilder.newClient();

        String url = String.format("http://localhost:8080/firstcup/rest/hospital/patient/%d",id);
        
        String response = client.target(url)
                                .request()
                                .get(String.class);

        System.out.println("Response is ..." );
        System.out.println(response);
        System.out.println("________________");
    }

    private void addPatient(String args[])
    {
       String xml = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<patient><id>1</id><forename>%s</forename><surname>%s</surname>" +
                "<male>true</male></patient>", args[1],args[2]);

        Client client = ClientBuilder.newClient();

        String url = "http://localhost:8080/firstcup/rest/hospital/patient/";
        
        String response = client.target(url)
                                .request()
                                .post(Entity.entity(xml, MediaType.APPLICATION_XML), String.class);

        System.out.println("Response is ..." );
        System.out.println(response);
        System.out.println("________________");
    }
}
