package biz.espc.shahin.entity;

import biz.espc.shahin.enumeration.transaction.TransactionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Entity
@Table(name = "TRANSACTION_REQUEST_STATUS")
@Getter
@Setter
public class TransactionRequestStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transaction_status_seq")
    @SequenceGenerator(name = "transaction_status_seq",
            sequenceName = "transaction_status_seq",
            allocationSize = 1)
    private Long id;

    @ManyToOne
    private TransactionRequest transactionRequest;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String comment;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}