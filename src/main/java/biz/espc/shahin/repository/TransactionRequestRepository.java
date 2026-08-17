package biz.espc.shahin.repository;

import biz.espc.shahin.entity.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {
}