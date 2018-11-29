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

package com.cjm.moni.service;

import com.cjm.moni.entity.parameters.BusinessSearchParameters;
import com.cjm.moni.entity.response.BusinessSearchResponse;

public interface YelpApi {
    // Yelp API
    String YELP_HOST = "https://api.yelp.com";
    String YELP_API_VERSION = "/v3";
    String BUSINESSES_SEARCH_ENDPOINT = "/businesses/search";
    String BUSINESSES_MATCHES_ENDPOINT = "/businesses/matches";
    String BUSINESSES_SEARCH_PHONE_ENDPOINT = "/businesses/search/phone";

    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer";

    BusinessSearchResponse searchBusiness(BusinessSearchParameters parameters) throws Exception;

//    BusinessMatchesResponse matchesBusiness(BusinessMatchesParameters parameters) throws Exception;
//
//    BusinessSearchPhoneResponse searchBusiness(BusinessSearchPhoneParameters parameters) throws Exception;
}
