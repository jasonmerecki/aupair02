import org.junit.Test;

import com.jkmcllc.aupair01.server.RequestServer;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.After;
import org.junit.Before;

/*
 *
 * @author merecki, @date 8/22/16 9:54 PM
 */
public class ServerTest1 {
    private static RequestServer requestServer = RequestServer.getInstance();
    private static HttpClient httpClient = new HttpClient();
    
    @Before
    public void setUp() throws Exception {
        assertNotNull(requestServer);
        requestServer.startServer();
        httpClient.start();
    }
    
    @Test 
    public void basicRequest() throws InterruptedException, TimeoutException, ExecutionException {
        Request request = httpClient.newRequest("http://localhost:8080");
        request.method(HttpMethod.POST);
        // request.file(Paths.get("file_to_upload.txt"), "text/plain")
        ContentResponse response = request.send();
        System.out.println(response.getContent());
    }
    
    @After
    public void tearDown() {
        requestServer.stopServer();
    }
}
