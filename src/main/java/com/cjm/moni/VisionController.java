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

package com.cjm.moni;

import com.google.cloud.vision.v1.*;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ModelMap;
import com.google.cloud.vision.v1.Feature.Type;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class VisionController {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ImageAnnotatorClient imageAnnotatorClient;

    /**
     * This method downloads an image from a URL and sends its contents to the Vision API for label detection.
     *
     * @param imageUrl the URL of the image
     * @return a string with the list of labels and percentage of certainty
     * @throws Exception if the Vision API call produces an error
     */
    @GetMapping("/vision")
    public ModelAndView uploadImage(String imageUrl, ModelMap map) throws Exception, IOException {
        // Copies the content of the image to memory.

//        byte[] imageBytes = StreamUtils.copyToByteArray(this.resourceLoader.getResource(imageUrl).getInputStream());

        BatchAnnotateImagesResponse responses;

        ByteString imgBytes = ByteString.readFrom(resourceLoader.getResource("file:"+imageUrl).getInputStream());
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
            for (EntityAnnotation annotation : response.getLabelAnnotationsList()) {
                annotations.put(annotation.getDescription(), annotation.getScore());
            }
            map.addAttribute("annotations", annotations.build());
        }

        map.addAttribute("imageUrl", imageUrl);

        return new ModelAndView("result", map);
    }

    public static void detectLabels(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) -> out.printf("%s : %s\n", k, v.toString()));
                }
            }
        }
    }
}
