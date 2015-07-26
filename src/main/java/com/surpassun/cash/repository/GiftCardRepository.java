package com.surpassun.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surpassun.cash.domain.GiftCard;

public interface GiftCardRepository extends JpaRepository<GiftCard, String> {
    public GiftCard findByCode(String code);
}
