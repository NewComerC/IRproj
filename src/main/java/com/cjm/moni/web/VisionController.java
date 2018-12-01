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
import com.cjm.moni.entity.response.BusinessSearchResponse;
import com.cjm.moni.service.YelpApi;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;


@RestController

public class VisionController {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ImageAnnotatorClient imageAnnotatorClient;

    @Autowired
    private YelpApi yelpApi;

    /**
     * This method downloads an image from a URL and sends its contents to the Vision API for label detection.
     * @return a string with the list of labels and percentage of certainty
     * @throws Exception if the Vision API call produces an error
     */
//    @PostMapping("/vision")
//    public ModelAndView uploadImage(@RequestParam("file")MultipartFile file,HttpServletRequest httpServletRequest,String imageUrl, ModelMap map) throws Exception, IOException {
//        // Copies the content of the image to memory.
//        if (!file.isEmpty()) {
//            Date date = new Date();
//            String realPath = httpServletRequest.getSession().getServletContext().getRealPath("");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//            String suffix  = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            String fileName = dateFormat.format(date) + getRandom() + suffix;
//            String path = realPath + "upload_img\\" + fileName;
//            try {
//                File dir = new File(realPath + "upload_img");
//                if (dir.exists()) {
//                    file.transferTo(new File(path));
//                } else {
//                    dir.mkdirs();
//                    file.transferTo(new File(path));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imageUrl = path;
//        }
////        byte[] imageBytes = StreamUtils.copyToByteArray(this.resourceLoader.getResource(imageUrl).getInputStream());
//
//        BatchAnnotateImagesResponse responses;
//
//        ByteString imgBytes = ByteString.readFrom(resourceLoader.getResource("file:"+imageUrl).getInputStream());
//        Image image = Image.newBuilder().setContent(imgBytes).build();
//
////        Image image = Image.newBuilder().setContent(ByteString.copyFrom(imageBytes)).build();
//
//        // Sets the type of request to label detection, to detect broad sets of categories in an image.
//        Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
//        AnnotateImageRequest request =
//                AnnotateImageRequest.newBuilder().setImage(image).addFeatures(feature).build();
//        responses = this.imageAnnotatorClient.batchAnnotateImages(Collections.singletonList(request));
//
//        // We're only expecting one response.
//        if (responses.getResponsesCount() == 1) {
//            AnnotateImageResponse response = responses.getResponses(0);
//
//            ImmutableMap.Builder<String, Float> annotations = ImmutableMap.builder();
//
//            HashSet<String> tags=new HashSet<>();
//            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
//                String tag=annotation.getDescription();
//                if(tags.contains(tag)) continue;
//                else tags.add(tag);
//
//                annotations.put(annotation.getDescription(), annotation.getScore());
//            }
//            map.addAttribute("annotations", annotations.build());
//        }
//
//        map.addAttribute("imageUrl", imageUrl);
//
//        return new ModelAndView("result", map);
//    }

    @PostMapping("/vision")
    public ModelAndView uploadImage(@RequestBody @RequestParam("file") MultipartFile uploadFile) throws Exception, IOException {
        // Copies the content of the image to memory.

        BatchAnnotateImagesResponse responses;

        ModelMap map=new ModelMap();

        ByteString imgBytes = ByteString.readFrom(uploadFile.getInputStream());
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

        map.addAttribute("imageUrl", "https://cloud.google.com/vision/docs/images/demo-image.jpg");

//        BusinessSearchParameters businessSearchParameters=new BusinessSearchParameters("pittsburgh","")

        return new ModelAndView("result", map);
    }



    @PostMapping("/test")
    public BusinessSearchResponse test(@RequestBody BusinessSearchParameters parameters) throws Exception{
//        BusinessSearchParameters parameters=new BusinessSearchParameters();

        return yelpApi.searchBusiness(parameters);
    }

    private static String getRandom() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            str = str + new Random().nextInt(10);
        }
        return str;
    }

}
