package com.example.demo.controllers;

import com.example.demo.dto.request.BuyItemsRequest;
import com.example.demo.services.content.ItemService;
import com.example.demo.services.payment.StripeClientService;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Transactional
    public Charge chargeCard(HttpServletRequest request, @RequestBody BuyItemsRequest items) throws Exception {
        itemService.buyItems(items.getItems(), request.getHeader("email"));
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
//        itemService.buyItems(request.getHeader("email"), items.getItems(), items.getQuantities());
        return this.stripeClientService.chargeCreditCard(token, amount);
    }
}




