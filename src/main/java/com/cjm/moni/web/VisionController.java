/*
        * Copyright 2017-2018 the original author or authors.
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        *      http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
        */

package com.cjm.moni.web;

import com.cjm.moni.entity.parameters.BusinessSearchParameters;
import com.cjm.moni.entity.parameters.SortBy;
import com.cjm.moni.entity.response.BusinessSearchResponse;
import com.cjm.moni.service.StopwordService;
import com.cjm.moni.service.YelpApi;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import java.io.IOException;
import java.util.*;


@RestController

public class VisionController {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ImageAnnotatorClient imageAnnotatorClient;

    @Autowired
    private YelpApi yelpApi;

    @Autowired
    private StopwordService stopwordService;
    /**
     * This method downloads an image from a URL and sends its contents to the Vision API for label detection.
     * @return a string with the list of labels and percentage of certainty
     * @throws Exception if the Vision API call produces an error
     */
    @PostMapping("/getResult")
    public ModelAndView uploadImage1(@RequestParam("uploadFile")MultipartFile file) throws Exception, IOException {
        // Copies the content of the image to memory.

        ModelMap map=new ModelMap();

        BatchAnnotateImagesResponse responses;

        ByteString imgBytes = ByteString.readFrom(file.getInputStream());
        Image image = Image.newBuilder().setContent(imgBytes).build();

//        Image image = Image.newBuilder().setContent(ByteString.copyFrom(imageBytes)).build();

        // Sets the type of request to label detection, to detect broad sets of categories in an image.
        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().setImage(image).addFeatures(feature).build();
        responses = this.imageAnnotatorClient.batchAnnotateImages(Collections.singletonList(request));

        // We're only expecting one response.
        if (responses.getResponsesCount() == 1) {
            AnnotateImageResponse response = responses.getResponses(0);

            ImmutableMap.Builder<String, Float> annotations = ImmutableMap.builder();

            HashSet<String> tags=new HashSet<>();
            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                String tag=annotation.getDescription();
                if(tags.contains(tag)) continue;
                else tags.add(tag);

                annotations.put(annotation.getDescription(), annotation.getScore());
            }
            map.addAttribute("annotations", annotations.build());
        }


        return new ModelAndView("result", map);
    }

    @PostMapping("/vision")
    public ModelAndView uploadImage(@RequestBody @RequestParam("file") MultipartFile uploadFile) throws Exception, IOException {
        List<String> stopword=stopwordService.getStopWords();

        BatchAnnotateImagesResponse responses;

        ModelMap map=new ModelMap();

        ByteString imgBytes = ByteString.readFrom(uploadFile.getInputStream());
        Image image = Image.newBuilder().setContent(imgBytes).build();

        // Sets the type of request to label detection, to detect broad sets of categories in an image.
        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().setImage(image).addFeatures(feature).build();
        responses = this.imageAnnotatorClient.batchAnnotateImages(Collections.singletonList(request));

        String token="food";
        // We're only expecting one response.
        if (responses.getResponsesCount() == 1) {
            AnnotateImageResponse response = responses.getResponses(0);

            ImmutableMap.Builder<String, Float> annotations = ImmutableMap.builder();

            HashSet<String> tags=new HashSet<>();
            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                String tag=annotation.getDescription();
                if(tags.contains(tag)) continue;
                else tags.add(tag);

                if(!stopword.contains(tag)) {

                    token=tag;
                    System.out.println(token);
                    break;
                }

                annotations.put(annotation.getDescription(), annotation.getScore());
            }
            map.addAttribute("annotations", annotations.build());
        }

        map.addAttribute("token",token);
        return new ModelAndView("index2",map);
    }



    @GetMapping("/test/{term}")
    public BusinessSearchResponse test(@PathVariable("term") String term) throws Exception{

        List<String> category=new ArrayList<>();
        category.add("food");

        BusinessSearchParameters para=new BusinessSearchParameters();
        para.setLocation("pittsburgh");
        para.setSortBy(SortBy.BEST_MATCH);
        para.setTerm(term);
        BusinessSearchResponse res=yelpApi.searchBusiness(para);
        return res;
    }

    @PostMapping("/newView")
    public ModelAndView test1(String term) throws Exception{

        ModelMap map=new ModelMap();

        map.addAttribute("token",term);
        return new ModelAndView("index2",map);

    }
//    public BusinessSearchResponse test(@RequestBody BusinessSearchParameters parameters) throws Exception{
////        BusinessSearchParameters parameters=new BusinessSearchParameters();
//        BusinessSearchResponse res=yelpApi.searchBusiness(parameters);
//        return res;
//    }

}
