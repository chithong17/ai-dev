package com.cobolairlines.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "emplo")
public class Employee {
    @Id
    @Column(name = "empid")
    private String empid;

    @Column(name = "admidate")
    private LocalDate admidate;

    @Column(name = "deptid")
    private Integer deptid;

    @Column(name = "stored_cryptpass")
    private String storedCryptpass;

    // getters and setters
    public String getEmpid() { return empid; }
    public void setEmpid(String empid) { this.empid = empid; }
    public LocalDate getAdmidate() { return admidate; }
    public void setAdmidate(LocalDate admidate) { this.admidate = admidate; }
    public Integer getDeptid() { return deptid; }
    public void setDeptid(Integer deptid) { this.deptid = deptid; }
    public String getStoredCryptpass() { return storedCryptpass; }
    public void setStoredCryptpass(String storedCryptpass) { this.storedCryptpass = storedCryptpass; }
}
