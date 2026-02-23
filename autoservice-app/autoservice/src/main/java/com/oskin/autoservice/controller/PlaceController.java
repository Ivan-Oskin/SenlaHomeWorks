package com.oskin.autoservice.controller;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.service.PlaceService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autoservice")
public class PlaceController {
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService){
        this.placeService = placeService;
    }

    @GetMapping("/places")
    public List<PlaceDto> findAll(){
        return placeService.getListOfPlace(1);
    }

    @GetMapping("/places/{id}")
    public PlaceDto findById(@PathVariable("id") int id){
        return placeService.findPlace(id);
    }

    @PostMapping("/places")
    public void save(@RequestBody PlaceRequest placeRequest){
        placeService.addPlace(placeRequest);
    }

    @PutMapping("/places/{id}")
    public void update(@PathVariable("id") int id, @RequestBody PlaceRequest placeRequest){
        placeService.updatePlace(id, placeRequest);
    }

    @DeleteMapping("/places/{id}")
    public void delete(@PathVariable("id") int id){
        placeService.deletePlace(id);
    }
}
