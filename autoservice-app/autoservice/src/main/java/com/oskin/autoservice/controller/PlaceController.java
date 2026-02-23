package com.oskin.autoservice.controller;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/place")
public class PlaceController {
    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService){
        this.placeService = placeService;
    }

    @ResponseBody
    @GetMapping("/places")
    public List<PlaceDto> findAll(){
        logger.info("findall");
        return placeService.getListOfPlace(1);
    }
}
