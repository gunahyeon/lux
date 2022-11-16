package com.used.lux.service;


import com.used.lux.domain.*;
import com.used.lux.dto.BrandDto;
import com.used.lux.dto.ProductOrderDto;
import com.used.lux.dto.security.Principal;
import com.used.lux.repository.*;
import com.used.lux.request.BrandCreateRequest;
import com.used.lux.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ProductRepository productRepository;

    private final ProductOrderLogRepository productOrderLogRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountLogRepository userAccountLogRepository;
    private final StateRepository stateRepository;


    public Page<ProductOrderDto> productListAll(Long id, Pageable pageable){
        return productOrderRepository.findByUserAccountId(id, pageable).map(ProductOrderDto::from);
    }

    public Page<ProductOrder> orderlistPage(Long id,Pageable pageable){
        Page<ProductOrder> paging = productOrderRepository.findByUserAccountId(id, pageable);
        return paging;
    }

    @Transactional
    public void createOrder(Principal principal, Long productId, OrderCreateRequest request) {
        Product product = productRepository.findById(productId).get();
        UserAccount userAccount = userAccountRepository.findById(principal.id()).get();
        int payment = userAccount.getPoint()-product.getProductPrice();

        State stateOrder = stateRepository.findByStateStep("주문완료");
        State stateProduct = stateRepository.findByStateStep("판매완료");

        product.setState(stateProduct);
        userAccount.setPoint(payment);
        userAccountRepository.save(userAccount);
        productRepository.save(product);
        productOrderRepository.save(ProductOrder.of(
                request.name(), request.phoneNumber(), request.address(), request.email(),
                request.payment(), request.requestTerm(), stateOrder, product,userAccount
        ));
        productOrderLogRepository.save(ProductOrderLog.of(
                null,
                principal.userEmail(),
                productId,
                product.getAppraisal().getAppraisalRequest().getAppraisalProductName(),
                stateOrder,
                product.getProductPrice(),
                product.getProductSellType(),
                principal.id()
        ));
        userAccountLogRepository.save(UserAccountLog.of(
                null,
                principal.userEmail(),
                principal.userGrade(),
                product.getProductPrice(),
                "차감",
                "중고/"+productId
        ));
    }




}
