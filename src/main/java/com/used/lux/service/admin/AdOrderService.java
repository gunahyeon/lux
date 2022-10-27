package com.used.lux.service.admin;

import com.used.lux.domain.State;
import com.used.lux.dto.ProductOrderCancelDto;
import com.used.lux.dto.ProductOrderDto;
import com.used.lux.dto.admin.AdProductOrderDto;
import com.used.lux.repository.ProductOrderCancelRepository;
import com.used.lux.repository.ProductOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdOrderService {

    private final ProductOrderRepository productOrderRepository;

    private final ProductOrderCancelRepository productOrderCancelRepository;

    public Page<ProductOrderDto> getOrderList(Pageable pageable) {
        return productOrderRepository.findAll(pageable).map(ProductOrderDto::from);
    }

    public ProductOrderDto getOrderDetail(Long orderId) {
        ProductOrderDto productOrderDto = ProductOrderDto.from(productOrderRepository.findById(orderId).get());
        return productOrderDto;
    }

    public ProductOrderCancelDto getOrderCancel(Long orderId) {
        ProductOrderCancelDto productOrderCancelDto = ProductOrderCancelDto.from(productOrderCancelRepository.findByOrderId(orderId));
        return productOrderCancelDto;
    }
}
