package com.example.demo.services.payment;

import com.example.demo.services.content.ItemService;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripeClientService {

    @Value("${app.stripe.secret.key}")
    private String SECRET_KEY;

    private final ItemService itemServiceImpl;

    StripeClientService(ItemService itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
        Stripe.apiKey = SECRET_KEY;
    }

    public Charge chargeCreditCard(String token, double amount) throws Exception {
        Stripe.apiKey = SECRET_KEY;
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        return Charge.create(chargeParams);
    }

    public Customer createCustomer(String token, String email) throws Exception {
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
        customerParams.put("source", token);
        return Customer.create(customerParams);
    }

    public Charge chargeCustomerCard(String customerId, int amount) throws Exception {
        Stripe.apiKey = SECRET_KEY;
        String sourceCard = Customer.retrieve(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        return Charge.create(chargeParams);
    }
}




