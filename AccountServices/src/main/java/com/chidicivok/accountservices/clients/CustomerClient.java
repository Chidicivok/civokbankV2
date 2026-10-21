package com.chidicivok.accountservices.clients;


import com.chidicivok.accountservices.clients.dto.CustomerResponse;
import com.chidicivok.accountservices.exception.CustomerClientServiceFailure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(@Value("${customer.service.url}") String customerServiceUrl ) {
        this.restClient = RestClient.create(customerServiceUrl);
    }

    public CustomerResponse getCustomerDetails (Long customerId) {

     try {
         return restClient.get()
                 .uri("/find-customer/" + customerId)
                 .retrieve()
                 .body(CustomerResponse.class);



     } catch (RestClientException e) {
         throw new CustomerClientServiceFailure("Could not connect with Customer service");
     }


    }




}
