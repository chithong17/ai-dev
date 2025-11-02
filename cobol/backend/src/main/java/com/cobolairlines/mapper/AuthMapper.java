package com.cobolairlines.mapper;

import com.cobolairlines.dto.AuthResponse;
import com.cobolairlines.model.Employee;

public class AuthMapper {
    public static AuthResponse toAuthResponse(Employee e, String token) {
        if (e == null) return new AuthResponse(token, null, null);
        AuthResponse r = new AuthResponse();
        r.setToken(token);
        r.setEmpid(e.getEmpid());
        r.setDeptid(e.getDeptid());
        return r;
    }
}
