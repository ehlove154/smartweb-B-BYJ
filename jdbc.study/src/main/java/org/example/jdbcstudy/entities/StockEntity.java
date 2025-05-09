package org.example.jdbcstudy.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class StockEntity {
    private String id;
    private String isin;
    private String market;
    private String displayText;
    private LocalDateTime createdAt;

    public StockEntity() {
    }

    public StockEntity(String id, String isin, String market, String displayText, LocalDateTime createdAt) {
        this.id = id;
        this.isin = isin;
        this.market = market;
        this.displayText = displayText;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockEntity that = (StockEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
