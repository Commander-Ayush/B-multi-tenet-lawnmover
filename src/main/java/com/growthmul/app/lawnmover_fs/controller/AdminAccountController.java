package com.growthmul.app.lawnmover_fs.controller;

import com.cloudinary.Cloudinary;
import com.growthmul.app.lawnmover_fs.dto.ChangePasswordRequest;
import com.growthmul.app.lawnmover_fs.security.CurrentAdmin;
import com.growthmul.app.lawnmover_fs.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {

    @Autowired
    private AuthService authService;
    

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody ChangePasswordRequest req) {
        authService.changePassword(CurrentAdmin.companyId(), req.getCurrentPassword(), req.getNewPassword());
    }
}
