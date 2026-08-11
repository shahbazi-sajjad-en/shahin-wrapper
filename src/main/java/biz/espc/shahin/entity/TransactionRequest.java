package biz.espc.shahin.entity;

import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.enumeration.transaction.TransactionPurpose;
import biz.espc.shahin.enumeration.transaction.TransactionStatus;
import biz.espc.shahin.enumeration.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTION_REQUEST")
@Getter
@Setter
public class TransactionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true, updatable = false)
    private UUID ShahinUUID;

    @Column(unique = true, nullable = false)
    private Long refCode;

    @Enumerated(EnumType.STRING)
    private Bank sourceBank;

    @Enumerated(EnumType.STRING)
    private Bank destinationBank;

    @Column
    private String nationalCode;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus currentStatus;

    @OneToMany(mappedBy = "transactionRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionRequestStatus> statuses = new ArrayList<>();

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column
    private String fromAccount;

    @Column
    private String toAccount;

    @Column
    private String paymentId;

    @Column
    private String documentId;

    @Column
    private String transferId;

    @Column
    private String depositDescription;

    @Column
    private String withdrawDescription;

    @Column
    private String errorCode;

    @Column
    private String errorMessage;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime responseDate;

    public void changeStatus(TransactionStatus newStatus, String comment) {
        TransactionRequestStatus history = new TransactionRequestStatus();

        history.setTransactionRequest(this);
        history.setStatus(newStatus);
        history.setComment(comment);
        history.setCreatedAt(LocalDateTime.now());
        this.currentStatus = newStatus;
        this.statuses.add(history);
    }
}