package biz.espc.shahin.mapper;

import biz.espc.shahin.dto.outbound.transaction.TransactionRequestDto;
import biz.espc.shahin.entity.TransactionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionRequestMapper {

    TransactionRequest toEntity(TransactionRequestDto dto);
}