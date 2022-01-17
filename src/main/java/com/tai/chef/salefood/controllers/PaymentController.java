package com.tai.chef.salefood.controllers;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.tai.chef.salefood.constant.PaypalPaymentIntent;
import com.tai.chef.salefood.constant.PaypalPaymentMethod;
import com.tai.chef.salefood.security.services.PaypalService;
import com.tai.chef.salefood.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {
    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";

    private final PaypalService paypalService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestParam("price") Double price) {
        String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        try {
            Payment payment = paypalService.createPayment(
                    Objects.isNull(price) ? 100 : price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay() {
        return "payment";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
