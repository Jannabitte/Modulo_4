# Sistema de Notas de Estudiantes - Java Swing

Este proyecto corresponde a un trabajo práctico desarrollado en Java, donde apliqué diferentes contenidos vistos en el módulo, integrando estructuras de datos y herramientas propias del lenguaje. El sistema permite registrar estudiantes, guardar sus notas, calcular promedios, analizar estados académicos y visualizar la información mediante una interfaz gráfica creada con Swing.

## Descripción del proyecto

Desarrollé un sistema visual de gestión de notas estudiantiles, orientado a practicar el uso de Java de una forma más completa y ordenada. La aplicación permite ingresar estudiantes con su ID o RUT, nombre y notas, para luego calcular su promedio y determinar si el estudiante se encuentra aprobado o reprobado.

El proyecto fue trabajado con una interfaz gráfica utilizando Swing, lo que permite una interacción más visual en comparación con una aplicación solo por consola. Además, incorporé el uso de colecciones como `Map`, `Set` y `List`, junto con operaciones usando `String` y `Math`.

## Objetivo del trabajo

El objetivo principal fue demostrar el manejo de estructuras de datos en Java y aplicarlas en un caso práctico. En este caso, decidí crear un sistema de notas porque permite trabajar con datos reales, cálculos, validaciones y organización de información.

Con este proyecto practiqué:

- Creación de una interfaz gráfica con Swing.
- Registro y visualización de estudiantes.
- Uso de listas para almacenar notas.
- Uso de mapas para relacionar el ID del estudiante con sus datos.
- Uso de conjuntos para mostrar estudiantes únicos.
- Manejo de textos con String.
- Cálculo de promedios usando operaciones matemáticas.
- Organización de la información de manera visual y clara.

## Tecnologías y contenidos utilizados

- Java
- Swing
- Map
- Set
- List
- String
- Math
- Programación orientada a objetos
- Git y GitHub

## Funcionalidades principales

El sistema permite realizar las siguientes acciones:

1. **Agregar estudiante**  
   Permite ingresar un ID o RUT, nombre del estudiante y sus notas.

2. **Calcular promedio**  
   El sistema calcula automáticamente el promedio de las notas ingresadas.

3. **Determinar estado académico**  
   Según el promedio obtenido, el estudiante queda marcado como aprobado o reprobado.

4. **Buscar estudiante por ID**  
   Permite consultar información de un estudiante registrado.

5. **Editar estudiante con PIN**  
   Algunas acciones están protegidas mediante un PIN de 4 dígitos, simulando una edición autorizada.

6. **Eliminar estudiante con PIN**  
   La eliminación también requiere autorización, para proteger los datos ingresados.

7. **Mostrar estudiantes registrados**  
   Permite visualizar los estudiantes agregados en la tabla.

8. **Mostrar área de promedios**  
   Entrega un análisis general del curso, incluyendo promedio general, cantidad de aprobados y reprobados.

9. **Mostrar estudiantes únicos**  
   Utiliza `Set` para mostrar datos únicos por ID y por nombre normalizado.

10. **Mostrar mapa de estudiantes**  
   Utiliza `Map` para mostrar la relación entre el ID del estudiante y sus datos principales.


```java
