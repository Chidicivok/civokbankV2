package com.chidicivok.adminservice.clients;


import com.chidicivok.adminservice.clients.dto.responses.EmailVerifierResponse;
import com.chidicivok.adminservice.exceptions.ExternalEmailApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.ObjectMapper;

// External API - Abstract Email Verification API
@Service
public class EmailVerifierService {

    private final RestClient restClient;

    public EmailVerifierService(@Value("${email.verifier.base}") String baseUrl, ObjectMapper objectMapper) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Value("${email.verifier.api.key}")
    String apiKey;

    public EmailVerifierResponse verifyEmail(String email) {


        try {

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/")
                            .queryParam("api_key", apiKey)
                            .queryParam("email", email)
                            .build()
                    ).retrieve()
                    .body(EmailVerifierResponse.class);

        } catch (RestClientException e) {
            throw new ExternalEmailApiException("Could not verify the email");
        }


    }

    /////////////






    public boolean isEmailValid(String email) {

        EmailVerifierResponse response = verifyEmail(email);


        boolean isStatusDeliverable = "deliverable".equalsIgnoreCase(response.emailDeliverability().status());
        boolean isStatusDetailValid = "valid_email".equalsIgnoreCase(response.emailDeliverability().statusDetail());

        return isStatusDeliverable
                && isStatusDetailValid
                && response.emailDeliverability().isFormatValid()
                && response.emailDeliverability().isSmtpValid()
                && !response.emailQuality().isDisposable();

    }



}




//    public boolean isEmailValid(String email) {
//
//        EmailVerifierResponse response = verifyEmail(email);
//
//        if (response == null || response.emailDeliverability() == null) {
//            throw new ExternalEmailApiException(
//                    "Email Verification Service returned an empty or invalid response"
//            );
//        }
//
//        EmailVerifierResponse.EmailDeliverability result =
//                response.emailDeliverability();
//
//        return "deliverable".equalsIgnoreCase(result.status())
//                && "valid_email".equalsIgnoreCase(result.statusDetail())
//                && Boolean.TRUE.equals(result.isSmtpValid());
//    }