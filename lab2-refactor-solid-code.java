interface PaymentProcessor {
    boolean processPayment(double amount);
}

class StandardPaymentProcessor implements PaymentProcessor {
    private PaymentService paymentService;

    public StandardPaymentProcessor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public boolean processPayment(double amount) {
        return paymentService.process(amount);
    }
}

class ExpressPaymentProcessor implements PaymentProcessor {
    private ExpressPaymentService expressPaymentService;

    public ExpressPaymentProcessor(ExpressPaymentService expressPaymentService) {
        this.expressPaymentService = expressPaymentService;
    }

    @Override
    public boolean processPayment(double amount) {
        return expressPaymentService.process(amount, "highPriority");
    }
}

class OrderProcessor {
    private InventoryService inventoryService;
    private OrderRepository orderRepository;
    private EmailService emailService;
    private PaymentService paymentService;
    private ExpressPaymentService expressPaymentService;

    public OrderProcessor(InventoryService inventoryService, OrderRepository orderRepository,
                          EmailService emailService, PaymentService paymentService,
                          ExpressPaymentService expressPaymentService) {
        this.inventoryService = inventoryService;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.paymentService = paymentService;
        this.expressPaymentService = expressPaymentService;
    }

    public void processOrder(Order order) {
        verifyInventory(order);
        processPayment(order);
        updateOrderStatus(order, "processed");
        notifyCustomer(order);
    }

    private void verifyInventory(Order order) {
        // Checks inventory levels
        if (!inventoryService.hasEnoughInventory(order.getProductId(), order.getQuantity())) {
            throw new InsufficientInventoryException("Out of stock");
        }
    }

    private void processPayment(Order order) {
        PaymentProcessor paymentProcessor = getPaymentProcessor(order.getType());
        if (!paymentProcessor.processPayment(order.getAmount())) {
            throw new PaymentException("Payment failed");
        }
    }

    private void updateOrderStatus(Order order, String status) {
        orderRepository.updateOrderStatus(order.getId(), status);
    }

    private void notifyCustomer(Order order) {
        emailService.sendEmail(order.getCustomerEmail(), "Your order has been processed.");
    }

    private PaymentProcessor getPaymentProcessor(String orderType) {
        if ("standard".equals(orderType)) {
            return new StandardPaymentProcessor(paymentService);
        } else if ("express".equals(orderType)) {
            return new ExpressPaymentProcessor(expressPaymentService);
        } else {
            throw new IllegalArgumentException("Invalid order type");
        }
    }
}
