package com.SCM.IOMS.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@Data @NoArgsConstructor @AllArgsConstructor
public class Asset {

    public enum AssetStatus { AVAILABLE, IN_USE, MAINTENANCE, LIQUIDATED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_code", nullable = false, unique = true, length = 50)
    private String assetCode;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(length = 100)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AssetStatus status = AssetStatus.AVAILABLE;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}