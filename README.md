# ironhack-lab-2
Repositorio de Laboratorio 2 de Bootcamp IronHack

## Lab Overview

En este Lab se busca analizar y utilizar lo aprendido en base a los principios de SOLID.

Se nos entrega un pseudo-codigo del cual se deben realizar ciertas actividades en busqueda de mejorarlo y aplicar los principios SOLID.

## 1.- Analyze the Code

Analizando el código proporcionado se puede conlcuir que no cumple con varios principios SOLID:  

- Single Responsibility Principle (SRP): La clase SystemManager tiene demasiadas responsabilidades. Se encarga de verificar el inventario, procesar pagos, actualizar el estado del pedido y notificar al cliente, lo cual hace que no se cumpla el principio de que una clase deberia realizar una sola responsabilidad.
    
- Open-Closed Principle (OCP): Si se desea agregar un nuevo tipo de pedido, se tendría que modificar el método processOrder, lo cual hace que no se cumpla el principio de abierto/cerrado.
    
- Dependency Inversion Principle (DIP): La clase SystemManager está directamente acoplada a implementaciones concretas de servicios como paymentService, expressPaymentService, database y emailService. Esto hace que la clase sea difícil de probar y modificar y no se cumpla con el principio.

## 2.- Refactor the Code

Para refactorizar el código y cumplir con los principios SOLID, se pueden realizar ciertas modificaciones:

- Se pueden introducir interfaces para los servicios
- Pasar las implementaciones concretas a través del constructor por medio de inyección de dependencias
- También se pueden crear clases separadas para manejar diferentes tipos de pedidos.

  ## ** EN este mismo repositorio se adjunta un achivo con el codigo refactorizado: Lab Refactor SOLID Code.java **

## 3.- Documment Your Changes

Se documentan los cambios al igual que los issues que se presentaban en el codigo base.

- Problema 1.- La clase SystemManager tenía demasiadas responsabilidades.
  - Principio: Single Responsibility Principle (SRP)
  - Solución: Se crearon clases separadas para manejar diferentes tipos de pedidos (StandardOrderProcessor y ExpressOrderProcessor).
  - Beneficios: Cada clase tiene ahora una única responsabilidad, lo que hace que el código sea más fácil de entender, probar y mantener.  

- Problema 2.- Si se quiere agregar un nuevo tipo de pedido, se tendría que modificar el método processOrder.
  - Principio: Open-Closed Principle (OCP)
  - Solución: Se crearon clases separadas para manejar diferentes tipos de pedidos. Para agregar un nuevo tipo de pedido, simplemente se crea una nueva clase que implemente la interfaz OrderProcessor.
  - Beneficios: El código es ahora más fácil de extender, ya que se pueden agregar nuevos tipos de pedidos sin modificar el código existente.

- Problema 3.- La clase SystemManager estaba directamente acoplada a implementaciones concretas de servicios.
- Principio: Dependency Inversion Principle (DIP)
- Solución: Se introdujeron interfaces para los servicios y se pasaron las implementaciones concretas a través del constructor (inyección de dependencias).
- Beneficio: El código es ahora más flexible y fácil de probar, ya que las dependencias pueden ser fácilmente sustituidas por mocks o stubs en las pruebas.

- Con los cambios implementados y siguiendo los principios SOLID, si necesitamos agregar un nuevo tipo de pedido, podemos crear una nueva clase que implemente OrderProcessor. Esto significa que nuestro código cumple con el principio abierto/cerrado: está abierto para la extensión (podemos agregar nuevos tipos de pedidos) pero cerrado para la modificación (no necesitamos modificar el código existente para agregar nuevos tipos de pedidos).
