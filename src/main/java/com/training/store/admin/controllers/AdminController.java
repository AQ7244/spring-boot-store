package com.training.store.admin.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Administrative operations")
public class AdminController {

    @GetMapping("/hello")
    @Operation(
            summary = "Say hello",
            description = "Returns a simple greeting message for admin users."
    )
    public String sayHello() {
        return "Hello Admin!";
    }
}
