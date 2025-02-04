package com.used.lux.service.admin;

import com.used.lux.domain.useraccount.UserAccount;
import com.used.lux.dto.admin.AdUserAccountDto;
import com.used.lux.dto.user.appraisal.AppraisalRequestDto;
import com.used.lux.dto.user.appraisal.AppraisalRequestLogDto;
import com.used.lux.dto.user.auction.AuctionLogDto;
import com.used.lux.dto.user.order.ProductOrderCancelDto;
import com.used.lux.dto.user.order.ProductOrderLogDto;
import com.used.lux.dto.user.useraccount.UserAccountDto;
import com.used.lux.dto.user.useraccount.UserAccountLogDto;
import com.used.lux.dto.UserGradeDto;
import com.used.lux.dto.user.useraccount.UserWithdrawalDto;
import com.used.lux.repository.*;
import com.used.lux.repository.appraisal.AppraisalRequestLogRepository;
import com.used.lux.repository.appraisal.AppraisalRequestRepository;
import com.used.lux.repository.auction.AuctionLogRepository;
import com.used.lux.repository.order.ProductOrderCancelRepository;
import com.used.lux.repository.order.ProductOrderLogRepository;
import com.used.lux.repository.useraccount.UserAccountLogRepository;
import com.used.lux.repository.useraccount.UserAccountRepository;
import com.used.lux.repository.useraccount.UserWithdrawalRepository;
import com.used.lux.request.useraccount.UserMemoUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdUserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final AppraisalRequestLogRepository appraisalRequestLogRepository;

    private final ProductOrderLogRepository productOrderLogRepository;

    private final ProductOrderCancelRepository productOrderCancelRepository;

    private final AppraisalRequestRepository appraisalRequestRepository;

    private final AuctionLogRepository auctionLogRepository;

    private final UserAccountLogRepository userAccountLogRepository;

    private final UserGradeRepository userGradeRepository;

    private final UserWithdrawalRepository userWithdrawalRepository;

    @Transactional(readOnly = true)
    public Page<UserAccountDto> getUserList(Pageable pageable, String gender, String age, String grade,
                                            String date, String query) {
        return userAccountRepository.searchUser(gender, age, grade, date, query, pageable).map(UserAccountDto::from);
    }

    @Transactional(readOnly = true)
    public AdUserAccountDto getUserDetail(Long userId) {
        // 회원 상세
        UserAccountDto userAccountDto = UserAccountDto.from(userAccountRepository.findById(userId).get());
        // 주문내역
        List<ProductOrderLogDto> productOrderLogDtos = productOrderLogRepository.findByUserEmail(userAccountDto.userEmail())
                .stream().map(ProductOrderLogDto::from).collect(Collectors.toCollection(ArrayList::new));
        // 취소내역
        List<ProductOrderCancelDto> productOrderCancelDtos = productOrderCancelRepository.findByUserName(userAccountDto.userName())
                .stream().map(ProductOrderCancelDto::from).collect(Collectors.toCollection(ArrayList::new));
        // 검수내역
        // pageable일땐 stream이어도 map만해줘도된다. 상관없음.
        // list, arraylist, collection 등 다른 리스트 형태들은 파이프(stream) 역할을 해줘야한다.
        List<AppraisalRequestDto> appraisalDtos = appraisalRequestRepository.findByUserAccountId2(userAccountDto.id())
                .stream().map(AppraisalRequestDto::from).collect(Collectors.toCollection(ArrayList::new));
        // 경매 내역
        List<AuctionLogDto> auctionLogDtos = auctionLogRepository.findByBidderList(userAccountDto.userName())
                .stream().map(AuctionLogDto::from).collect(Collectors.toCollection(ArrayList::new));
        // 포인트 내역
        List<UserAccountLogDto> userAccountLogDtos = userAccountLogRepository.findByUserEmail(userAccountDto.userEmail())
                .stream().map(UserAccountLogDto::from).collect(Collectors.toCollection(ArrayList::new));

        List<AppraisalRequestLogDto> userAppraisalLogDtos = appraisalRequestLogRepository.findByUserIdOrderByModifiedAtDesc(userAccountDto.id())
                .stream().map(AppraisalRequestLogDto::from).collect(Collectors.toCollection(ArrayList::new));
        return AdUserAccountDto.of(userAccountDto, productOrderLogDtos, productOrderCancelDtos,
                appraisalDtos, auctionLogDtos, userAccountLogDtos, userAppraisalLogDtos);
    }

    @Transactional(readOnly = true)
    public UserAccountDto getUserMemo(Long userId) {
        return UserAccountDto.from(userAccountRepository.findById(userId).get());
    }

    @Transactional(readOnly = true)
    public List<UserGradeDto> getUserGrade() {
        return userGradeRepository.findAll()
                .stream().map(UserGradeDto::from).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional(readOnly = true)
    public List<UserWithdrawalDto> getUserWithdrawal() {
        return userWithdrawalRepository.findAll()
                .stream().map(UserWithdrawalDto::from).collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public void updateMemo(Long userId, UserMemoUpdateRequest request) {
        UserAccount userAccount = userAccountRepository.getReferenceById(userId);
        userAccount.setMemo(request.memo());
    }
}
