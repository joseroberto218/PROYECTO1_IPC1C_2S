# Manual Técnico - Sistema de Gestión de Inventario

## Descripción General del Sistema

Este sistema de gestión de inventario está implementado en Java utilizando arrays estáticos como estructura principal de almacenamiento de datos. El programa mantiene cinco arrays paralelos (nombres, categorías, precios, cantidades y códigos) con un tamaño máximo de 100 elementos, controlados por la variable `totalProductos` que actúa como índice y contador. Es fundamental entender que todas las operaciones CRUD (crear, leer, actualizar, eliminar) dependen de la sincronización entre estos arrays, por lo que cualquier modificación debe mantener la coherencia de índices entre todos los vectores.

El sistema implementa un patrón de menú con switch-case y utiliza métodos estáticos para cada funcionalidad principal. La persistencia de datos se maneja mediante archivos de texto plano (ventas.txt) para el registro de transacciones, mientras que los reportes se generan como archivos PDF reales utilizando la librería iText. La bitácora del sistema se almacena temporalmente en memoria durante la ejecución y se reinicia en cada arranque del programa.

## Requerimientos de la Aplicación

### Requerimientos de Hardware
- Procesador: Intel Core i3 o equivalente
- Memoria RAM: 4 GB mínimo
- Espacio en disco: 100 MB libres
- Sistema operativo: Windows 7+, macOS 10.12+, o Linux

### Requerimientos de Software
- Java JDK 17 o superior
- Apache Maven 3.6+
- IDE recomendado: NetBeans 24, IntelliJ IDEA, o Eclipse

### Dependencias del Proyecto
```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>
```

## Descripción de Métodos Principales

### `main(String[] args)`
- **Propósito:** Función principal que ejecuta el menú interactivo del sistema
- **Parámetros:** Argumentos de línea de comandos (no utilizados)
- **Funcionalidad:** Presenta un menú de 8 opciones y ejecuta la funcionalidad seleccionada mediante switch-case
- **Variables críticas:** `opcion` (int) - controla el flujo del programa

### `agregarProducto()`
- **Propósito:** Registra un nuevo producto en el inventario
- **Validaciones críticas:**
  - Verifica espacio disponible (`totalProductos < MAX`)
  - Valida unicidad del código del producto
  - Verifica que precio sea positivo (`precio > 0`)
  - Verifica que cantidad no sea negativa (`cantidad >= 0`)
- **Variables críticas:** Utiliza `totalProductos` como índice para todos los arrays paralelos
- **Efecto:** Incrementa `totalProductos` y almacena datos en arrays paralelos

### `buscarProducto()`
- **Propósito:** Busca productos por nombre, categoría o código
- **Algoritmo:** Búsqueda lineal en todos los arrays con comparación insensible a mayúsculas
- **Funcionalidad:** Utiliza `contains()` para búsquedas parciales en nombre/categoría y `equals()` para código exacto
- **Variables críticas:** `encontrado` (boolean) - controla si se muestra mensaje de "no encontrado"

### `eliminarProducto()`
- **Propósito:** Elimina un producto del inventario por código único
- **Algoritmo crítico:** Desplazamiento de elementos hacia la izquierda para mantener arrays compactos
- **Proceso:**
  1. Busca el producto por código
  2. Mueve todos los elementos posteriores una posición hacia atrás
  3. Decrementa `totalProductos`
- **Variables críticas:** `eliminado` (boolean) - confirma éxito de la operación

### `registrarVenta()`
- **Propósito:** Procesa una venta y actualiza el inventario
- **Validaciones críticas:**
  - Verifica existencia del producto
  - Valida stock suficiente (`cantidadVendida <= cantidades[i]`)
  - Valida cantidad positiva (`cantidadVendida > 0`)
- **Persistencia:** Escribe transacción en archivo `ventas.txt` con formato separado por pipe (|)
- **Efecto:** Reduce `cantidades[i]` por la cantidad vendida

### `generarReportes()`
- **Propósito:** Genera reportes en formato PDF de stock actual y historial de ventas
- **Tecnología:** Utiliza las clases nativas de Java para escritura de archivos con formato PDF
- **Archivos generados:**
  - `{fecha}_Stock.pdf` - Lista todos los productos con información completa
  - `{fecha}_Ventas.pdf` - Historial completo de transacciones
- **Formato de fecha:** `dd_MM_yyyy_HH_mm_ss` para nombres únicos de archivo

### `registrarBitacora(String accion, String resultado)`
- **Propósito:** Registra todas las acciones del usuario para auditoría temporal
- **Parámetros:**
  - `accion` - Descripción de la operación realizada
  - `resultado` - "Correcto" o descripción del error
- **Limitación:** Máximo 200 acciones por sesión
- **Variables críticas:** `totalAcciones` - contador e índice para array de bitácora

### `mostrarBitacora()`
- **Propósito:** Muestra el historial completo de acciones de la sesión actual
- **Funcionalidad:** Recorre array `bitacora[]` hasta `totalAcciones`
- **Nota:** Se auto-registra en la bitácora al ser ejecutada

## Estructura de Datos Críticas

### Arrays Paralelos
```java
static String[] nombres = new String[MAX];      // Nombres de productos
static String[] categorias = new String[MAX];  // Categorías
static double[] precios = new double[MAX];     // Precios en quetzales
static int[] cantidades = new int[MAX];        // Stock disponible
static String[] codigos = new String[MAX];     // Códigos únicos
```

### Contadores Globales
- `totalProductos` - Número actual de productos registrados (0 a 99)
- `totalAcciones` - Número de acciones registradas en bitácora (0 a 199)

### Archivos del Sistema
- `ventas.txt` - Persistencia de transacciones con formato: codigo|nombre|cantidad|total|fecha
- `{fecha}_Stock.pdf` - Reporte de inventario actual
- `{fecha}_Ventas.pdf` - Reporte de historial de ventas

## Consideraciones para Mantenimiento

1. **Sincronización de Arrays:** Cualquier modificación debe mantener la coherencia entre todos los arrays paralelos
2. **Límites de Capacidad:** Sistema limitado a 100 productos y 200 acciones de bitácora por sesión
3. **Persistencia:** Solo las ventas se guardan permanentemente; inventario y bitácora se reinician cada ejecución
4. **Validaciones:** Todas las entradas del usuario están validadas para prevenir estados inconsistentes
5. **Manejo de Errores:** Operaciones críticas incluyen manejo de excepciones para E/S de archivos