package com.tgw360.uip.entity;

import com.tgw360.uip.common.BaseObject;

import java.util.Date;

/**
 * 财务实体类
 * Created by 邹祥 on 2017/11/20 16:01
 */
public class Finance extends BaseObject {
    private Long id;
    private Date endDate; // 报告日期
    private Double npParentCompanyYOY; // 净利润同比
    private Double operatingRevenueGrowRate; // 营业收入同比
    private Double basicEPS;  // 基本每股收益
    private Double netAssetPS;  // 每股净资产
    private Double cashFlowPS;  // 每股现金流
    private Double roe;  // 净资产收益率
    private Double grossIncomeRatio; // 毛利润率

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getNpParentCompanyYOY() {
        return npParentCompanyYOY;
    }

    public void setNpParentCompanyYOY(Double npParentCompanyYOY) {
        this.npParentCompanyYOY = npParentCompanyYOY;
    }

    public Double getOperatingRevenueGrowRate() {
        return operatingRevenueGrowRate;
    }

    public void setOperatingRevenueGrowRate(Double operatingRevenueGrowRate) {
        this.operatingRevenueGrowRate = operatingRevenueGrowRate;
    }

    public Double getBasicEPS() {
        return basicEPS;
    }

    public void setBasicEPS(Double basicEPS) {
        this.basicEPS = basicEPS;
    }

    public Double getNetAssetPS() {
        return netAssetPS;
    }

    public void setNetAssetPS(Double netAssetPS) {
        this.netAssetPS = netAssetPS;
    }

    public Double getCashFlowPS() {
        return cashFlowPS;
    }

    public void setCashFlowPS(Double cashFlowPS) {
        this.cashFlowPS = cashFlowPS;
    }

    public Double getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = roe;
    }

    public Double getGrossIncomeRatio() {
        return grossIncomeRatio;
    }

    public void setGrossIncomeRatio(Double grossIncomeRatio) {
        this.grossIncomeRatio = grossIncomeRatio;
    }
}
