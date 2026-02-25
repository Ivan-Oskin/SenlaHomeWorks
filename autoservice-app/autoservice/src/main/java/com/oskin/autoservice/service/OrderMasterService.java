package com.oskin.autoservice.service;

import com.oskin.autoservice.dto.MasterDto;
import com.oskin.autoservice.dto.OrderDto;
import com.oskin.autoservice.dto.OrderMasterDto;
import com.oskin.autoservice.model.Master;
import com.oskin.autoservice.model.Order;
import com.oskin.autoservice.model.OrderMaster;
import com.oskin.autoservice.model.SortTypeOrderMaster;
import com.oskin.autoservice.repository.MasterRepository;
import com.oskin.autoservice.repository.OrderRepository;
import com.oskin.autoservice.repository.OrderMasterRepository;
import com.oskin.autoservice.utils.MapperToDto;
import com.oskin.autoservice.utils.MapperToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMasterService {
    private final MasterRepository masterRepository;
    private final OrderRepository orderRepository;
    private final OrderMasterRepository orderMasterRepository;
    private final MapperToDto mapperToDto;
    private final MapperToEntity mapperToEntity;

    @Autowired
    public OrderMasterService(MasterRepository masterRepository, OrderRepository orderRepository,
                              OrderMasterRepository orderMasterRepository, MapperToDto mapperToDto, MapperToEntity mapperToEntity) {
        this.masterRepository = masterRepository;
        this.orderRepository = orderRepository;
        this.orderMasterRepository = orderMasterRepository;
        this.mapperToDto = mapperToDto;
        this.mapperToEntity = mapperToEntity;
    }

    public List<OrderMasterDto> getListOfOrderMasterDto() {
        return orderMasterRepository.findAll(SortTypeOrderMaster.ID).stream().map(mapperToDto::mapToOrderMasterDto).toList();
    }


    @Transactional
    public void addOrderMaster(int masterId, int orderId) {
        Master master = masterRepository.find(masterId);
        Order order = orderRepository.find(orderId);
        if (master != null && order != null) orderMasterRepository.create(new OrderMaster(order, master));
        else throw new NullPointerException();
    }

    public ArrayList<Master> getMasterFromOrderMaster(ArrayList<OrderMaster> orderMasters) {
        ArrayList<Master> masters = new ArrayList<>(orderMasters.size());
        for (OrderMaster orderMaster : orderMasters) {
            masters.add(orderMaster.getMaster());
        }
        return masters;
    }

    @Transactional
    public void deleteByMaster(int idMaster) {
        orderMasterRepository.deleteByMaster(idMaster);
    }

    @Transactional
    public void delete(int id) {
        orderMasterRepository.delete(id);
    }

    @Transactional
    public void deleteByOrder(int idOrder) {
        orderMasterRepository.deleteByOrder(idOrder);
    }

    public OrderMasterDto findOrderMaster(int id) {
        return mapperToDto.mapToOrderMasterDto(orderMasterRepository.find(id));
    }

    @Transactional
    public void updateOrderMaster(int id, OrderDto orderDto, MasterDto masterDto) {
        OrderMaster orderMaster = mapperToEntity.mapToOrderMasterEntity(id, orderDto, masterDto);
        orderMasterRepository.update(orderMaster);
    }

    public List<OrderMasterDto> getOrderMasterDtoByMaster(int masterId) {
        return orderMasterRepository.getOrdersByMasterInDB(masterId).stream().map(mapperToDto::mapToOrderMasterDto).toList();
    }

    public List<OrderMasterDto> getOrderMasterDtoByOrder(int orderId) {
        return orderMasterRepository.getMastersByOrderInDB(orderId).stream().map(mapperToDto::mapToOrderMasterDto).toList();
    }
}
