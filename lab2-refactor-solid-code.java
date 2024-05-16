// Se aplica el Dependency Inversion Principle (DIP) ya que los módulos de alto nivel (como OrderProcessor) no deben depender de los módulos de bajo nivel.
interface PaymentService {
    boolean process(double amount);
}

// Se aplica el Open/Closed Principle (OCP) ya que permite que el sistema esté abierto para nuevos tipos de Processor pero cerrado para la modificación.
interface OrderProcessor {
    void processOrder(Order order);
}

// Se aplica el Single Responsibility Principle (SRP) ya que tiene solo una razón para cambiar, que son los cambios en cómo se procesan los StandardOrder.
class StandardOrderProcessor implements OrderProcessor {
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private OrderStatusService orderStatusService;
    private NotificationService notificationService;

    // Se aplica DIP.
    public StandardOrderProcessor(PaymentService paymentService, InventoryService inventoryService, OrderStatusService orderStatusService, NotificationService notificationService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.orderStatusService = orderStatusService;
        this.notificationService = notificationService;
    }

    // Se aplica SRP ya que es el único método en esta clase, y encapsula el comportamiento de procesar un pedido estándar.
    @Override
    public void processOrder(Order order) {
        inventoryService.verifyInventory(order);
        paymentService.process(order.amount);
        orderStatusService.updateOrderStatus(order, "processed");
        notificationService.notifyCustomer(order);
    }
}

// Se pueden crear clases similares para simular el ExpressOrderProcessor
// Esto sigue el Open/Closed Principle ya que se pueden procesar nuevos tipos de pedidos agregando nuevas clases que implementen la interfaz OrderProcessor.
