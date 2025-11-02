package com.cobolairlines.dto;

public class AuthResponse {
    private String token;
    private String empid;
    private Integer deptid;

    public AuthResponse() {}

    public AuthResponse(String token, String empid, Integer deptid) {
        this.token = token;
        this.empid = empid;
        this.deptid = deptid;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmpid() { return empid; }
    public void setEmpid(String empid) { this.empid = empid; }
    public Integer getDeptid() { return deptid; }
    public void setDeptid(Integer deptid) { this.deptid = deptid; }
}
