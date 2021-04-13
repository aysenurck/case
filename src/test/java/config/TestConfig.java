package config;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.json.JsonSchemaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.mapping.HeaderMapper;


@Configuration
public class TestConfig {

    @Bean
    public HttpClient generalClient() {

        HttpClient citrusClient = CitrusEndpoints.http()
                .client()
                .requestUrl("http://" )
                .build();

        HeaderMapper<HttpHeaders> headersHeaderMapper = citrusClient.getEndpointConfiguration().getHeaderMapper();
        return citrusClient;
    }

    @Bean
    public JsonSchemaRepository schemaRepository() {
        JsonSchemaRepository schemaRepository = new JsonSchemaRepository();
        return schemaRepository;
    }


}
