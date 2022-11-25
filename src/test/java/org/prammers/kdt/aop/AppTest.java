package org.prammers.kdt.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prammers.kdt.order.Order;
import org.prammers.kdt.order.OrderItem;
import org.prammers.kdt.order.OrderService;
import org.prammers.kdt.order.OrderStatus;
import org.prammers.kdt.voucher.FixedAmountVoucher;
import org.prammers.kdt.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringJUnitConfig
@ActiveProfiles("test")
public class AppTest {

    @Configuration
    @ComponentScan(basePackages = {"org.prammers.kdt.voucher", "org.prammers.kdt.aop"})
    @EnableAspectJAutoProxy
    static class Config {
    }

   @Autowired
   ApplicationContext applicationContext;

    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("OrderService 를 사용해서 주문을 생성할 수 있다.")
    public void testOrderServiceCreation() {
        // Given
        FixedAmountVoucher fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);
    }
}
