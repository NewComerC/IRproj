//  The MIT License
//
//  Copyright © 2018 Saurabh Rane
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.

package com.cjm.moni.service.impl;


import com.cjm.moni.entity.parameters.BusinessSearchParameters;
import com.cjm.moni.entity.response.BusinessSearchResponse;
import com.cjm.moni.service.YelpApi;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YelpService implements YelpApi {
    private String token="VoQovMgPNeHtqtyidH7oNVo1pakvkJWIi_soeA4uUfTZLpH9yKGp77VfQTV_kT5tejYxsKcA6JhLgg6ca3rqQxuZlpO27qHthOW1l8tIxv-GoMKyNip2OyeqxUf8W3Yx";

//    public YelpService(String token) {
//        this.token = token;
//    }


    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public BusinessSearchResponse searchBusiness(BusinessSearchParameters parameters) throws Exception {
        String uri = getBusinessesSearchEndpointUriWithParameters(parameters);
        ResponseEntity<BusinessSearchResponse> response = restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(),
                BusinessSearchResponse.class);
        return response.getBody();
    }

//    @Override
//    public BusinessMatchesResponse matchesBusiness(BusinessMatchesParameters parameters) throws Exception {
//        String uri = getBusinessesMatchesEndpointUriWithParameters(parameters);
//        ResponseEntity<BusinessMatchesResponse> response = restTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(),
//                BusinessMatchesResponse.class);
//        return response.getBody();
//    }
//
//    @Override
//    public BusinessSearchPhoneResponse searchBusiness(BusinessSearchPhoneParameters parameters) throws Exception {
//        String uri = getBusinessesSearchPhoneEndpointUriWithParameters(parameters);
//        ResponseEntity<BusinessSearchPhoneResponse> response = restTemplate.exchange(uri, HttpMethod.GET,
//                getHttpEntity(), BusinessSearchPhoneResponse.class);
//        return response.getBody();
//    }

    private HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, getBearerToken());
        return new HttpEntity<>(headers);
    }

//    private String getBusinessesSearchPhoneEndpointUriWithParameters(BusinessSearchPhoneParameters parameters)
//            throws Exception {
//        return getBusinessesSearchPhoneEndpointUri() + parameters.getParameters();
//    }
//
//    private String getBusinessesMatchesEndpointUriWithParameters(BusinessMatchesParameters parameters)
//            throws Exception {
//        return getBusinessesMatchesEndpointUri() + parameters.getParameters();
//    }

    private String getBusinessesSearchEndpointUriWithParameters(BusinessSearchParameters parameters) throws Exception {
        return getBusinessesSearchEndpointUri() + parameters.getParameters();
    }

    private String getBusinessesSearchEndpointUri() {
        return getBaseUri() + BUSINESSES_SEARCH_ENDPOINT;
    }

//    private String getBusinessesMatchesEndpointUri() {
//        return getBaseUri() + BUSINESSES_MATCHES_ENDPOINT;
//    }
//
//    private String getBusinessesSearchPhoneEndpointUri() {
//        return getBaseUri() + BUSINESSES_SEARCH_PHONE_ENDPOINT;
//    }

    private String getBaseUri() {
        return YELP_HOST + YELP_API_VERSION;
    }

    private String getBearerToken() {
        return BEARER + " " + token;
    }
}
