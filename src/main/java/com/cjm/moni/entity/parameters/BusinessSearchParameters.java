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

package com.cjm.moni.entity.parameters;

import com.cjm.moni.exception.InvalidParameterException;
import com.cjm.moni.exception.NullParameterException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

//@Data
public class BusinessSearchParameters implements Parameters,Serializable {
    private String term;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private List<String> categories;
    private Locale locale;
    private Integer limit;
    private Integer offset;
    private SortBy sortBy;
    private List<Integer> price;
    private Boolean openNow;
    private Integer openAt;
//    private List<Attribute> attributes;



    public String getParameters() throws NullParameterException, InvalidParameterException {
        StringBuilder builder = new StringBuilder("?");

        if (!StringUtils.isBlank(term)) {
            builder.append("&term=").append(term);
        }

        if (StringUtils.isBlank(location) && latitude == null && longitude == null) {
            throw new NullParameterException("Both location and latitude/longitude combination cannot be null for " +
                    "/businesses/search endpoint.");
        }

        if (!StringUtils.isBlank(location)) {
            builder.append("&location=").append(location);
        } else {
            if (latitude == null) {
                throw new NullParameterException("latitude cannot be null for /businesses/search endpoint.");
            }
            if (longitude == null) {
                throw new NullParameterException("longitude cannot be null for /businesses/search endpoint.");
            }
            builder.append("&latitude=").append(latitude);
            builder.append("&longitude=").append(longitude);
        }

        if (radius != null) {
            if (radius > 40000) {
                throw new InvalidParameterException("AREA_TOO_LARGE: Please input the correct parameter value for " +
                        "radius (< 40000) in /businesses/search endpoint.");
            }
        }

        if (categories != null) {
            builder.append("&categories=").append(getParameterValues(categories));
        }

        if (locale != null) {
            builder.append("&locale=").append(locale);
        }

        if (limit != null) {
            if (limit > 50) {
                limit = 50;
            }
            builder.append("&limit=").append(limit);
        }

        if (offset != null) {
            builder.append("&offset=").append(offset);
        }

        if (sortBy != null) {
            builder.append("&sort_by=").append(sortBy.getValue());
        }

        if (price != null) {
            builder.append("&price=").append(getParameterValues(price));
        }

        if (openNow != null && openAt != null) {
            throw new InvalidParameterException("open_now and open_at cannot be used together for " +
                    "/businesses/search endpoint.");
        }
        if (openNow != null) {
            builder.append("&open_now=").append(openNow);
        }
        if (openAt != null) {
            builder.append("&open_at=").append(openAt);
        }

//        if (attributes != null) {
//            builder.append("&attributes=").append(getParameterValues(attributes));
//        }

        return builder.toString();
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public List<Integer> getPrice() {
        return price;
    }

    public void setPrice(List<Integer> price) {
        this.price = price;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public Integer getOpenAt() {
        return openAt;
    }

    public void setOpenAt(Integer openAt) {
        this.openAt = openAt;
    }

    @Override
    public String toString() {
        return "BusinessSearchParameters{" +
                "term='" + term + '\'' +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", categories=" + categories +
                ", locale=" + locale +
                ", limit=" + limit +
                ", offset=" + offset +
                ", sortBy=" + sortBy +
                ", price=" + price +
                ", openNow=" + openNow +
                ", openAt=" + openAt +
                '}';
    }


    public BusinessSearchParameters(){

    }

    public BusinessSearchParameters(String term, String location,SortBy sortBy) {
        this.term = term;
        this.location = location;
        this.sortBy=sortBy;
    }
}
