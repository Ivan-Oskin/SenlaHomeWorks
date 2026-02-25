package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.request.MasterRequest;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortTypeMaster;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import com.oskin.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class MasterService {
    Config config;
    MasterRepository masterRepository;
    OrderMasterService orderMasterService;
    OrderMasterRepository orderMasterRepository;
    OrderRepository orderRepository;
    MapperToDto mapperToDto;
    MapperToEntity mapperToEntity;

    @Autowired
    public MasterService(Config config, MasterRepository masterRepository,
                         OrderMasterService orderMasterService, OrderMasterRepository orderMasterRepository,
                         OrderRepository orderRepository, MapperToDto mapperToDto, MapperToEntity mapperToEntity) {
        this.config = config;
        this.masterRepository = masterRepository;
        this.orderMasterService = orderMasterService;
        this.orderMasterRepository = orderMasterRepository;
        this.orderRepository = orderRepository;
        this.mapperToDto = mapperToDto;
        this.mapperToEntity = mapperToEntity;
    }

    @Transactional
    public void addMaster(MasterRequest masterRequest) {
        masterRepository.create(mapperToEntity.mapToMasterEntity(masterRequest));
    }

    @Transactional
    public void deleteMaster(int id) {
        Master master = masterRepository.find(id);
        if (master != null) {
            orderMasterService.deleteByMaster(id);
            masterRepository.delete(id);
        }
    }

    public MasterDto findMaster(int id) {
        return mapperToDto.mapToMasterDto(masterRepository.find(id));
    }

    @Transactional
    public void updateMaster(int id, MasterRequest masterRequest) {
        Master master = mapperToEntity.mapToMasterEntity(id, masterRequest);
        masterRepository.update(master);
    }

    public ArrayList<Master> getListOfMasters(SortTypeMaster sortType) {
        return masterRepository.findAll(sortType);
    }

    public List<MasterDto> getListOfMastersDto(SortTypeMaster sortType) {
        return masterRepository.findAll(sortType).stream().map(
                master -> mapperToDto.mapToMasterDto(master)).toList();
    }

    public ArrayList<Master> getMastersByOrder(String name) {
        Order order = orderRepository.find(name);
        if (order != null) {
            ArrayList<OrderMaster> orderMasters = orderMasterRepository.getMastersByOrderInDB(order.getId());
            return orderMasterService.getMasterFromOrderMaster(orderMasters);
        }
        return new ArrayList<>();
    }
}
