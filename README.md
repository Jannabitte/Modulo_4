# Gestión de Servicios Contratados

## Descripción del proyecto

Este proyecto corresponde al trabajo final del Módulo 4, desarrollado en Java con IntelliJ IDEA y Maven. El objetivo principal fue construir un sistema de gestión de servicios contratados, aplicando los principios de la Programación Orientada a Objetos, especialmente encapsulamiento, herencia, polimorfismo y composición.

El sistema permite registrar un cliente, asociarle distintos servicios contratados y calcular el total mensual a pagar. Los servicios trabajados son Internet, Telefonía y Televisión, siguiendo el contexto solicitado en la evaluación.

Además de la lógica principal, incorporé una interfaz gráfica con Swing para poder visualizar el funcionamiento del sistema de manera más clara. También dejé una versión por consola, ya que el enunciado solicita un menú simple para interactuar con el sistema.

---

## Objetivo

Desarrollar una aplicación en Java que permita gestionar clientes y servicios contratados, calculando el total mensual según las características de cada servicio.

El proyecto busca demostrar el uso correcto de:

* Programación Orientada a Objetos.
* Clases y objetos.
* Encapsulamiento.
* Herencia.
* Polimorfismo.
* Composición.
* Pruebas unitarias con JUnit.
* Simulación de dependencias con Mockito.
* Enfoque TDD.
* Interfaz gráfica con Swing.
* Menú de consola como respaldo funcional.

---

## Tecnologías utilizadas

* Java 21
* IntelliJ IDEA
* Maven
* Swing
* JUnit 5
* Mockito
* Git
* GitHub

---

## Estructura del proyecto

```text
GestionServiciosContratados
 ├── src
 │   ├── main
 │   │   └── java
 │   │       └── cl.gestionservicios
 │   │           ├── modelo
 │   │           │   ├── Cliente.java
 │   │           │   ├── Servicio.java
 │   │           │   ├── Internet.java
 │   │           │   ├── Telefonia.java
 │   │           │   └── Television.java
 │   │           │
 │   │           ├── servicio
 │   │           │   ├── GestorClientes.java
 │   │           │   ├── Notificador.java
 │   │           │   └── NotificadorEmail.java
 │   │           │
 │   │           ├── vista
 │   │           │   └── VentanaPrincipal.java
 │   │           │
 │   │           ├── consola
 │   │           │   └── MainConsola.java
 │   │           │
 │   │           └── Main.java
 │   │
 │   └── test
 │       └── java
 │           └── cl.gestionservicios
 │               ├── ClienteTest.java
 │               ├── ServicioTest.java
 │               ├── NotificadorTest.java
 │               └── SuitePruebasTest.java
 │
 └── pom.xml
```

---

## Aplicación de Programación Orientada a Objetos

### Encapsulamiento

Los atributos de las clases se declararon como privados y se accede a ellos mediante métodos públicos cuando corresponde. Esto permite proteger los datos internos de cada objeto.

Ejemplo:

```java
private String rut;
private String nombre;
private List<Servicio> serviciosContratados;
```

---

### Herencia

Se creó una clase abstracta llamada `Servicio`, que representa las características comunes de todos los servicios contratados.

Las clases `Internet`, `Telefonia` y `Television` heredan de `Servicio`, reutilizando sus atributos y especializando su comportamiento.

```java
public abstract class Servicio {
    private String nombre;
    private double costoMensual;

    public abstract double calcularCosto();
}
```

---

### Polimorfismo

El polimorfismo se aplica mediante el método `calcularCosto()`, ya que cada servicio tiene su propia forma de calcular el valor final.

Aunque todos los servicios son tratados como objetos de tipo `Servicio`, cada clase hija responde de manera distinta.

Ejemplo:

```java
Servicio internet = new Internet("Internet Fibra", 20000, 600);
Servicio telefonia = new Telefonia("Telefonía Móvil", 12000, 1200);
Servicio television = new Television("Televisión HD", 18000, 150);
```

Cada uno ejecuta su propia versión de:

```java
calcularCosto();
```

---

### Composición

La clase `Cliente` contiene una lista de servicios contratados. Esto representa una relación de composición, ya que un cliente puede tener varios servicios asociados.

```java
private List<Servicio> serviciosContratados;
```

---

## Reglas de cálculo utilizadas

Para demostrar que cada servicio tiene una lógica propia, se definieron reglas simples de cálculo:

| Servicio   | Regla aplicada                                                                |
| ---------- | ----------------------------------------------------------------------------- |
| Internet   | Si la velocidad supera los 500 Mbps, se agrega un cargo extra de $5.000       |
| Telefonía  | Si los minutos incluidos superan los 1000, se agrega un cargo extra de $3.000 |
| Televisión | Si la cantidad de canales supera los 100, se agrega un cargo extra de $4.000  |

Ejemplo de cálculo:

```text
Internet Fibra: $20.000 + $5.000 = $25.000
Telefonía Móvil: $12.000 + $3.000 = $15.000
Televisión HD: $18.000 + $4.000 = $22.000

Total mensual: $62.000
```

---

## Funcionalidades del sistema

El sistema permite:

