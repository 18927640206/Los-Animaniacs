package karate.gateway;

import com.intuit.karate.junit5.Karate;

class GatewayTest {
    
    @Karate.Test
    Karate testGateway() {
        return Karate.run("Gateway").relativeTo(getClass());
    }
}