package com.oskin.autoservice.controller;

import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/place")
public class PlaceController {
    private final PlaceService placeService;
    private final PlaceDto placeDto;

    @Autowired
    public PlaceController(PlaceService placeService, PlaceDto placeDto){
        this.placeDto = placeDto;
        this.placeService = placeService;
    }

    @ResponseBody
    @GetMapping("/places")
    public List<PlaceDto> findAll(){
        return placeService.getListOfPlace(1);
    }
}
