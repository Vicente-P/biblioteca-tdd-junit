# biblioteca-tdd-junit

Práctica de **Test-Driven Development (TDD)** con Java 21 y JUnit 5. El proyecto modela una biblioteca simple donde se pueden registrar libros, buscarlos por ISBN o título, listar los disponibles, y gestionar préstamos y devoluciones.

## Descripción del proyecto

Las clases principales son:

- `Biblioteca` — Lógica de negocio (registro, búsqueda, préstamo y devolución).
- `Libro` — Entidad con ISBN, título, autor, año de publicación y estado de disponibilidad.
- `BibliotecaTest` — Suite de tests que define el comportamiento esperado del sistema.

## Aplicación de TDD

El desarrollo siguió el ciclo **Red → Green → Refactor** de forma incremental:

1. **Red** — Se escribió un test que describe un comportamiento concreto (por ejemplo, registrar un libro y encontrarlo por ISBN). Al inicio, el test fallaba porque la funcionalidad aún no existía.
2. **Green** — Se implementó el código mínimo necesario para que el test pasara, sin añadir lógica extra.
3. **Refactor** — Una vez en verde, se mejoró la estructura del código (extracción de excepciones propias, uso de `HashMap` indexado por ISBN, métodos auxiliares en los tests) manteniendo todos los tests pasando.

El orden de implementación fue guiado por los tests: primero el registro y la búsqueda básica, luego las validaciones de datos, después la búsqueda por título y el listado de disponibles, y finalmente el ciclo de préstamo y devolución con sus casos de error. Cada nueva funcionalidad comenzó con un test fallido que actuó como especificación ejecutable.

## Decisiones de diseño

| Decisión | Motivación |
|----------|------------|
| **Almacenamiento en memoria** | El alcance del ejercicio es la lógica de dominio; no se implemento persistencia en base de datos. Se usa un `HashMap<String, Libro>` para almacenar libros de la biblioteca |
| **ISBN como clave** | El ISBN identifica de única el libro respectivo. `HashMap<String, Libro>` permite realizar operaciones básicas como buscar y insertar en tiempo constante O(1). |
| **Excepciones de dominio propias** | Cada error de negocio tiene su excepción (`DatosInvalidosException`, `LibroDuplicadoException`, etc.) para comunicar con claridad la causa del fallo. |
| **Libro disponible por defecto** | Al registrarse, un libro inicia con `disponible = true`, reflejando que entra a la biblioteca listo para préstamo. |
| **Búsqueda por título flexible** | `buscarPorTitulo` admite coincidencia parcial e ignora mayúsculas/minúsculas, facilitando búsquedas del usuario. |
| **`Optional` en búsqueda por ISBN** | Cuando el libro existe, `buscarPorIsbn` devuelve un `Optional<Libro>`. Si no existe, lanza `LibroNoEncontradoException`. |
| **Estado mutable en `Libro`** | La disponibilidad se modifica con `setDisponible` al prestar o devolver, manteniendo la entidad como fuente de verdad del estado. |
| **`@BeforeEach` en tests** | Cada test parte de una `Biblioteca` vacía, garantizando independencia entre casos de prueba. |

## Casos de prueba implementados

La suite `BibliotecaTest` contiene **16 tests** organizados por funcionalidad:

### Registro de libros

| Test | Descripción |
|------|-------------|
| `registrarLibroValido` | Registrar un libro válido permite encontrarlo por su ISBN. |
| `libroNuevoDisponible` | Un libro recién registrado queda disponible para préstamo. |
| `noRegistrarLibroConIsbnVacio` | Rechaza registro con ISBN vacío o `null`. |
| `noRegistrarLibroConTituloVacio` | Rechaza registro con título vacío o `null`. |
| `noRegistrarLibroConIsbnDuplicado` | Rechaza registrar dos libros con el mismo ISBN. |

### Búsqueda

| Test | Descripción |
|------|-------------|
| `buscarLibroPorIsbn` | Encuentra un libro existente por su ISBN. |
| `buscarLibroInexistentePorIsbn` | Lanza excepción al buscar un ISBN que no existe. |
| `buscarLibroPorCoincidenciaParcialDelTitulo` | Encuentra libros cuyo título contiene el texto buscado. |
| `buscarLibroPorTituloSinDiferenciarMayusculasYMinusculas` | La búsqueda por título no distingue mayúsculas de minúsculas. |

### Disponibilidad y listado

| Test | Descripción |
|------|-------------|
| `listarLibrosDisponibles` | Devuelve solo los libros que no están prestados. |

### Préstamo

| Test | Descripción |
|------|-------------|
| `prestarLibroDisponible` | Prestar un libro disponible lo marca como no disponible. |
| `noPrestarLibroInexistente` | Lanza excepción al intentar prestar un ISBN inexistente. |
| `noPrestarLibroQueYaEstaPrestado` | Lanza excepción al intentar prestar un libro ya prestado. |

### Devolución

| Test | Descripción |
|------|-------------|
| `devolverLibroPrestado` | Devolver un libro prestado lo marca como disponible nuevamente. |
| `noDevolverLibroInexistente` | Lanza excepción al intentar devolver un ISBN inexistente. |
| `noDevolverLibroQueYaEstaDisponible` | Lanza excepción al intentar devolver un libro que ya estaba disponible. |

## Instrucciones de ejecución

### Requisitos previos

- Java 21 o superior
- Apache Maven 3.6+
- GNU Make (opcional). En Windows puedes instalarlo con [Chocolatey](https://chocolatey.org/) (`choco install make`), Git Bash o WSL.

Verifica que Java y Maven estén disponibles:

```bash
java -version
mvn -version
```

### Compilar

Con Make:

```bash
make compile
```

Con Maven:

```bash
mvn compile
```

### Ejecutar tests

Con Make:

```bash
make test      # Compila y ejecuta todos los tests
make all       # Equivalente a make test (objetivo por defecto)
```

Con Maven:

```bash
mvn test           # Compila y ejecuta los tests
mvn clean test     # Limpia artefactos previos y ejecuta desde cero
```

Los reportes de Surefire se generan en `target/surefire-reports/`.

### Limpiar artefactos

```bash
make clean
# o
mvn clean
```

### Generar JAR (sin tests)

```bash
make package
# o
mvn package -DskipTests
```
