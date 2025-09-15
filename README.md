Manual Técnico - Sistema de Gestión de Inventario
Descripción General del Sistema
Este sistema de gestión de inventario está implementado en Java utilizando arrays estáticos como estructura principal de almacenamiento de datos. El programa mantiene cinco arrays paralelos (nombres, categorías, precios, cantidades y códigos) con un tamaño máximo de 100 elementos, controlados por la variable totalProductos que actúa como índice y contador. Es fundamental entender que todas las operaciones CRUD (crear, leer, actualizar, eliminar) dependen de la sincronización entre estos arrays, por lo que cualquier modificación debe mantener la coherencia de índices entre todos los vectores.
El sistema implementa un patrón de menú con switch-case y utiliza métodos estáticos para cada funcionalidad principal. La persistencia de datos se maneja mediante archivos de texto plano (ventas.txt) para el registro de transacciones, mientras que los reportes se generan como archivos PDF reales utilizando la librería iText. La bitácora del sistema se almacena temporalmente en memoria durante la ejecución y se reinicia en cada arranque del programa.
Requerimientos de la Aplicación
Requerimientos de Hardware

Procesador: Intel Core i3 o equivalente
Memoria RAM: 4 GB mínimo
Espacio en disco: 100 MB libres
Sistema operativo: Windows 7+, macOS 10.12+, o Linux

Requerimientos de Software

Java JDK 17 o superior (compatible con JDK 23)
Apache Maven 3.6+
IDE recomendado: NetBeans 24, IntelliJ IDEA, o Eclipse

Dependencias del Proyecto
xml<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.13</version>
</dependency>
Descripción de Métodos Principales
main(String[] args)

Propósito: Función principal que ejecuta el menú interactivo del sistema
Parámetros: Argumentos de línea de comandos (no utilizados)
Funcionalidad: Presenta un menú de 8 opciones y ejecuta la funcionalidad seleccionada mediante switch-case
Variables críticas: opcion (int) - controla el flujo del programa

agregarProducto()

Propósito: Registra un nuevo producto en el inventario
Validaciones críticas:

Verifica espacio disponible (totalProductos < MAX)
Valida unicidad del código del producto
Verifica que precio sea positivo (precio > 0)
Verifica que cantidad no sea negativa (cantidad >= 0)


Variables críticas: Utiliza totalProductos como índice para todos los arrays paralelos
Efecto: Incrementa totalProductos y almacena datos en arrays paralelos

buscarProducto()

Propósito: Busca productos por nombre, categoría o código
Algoritmo: Búsqueda lineal en todos los arrays con comparación insensible a mayúsculas
Funcionalidad: Utiliza contains() para búsquedas parciales en nombre/categoría y equals() para código exacto
Variables críticas: encontrado (boolean) - controla si se muestra mensaje de "no encontrado"

eliminarProducto()

Propósito: Elimina un producto del inventario por código único
Algoritmo crítico: Desplazamiento de elementos hacia la izquierda para mantener arrays compactos
Proceso:

Busca el producto por código
Mueve todos los elementos posteriores una posición hacia atrás
Decrementa totalProductos


Variables críticas: eliminado (boolean) - confirma éxito de la operación

registrarVenta()

Propósito: Procesa una venta y actualiza el inventario
Validaciones críticas:

Verifica existencia del producto
Valida stock suficiente (cantidadVendida <= cantidades[i])
Valida cantidad positiva (cantidadVendida > 0)


Persistencia: Escribe transacción en archivo ventas.txt con formato separado por pipe (|)
Efecto: Reduce cantidades[i] por la cantidad vendida

generarReportes() 

Propósito: Genera reportes en formato PDF de stock actual y historial de ventas con validaciones previas
Tecnología: Utiliza iText 5.5.13 para generación de PDFs
Validaciones implementadas:

Stock: Verifica que totalProductos > 0 antes de generar reporte
Ventas: Valida existencia y contenido del archivo ventas.txt usando File.exists() y File.length()


Manejo de errores mejorado:

Mensajes específicos por tipo de error
Registro diferenciado en bitácora por cada tipo de fallo
Uso de return para detener ejecución en caso de errores


