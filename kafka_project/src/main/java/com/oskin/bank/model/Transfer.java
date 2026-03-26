package com.oskin.bank.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "id_withdrawing")
    private int idWithdrawing;
    @Column(name = "id_depositing")
    private int idDepositing;
    @Column(name = "sum")
    private int sum;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdWithdrawing() {
        return idWithdrawing;
    }

    public void setIdWithdrawing(int idWithdrawing) {
        this.idWithdrawing = idWithdrawing;
    }

    public int getIdDepositing() {
        return idDepositing;
    }

    public void setIdDepositing(int idDepositing) {
        this.idDepositing = idDepositing;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Transfer(int idWithdrawing, int idDepositing, int sum) {
        this.idWithdrawing = idWithdrawing;
        this.idDepositing = idDepositing;
        this.sum = sum;
    }

    public Transfer() {
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
}
