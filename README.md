# ironhack-lab-2
Repositorio de Laboratorio 2 de Bootcamp IronHack

## Lab Overview

En este Lab se busca analizar y utilizar lo aprendido en base a los principios de SOLID.

Se nos entrega un pseudo-codigo del cual se deben realizar ciertas actividades en busqueda de mejorarlo y aplicar los principios SOLID.

## 1.- Analyze the Code

Analizando el código proporcionado se puede conlcuir que no cumple con varios principios SOLID:  

- 1.- Single Responsibility Principle (SRP): La clase SystemManager tiene demasiadas responsabilidades. Se encarga de verificar el inventario, procesar pagos, actualizar el estado del pedido y notificar al cliente, lo cual hace que no se cumpla el principio de que una clase deberia realizar una sola responsabilidad.
    
- 2.- Open-Closed Principle (OCP): Si se desea agregar un nuevo tipo de pedido, se tendría que modificar el método processOrder, lo cual hace que no se cumpla el principio de abierto/cerrado.
    
- 3.- Dependency Inversion Principle (DIP): La clase SystemManager está directamente acoplada a implementaciones concretas de servicios como paymentService, expressPaymentService, database y emailService. Esto hace que la clase sea difícil de probar y modificar y no se cumpla con el principio.

```
public class SystemManager {
    // Otros métodos omitidos por brevedad

    // Método processOrder con múltiples responsabilidades
    public void processOrder(Order order) {
        if (order.type == "standard") {
            verifyInventory(order);
            processStandardPayment(order);
        } else if (order.type == "express") {
            verifyInventory(order);
            processExpressPayment(order, "highPriority");
        }
        updateOrderStatus(order, "processed");
        notifyCustomer(order);
    }

    // Otros métodos omitidos por brevedad
}

```

## 2.- Refactor the Code

Para refactorizar el código y cumplir con los principios SOLID, se pueden realizar ciertas modificaciones:

- Se pueden introducir interfaces para los servicios
- Pasar las implementaciones concretas a través del constructor por medio de inyección de dependencias
- También se pueden crear clases separadas para manejar diferentes tipos de pedidos.

  ### ** EN este mismo repositorio se adjunta un achivo con el codigo refactorizado: lab2-refactor-solid-code.java **

## 3.- Documment Your Changes

Se documentan los cambios al igual que los issues que se presentaban en el codigo base.

- ### Problema 1.- La clase SystemManager tenía demasiadas responsabilidades.
  - ### Principio: Single Responsibility Principle (SRP)
 
 ```
    public class OrderProcessor {
    public void processOrder(Order order) {
        verifyInventory(order);
        processPayment(order);
        updateOrderStatus(order, "processed");
        notifyCustomer(order);
    }

    private void verifyInventory(Order order) {
        // Codigo omitido para brevedad
    }

    private void processPayment(Order order) {
        PaymentProcessor paymentProcessor = getPaymentProcessor(order.getType());
        if (!paymentProcessor.processPayment(order.getAmount())) {
            throw new PaymentException("Payment failed");
        }
    }

    private void updateOrderStatus(Order order, String status) {
        // Codigo omitido para brevedad
    }

    private void notifyCustomer(Order order) {
        // Codigo omitido para brevedad
    }

    private PaymentProcessor getPaymentProcessor(String orderType) {
        // Codigo omitido para brevedad
    }
 }
```
 - ### Solución:
 -  La clase OrderProcessor ahora se encarga únicamente del procesamiento de órdenes. Cada método dentro de esta clase tiene una única responsabilidad, como verificar inventario, procesar pagos, actualizar estado de la orden y notificar al cliente. Esto cumple con el principio SRP, ya que la clase tiene una sola razón para cambiar.

- ### Problema 2.- Si se quiere agregar un nuevo tipo de pedido, se tendría que modificar el método processOrder.
  - ### Principio: Open-Closed Principle (OCP)
 ```
interface PaymentProcessor {
    boolean processPayment(double amount);
}

class StandardPaymentProcessor implements PaymentProcessor {
    // Codigo omitido para brevedad
}

class ExpressPaymentProcessor implements PaymentProcessor {
    // Codigo omitido para brevedad
}

public class OrderProcessor {
    // Codigo omitido para brevedad

    public void processOrder(Order order) {
        verifyInventory(order);
        processPayment(order);
        updateOrderStatus(order, "processed");
        notifyCustomer(order);
    }

    private void processPayment(Order order) {
        PaymentProcessor paymentProcessor = getPaymentProcessor(order.getType());
        if (!paymentProcessor.processPayment(order.getAmount())) {
            throw new PaymentException("Payment failed");
        }
    }

    private PaymentProcessor getPaymentProcessor(String orderType) {
        if ("standard".equals(orderType)) {
            return new StandardPaymentProcessor();
        } else if ("express".equals(orderType)) {
            return new ExpressPaymentProcessor();
        } else {
            throw new IllegalArgumentException("Invalid order type");
        }
    }
}

 ```

- ### Solución:
- Se ha implementado el patrón Strategy para el procesamiento de pagos. La clase OrderProcessor ahora utiliza interfaces y delega el procesamiento de pagos a clases específicas (StandardPaymentProcessor y ExpressPaymentProcessor). Esto permite añadir nuevos tipos de órdenes sin modificar la clase OrderProcessor, cumpliendo así con el principio OCP.

Con los cambios implementados en la refactorización, se logra cumplir en lo más posible con los principios SOLID aprendidos en el Lab:

- Single Responsibility Principle (SRP): Cada clase tiene una única responsabilidad. Por ejemplo, StandardOrderProcessor solo es responsable de procesar pedidos estándar.
    
- Open/Closed Principle (OCP): El sistema está abierto para la extensión pero cerrado para la modificación. Se pueden procesar nuevos tipos de pedidos agregando nuevas clases que implementen la interfaz OrderProcessor.
  
- Liskov Substitution Principle (LSP): Este principio se aplica implícitamente ya que cualquier clase que implemente la interfaz OrderProcessor puede ser sustituida sin cambiar la corrección del programa.
  
- Interface Segregation Principle (ISP): El sistema utiliza múltiples interfaces específicas (PaymentService, InventoryService, OrderStatusService, NotificationService) en lugar de una interfaz de propósito general.
   
- Dependency Inversion Principle (DIP): Los módulos de alto nivel (como OrderProcessor) dependen de abstracciones (como PaymentService), no de módulos de bajo nivel. Esto se logra mediante la inyección de dependencias en el constructor.


