package org.prammers.kdt.example;

import org.prammers.kdt.configuration.AppConfiguration;
import org.prammers.kdt.order.Order;
import org.prammers.kdt.order.OrderItem;
import org.prammers.kdt.order.OrderService;
import org.prammers.kdt.voucher.FixedAmountVoucher;
import org.prammers.kdt.voucher.Voucher;
import org.prammers.kdt.voucher.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {
    private static final Logger logger = LoggerFactory.getLogger(OrderTester.class);

    public static void main(String[] args) throws IOException {

        logger.info("Main start");

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);

//        Resource resource = applicationContext.getResource("application.yaml");
//        System.out.println(MessageFormat.format("Resoure -> {0}", resource.getClass().getCanonicalName()));
//        File file = resource.getFile();
//        List<String> strings = Files.readAllLines(file.toPath());
//        System.out.println(strings.stream().reduce("", (a, b) -> a + "\n" +b));

        UUID customerId = UUID.randomUUID();
        VoucherRepository voucherRepository = applicationContext.getBean(VoucherRepository.class);
        Voucher voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        ArrayList<OrderItem> orderItems = new ArrayList<>() {{
                add(new OrderItem(UUID.randomUUID(), 100L, 1));
            }};

        OrderService orderService = applicationContext.getBean(OrderService.class);
        Order order = orderService.createOrder(customerId, orderItems, voucher.getVoucherId());
        Assert.isTrue(order.totalAmount() == 90L, MessageFormat.format("totalAmount{0} is not 90L", order.totalAmount()));

        applicationContext.close();
    }
}
