package com.serviceops.architecture.servicesarchitecture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {


    @GetMapping("/arch-gen")
    public String testpage(Model model) {

        return "test";
    }

    @GetMapping("/bubble")
    public String bubblepage(Model model) {

        return "bubble";
    }
}
