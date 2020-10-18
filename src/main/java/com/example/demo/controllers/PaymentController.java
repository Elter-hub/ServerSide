package com.example.demo.controllers;

import com.example.demo.services.content.ItemService;
import com.example.demo.services.payment.StripeClientService;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final StripeClientService stripeClientService;
    private final ItemService itemService;

    @Autowired
    PaymentController(StripeClientService stripeClientService, ItemService itemService) {
        this.stripeClientService = stripeClientService;
        this.itemService = itemService;
    }

    @PostMapping("/charge")
    public Charge chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        itemService.buyItems(request.getHeader("email"));
        return this.stripeClientService.chargeCreditCard(token, amount);
    }
}




