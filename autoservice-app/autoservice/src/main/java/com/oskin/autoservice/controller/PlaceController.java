package com.oskin.autoservice.controller;
import com.oskin.autoservice.dto.PlaceDto;
import com.oskin.autoservice.dto.request.PlaceRequest;
import com.oskin.autoservice.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("/autoservice")
public class PlaceController {
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/places")
    public List<PlaceDto> findAll() {
        return placeService.getListOfPlace(1);
    }

    @GetMapping("/places/{id}")
    public PlaceDto findById(@PathVariable("id") int id) {
        return placeService.findPlace(id);
    }

    @PostMapping("/places")
    public void save(@RequestBody PlaceRequest placeRequest) {
        placeService.addPlace(placeRequest);
    }

    @PutMapping("/places/{id}")
    public void update(@PathVariable("id") int id, @RequestBody PlaceRequest placeRequest) {
        placeService.updatePlace(id, placeRequest);
    }

    @DeleteMapping("/places/{id}")
    public void delete(@PathVariable("id") int id) {
        placeService.deletePlace(id);
    }
}
