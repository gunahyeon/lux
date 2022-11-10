package com.used.lux.response.appraisal;

import com.used.lux.dto.*;
import com.used.lux.response.UserAccountResponse;

import java.time.LocalDateTime;

public record AppraisalRequestResponse(
        Long id,
        String appraisalProductName,
        String appraisalBrandName,
        String appraisalGender,
        String appraisalColor,
        String appraisalSize,
        String appraisalStateName,
        String appraisalStateStep,
        UserAccountResponse userAccount,
        Long appraisalId,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
)  {

    public static AppraisalRequestResponse of(Long id, String appraisalProductName, String appraisalBrandName, String appraisalGender, String appraisalColor,
                                    String appraisalSize, String appraisalStateName, String appraisalStateStep, UserAccountResponse userAccount,
                                    Long appraisalId, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new AppraisalRequestResponse(id, appraisalProductName, appraisalBrandName, appraisalGender, appraisalColor, appraisalSize,
                appraisalStateName, appraisalStateStep, userAccount, appraisalId, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static AppraisalRequestResponse from(AppraisalRequestDto dto) {
        return new AppraisalRequestResponse(
                dto.id(),
                dto.appraisalProductName(),
                dto.appraisalBrand().brandName(),
                dto.appraisalGender(),
                dto.appraisalColor(),
                dto.appraisalSize(),
                dto.appraisalState().stateName(),
                dto.appraisalState().stateStep(),
                UserAccountResponse.from(dto.userAccount()),
                dto.appraisalId(),
                dto.createdAt(),
                dto.createdBy(),
                dto.modifiedAt(),
                dto.modifiedBy()
        );
    }

}