* Registrar un cliente con RUT y nombre.
* Seleccionar un tipo de servicio.
* Ingresar costo base del servicio.
* Ingresar un dato adicional según el servicio:

  * Velocidad para Internet.
  * Minutos para Telefonía.
  * Cantidad de canales para Televisión.
* Agregar servicios al cliente.
* Mostrar un resumen con los servicios contratados.
* Calcular el total mensual a pagar.
* Ejecutar la aplicación mediante interfaz gráfica Swing.
* Ejecutar una versión alternativa mediante consola.

---

## Interfaz gráfica Swing

La aplicación cuenta con una ventana principal desarrollada con Swing, donde se puede registrar un cliente y agregar servicios de forma visual.

La interfaz incluye:

* Campos para ingresar RUT y nombre.
* Selector de tipo de servicio.
* Campo para costo base.
* Campo para velocidad, minutos o canales.
* Botón para crear cliente.
* Botón para agregar servicio.
* Botón para mostrar resumen.
* Botón para limpiar el formulario.
* Área de resumen del cliente y total mensual.

La clase principal para ejecutar la interfaz gráfica es:

```java
Main.java
```

---

## Versión por consola

También se incorporó una versión por consola mediante la clase:

```java
MainConsola.java
```

Esta versión permite interactuar con el sistema mediante un menú simple:

```text
1. Registrar cliente
2. Agregar servicio a cliente
3. Mostrar resumen del cliente
4. Salir
```

Esta implementación se agregó para mantener el cumplimiento del enunciado y demostrar que la lógica del sistema puede funcionar tanto en consola como en una interfaz gráfica.

---

## Pruebas unitarias

El proyecto incluye pruebas unitarias con JUnit 5 para validar el correcto funcionamiento de la lógica.

Se realizaron pruebas para:

* Verificar el cálculo individual de cada servicio.
* Confirmar que el cliente inicia sin servicios contratados.
* Confirmar que el cliente puede agregar servicios.
* Validar el cálculo total mensual del cliente.
* Probar el comportamiento del notificador usando Mockito.
* Agrupar las pruebas en una suite.

Clases de prueba:

```text
ServicioTest.java
ClienteTest.java
NotificadorTest.java
SuitePruebasTest.java
```

---

## Uso de TDD

Para el desarrollo del proyecto se aplicó el enfoque TDD, escribiendo primero las pruebas y luego el código necesario para que esas pruebas pasaran correctamente.

Este enfoque permitió definir desde el inicio el comportamiento esperado del sistema, especialmente en el cálculo de costos y en la interacción entre cliente, servicios y notificador.

---

## Uso de Mockito

Se utilizó Mockito para simular el comportamiento de la interfaz `Notificador`.

Esto permitió comprobar que el sistema llama correctamente al método de notificación sin depender de un envío real de correo o mensaje.

Ejemplo del objetivo de la prueba:

```text
Verificar que el cliente sea notificado una vez con el total mensual calculado.
```

---

## Cómo ejecutar el proyecto

### Ejecutar interfaz gráfica

Desde IntelliJ IDEA, abrir y ejecutar la clase:

```java
Main.java
```

Esto abrirá la ventana Swing del sistema.

---

### Ejecutar versión por consola

Desde IntelliJ IDEA, abrir y ejecutar la clase:

```java
MainConsola.java
```

Esto iniciará el menú interactivo en consola.

---

### Ejecutar pruebas unitarias

Desde IntelliJ IDEA se puede ejecutar la clase:

```java
SuitePruebasTest.java
```

También se pueden ejecutar las pruebas desde terminal con Maven:

```bash
mvn test
```

---

## Ejemplo de uso

Datos de prueba:

```text
Cliente:
RUT: 12.345.678-9
Nombre: Ana Pérez

Servicios:
Internet - costo base 20000 - velocidad 600 Mbps
Telefonía - costo base 12000 - minutos 1200
Televisión - costo base 18000 - canales 150
```

Resultado esperado:

```text
Internet: $25000.0
Telefonía: $15000.0
Televisión: $22000.0

Total mensual: $62000.0
```

---

## Aprendizaje obtenido

Con este proyecto reforcé la importancia de separar la lógica del sistema en clases con responsabilidades claras. También pude aplicar de manera práctica los conceptos de herencia y polimorfismo, ya que cada servicio comparte una estructura común, pero calcula su costo de forma diferente.

Además, el uso de pruebas unitarias me permitió comprobar que el sistema funciona correctamente antes de avanzar con la interfaz gráfica. Esto me ayudó a entender mejor el enfoque TDD y la utilidad de probar partes específicas del código.

La incorporación de Swing me permitió visualizar el funcionamiento del sistema de una manera más cercana a una aplicación real, mientras que la versión por consola mantiene el cumplimiento del enunciado original.

---

## Estado del proyecto

Proyecto desarrollado y probado correctamente.

Incluye:

* Modelo orientado a objetos.
* Herencia y polimorfismo.
* Cálculo de servicios contratados.
* Interfaz Swing.
* Menú por consola.
* Pruebas unitarias con JUnit.
* Mock con Mockito.
* Suite de pruebas.
* Control de versiones con Git.