Archivos generados:

{fecha}_Stock.pdf - Solo si hay productos registrados
{fecha}_Ventas.pdf - Solo si hay ventas en el archivo


Formato de fecha: dd_MM_yyyy_HH_mm_ss para nombres únicos de archivo
Mensajes de error específicos:

"ERROR AL GENERAR REPORTE DE STOCK: No hay productos registrados"
"NO HAY VENTAS :c" cuando no existen ventas
Mensajes técnicos específicos para problemas de E/S



registrarBitacora(String accion, String resultado)

Propósito: Registra todas las acciones del usuario para auditoría temporal
Parámetros:

accion - Descripción de la operación realizada
resultado - "Correcto" o descripción del error específico


Limitación: Máximo 200 acciones por sesión
Variables críticas: totalAcciones - contador e índice para array de bitácora
Registros de error actualizados: Incluye estados específicos como "Error: sin productos", "Error: sin ventas"

mostrarBitacora()

Propósito: Muestra el historial completo de acciones de la sesión actual
Funcionalidad: Recorre array bitacora[] hasta totalAcciones
Nota: Se auto-registra en la bitácora al ser ejecutada

verDatosEstudiante()

Propósito: Muestra información del desarrollador del sistema
Datos mostrados:

Nombre: Jose Roberto Orozco Orozco
ID: 202200030
Curso: Introducción a la Programación y Computación 1
Proyecto: Proyecto 1, 2do Semestre 2025, ING-USAC



Estructura de Datos Críticas
Arrays Paralelos
javastatic String[] nombres = new String[MAX];      // Nombres de productos
static String[] categorias = new String[MAX];  // Categorías
static double[] precios = new double[MAX];     // Precios en quetzales
static int[] cantidades = new int[MAX];        // Stock disponible
static String[] codigos = new String[MAX];     // Códigos únicos
Contadores Globales

totalProductos - Número actual de productos registrados (0 a 99)
totalAcciones - Número de acciones registradas en bitácora (0 a 199)

Archivos del Sistema

ventas.txt - Persistencia de transacciones con formato: codigo|nombre|cantidad|total|fecha
{fecha}_Stock.pdf - Reporte de inventario actual (solo si hay productos)
{fecha}_Ventas.pdf - Reporte de historial de ventas (solo si hay ventas)

Imports y Dependencias Utilizadas
javaimport java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;  // Agregado para validaciones de archivo
Consideraciones para Mantenimiento

Sincronización de Arrays: Cualquier modificación debe mantener la coherencia entre todos los arrays paralelos
Límites de Capacidad: Sistema limitado a 100 productos y 200 acciones de bitácora por sesión
Persistencia: Solo las ventas se guardan permanentemente; inventario y bitácora se reinician cada ejecución
Validaciones Mejoradas:

Generación de reportes incluye verificación previa de datos
Manejo específico de errores por tipo de operación
Mensajes de error diferenciados para mejor experiencia de usuario


Manejo de Errores: Operaciones críticas incluyen manejo de excepciones para E/O de archivos con mensajes específicos
Validación de Archivos: Uso de File.exists() y File.length() para verificar existencia y contenido antes de procesamiento

Flujo de Validación en Generación de Reportes
Reporte de Stock

Verifica totalProductos > 0
Si es falso: muestra error específico y registra en bitácora
Si es verdadero: procede con generación de PDF
Manejo de excepciones técnicas con mensajes diferenciados

Reporte de Ventas

Crea objeto File para ventas.txt
Verifica archivoVentas.exists() y archivoVentas.length() > 0
Si falla: muestra "NO HAY VENTAS :c" y registra error
Si pasa: procede con generación de PDF
Manejo de excepciones técnicas independiente

Mensajes de Error Implementados

Stock sin productos: "ERROR AL GENERAR REPORTE DE STOCK: No hay productos registrados"
Ventas sin datos: "NO HAY VENTAS :c"
Errores técnicos de stock: "ERROR AL GENERAR REPORTE DE STOCK: [mensaje técnico]"
Errores técnicos de ventas: "PROBLEMAS TECNICOS CON REPORTE DE VENTAS: [mensaje técnico]"
