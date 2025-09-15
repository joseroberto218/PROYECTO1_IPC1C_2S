package com.mycompany.proyecto1_202200030;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class PROYECTO1_202200030 {

    static final int MAX = 100; //Máximo número de productos permitidos

    //Vectores que almanecan los datos del inventario
    static String[] nombres = new String[MAX];
    static String[] categorias = new String[MAX];
    static double[] precios = new double[MAX];
    static int[] cantidades = new int[MAX];
    static String[] codigos = new String[MAX];
    static int totalProductos = 0; //contador de productos registrados

    static String[] bitacora = new String[200];
    static int totalAcciones = 0; //contador de acciones registradas

    static Scanner sc = new Scanner(System.in);
    
    //Función principal (MENÚ)
    public static void main(String[] args) {
        int opcion;
        do {
            //Menú principal
            System.out.println("\n MENU DE INVENTARIO");
            System.out.println("1. AGREGAR PRODUCTO");
            System.out.println("2. BUSCAR PRODUCTO");
            System.out.println("3. ELIMINAR PRODUCTO");
            System.out.println("4. REGISTRAR VENTA");
            System.out.println("5. GENERAR REPORTES");
            System.out.println("6. DATOS DEL ESTUDIANTE");
            System.out.println("7. BITACORA");
            System.out.println("8. SALIR");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt(); sc.nextLine();

            //switch con todas las funciones seleccionadas
            switch (opcion) {
                case 1: agregarProducto(); 
                break;
                case 2: buscarProducto(); 
                break;
                case 3: eliminarProducto(); 
                break;
                case 4: registrarVenta(); 
                break;
                case 5: generarReportes(); 
                break;
                case 6: verDatosEstudiante(); 
                break; 
                case 7: mostrarBitacora(); 
                break;
                case 8: System.out.println("Saliendo...");
                    System.out.println("FELIZ DIA :D");
                break;
                default: System.out.println("OPCION INVIALIDA, INTENTELO DE NUEVO D:");
            }
        } while (opcion != 8);
    }

    //Funciones AGREGAR PRODUCTO
    static void agregarProducto() {
        //Verificación de espacio en el inventario
        if (totalProductos >= MAX) {
            System.out.println("INVENTARIO LLENO :(");
            registrarBitacora("Agregar Producto", "Error: inventario lleno");
            return;
        }

        System.out.print("INGRESE CODIGO UNICO: ");
        String codigo = sc.nextLine();
        
        //Verifica que el código único no exista
        for (int i = 0; i < totalProductos; i++) {
            if (codigos[i].equals(codigo)) {
                System.out.println("ERROR: EL CODIGO YA EXISTE :(");
                registrarBitacora("Agregar Producto", "Error: codigo repetido");
                return;
            }
        }

        //Solicita los datos del producto
        System.out.print("NOMBRE DEL PRODUCTO: ");
        String nombre = sc.nextLine();

        System.out.print("CATEGORIA DEL PRODUCTO: ");
        String categoria = sc.nextLine();

        System.out.print("PRECIO: Q. ");
        double precio = sc.nextDouble();
        //Verifica que el precio sea positivo
        if (precio <= 0) {
            System.out.println("ERROR: EL PRECIO ES INVALIDO D:");
            sc.nextLine();
            registrarBitacora("Agregar Producto", "Error: precio invalido");
            return;
        }

        System.out.print("Cantidad en stock: ");
        int cantidad = sc.nextInt(); sc.nextLine();
        //Valida que la cantidad no sea negativa
        if (cantidad < 0) {
            System.out.println("ERROR: CANTIDAD INVALIDA D:");
            registrarBitacora("Agregar Producto", "Error: cantidad invalida");
            return;
        }

        //Guarda el producto en los productos
        codigos[totalProductos] = codigo;
        nombres[totalProductos] = nombre;
        categorias[totalProductos] = categoria;
        precios[totalProductos] = precio;
        cantidades[totalProductos] = cantidad;
        totalProductos++;//Incrementa el contador

        System.out.println("SE REGISTRO EL PRODUCTO CON EXITO :D");
        registrarBitacora("Agregar Producto", "Correcto");
    }
    
    //Función BUSQUEDA DE PRODUCTOS
    static void buscarProducto() {
        System.out.print("BUSQUEDA DEL PRODUCTO POR: (NOMBRE/CATEGORIA/CODIGO UNICO): ");
        String criterio = sc.nextLine().toLowerCase();
        boolean encontrado = false;
        
        //Recorre todos los productos buscando coincidencia
        for (int i = 0; i < totalProductos; i++) {
            if (nombres[i].toLowerCase().contains(criterio) ||
                categorias[i].toLowerCase().contains(criterio) ||
                codigos[i].toLowerCase().equals(criterio)) {
                //Muestra los datos del producto encontrado
                System.out.println("ENCONTRADO :D -> " + codigos[i] + " | " + nombres[i] +
                                   " | " + categorias[i] + " | Q." + precios[i] +
                                   " | En Stock: " + cantidades[i]);
                encontrado = true;
            }
        }

        //Informa que no encontro nada relacionado
        if (!encontrado) {
            System.out.println("NO SE ENCONTRO NINGUN PRODUCTO :C");
            registrarBitacora("Buscar Producto", "Error: no se encontro");
        } else {
            registrarBitacora("Buscar Producto", "Correcto");
        }
    }

    //FUNCIÓN ELIMINAR PRODUCTO
    static void eliminarProducto() {
        System.out.print("INGRESE CODIGO PARA ELIMINAR: ");
        String codigo = sc.nextLine();
        boolean eliminado = false;

        //Busca el producto por el código único
        for (int i = 0; i < totalProductos; i++) {
            if (codigos[i].equals(codigo)) {
                //resta los elementos para eliminar el producto
                for (int j = i; j < totalProductos - 1; j++) {
                    codigos[j] = codigos[j+1];
                    nombres[j] = nombres[j+1];
                    categorias[j] = categorias[j+1];
                    precios[j] = precios[j+1];
                    cantidades[j] = cantidades[j+1];
                }
                totalProductos--; //Reduce el contador
                eliminado = true;
                break;
            }
        }

        //Confirma si sé eliminó o no
        if (eliminado) {
            System.out.println("PRODUCTO ELIMINADO CON EXITO :D");
            registrarBitacora("Eliminar Producto", "Correcto");
        } else {
            System.out.println("NO SE ENCONTRO PRODUCTO :/");
            registrarBitacora("Eliminar Producto", "Error: no encontrado");
        }
    }

    //Registra una venta y actualiza el stock
    static void registrarVenta() {
        System.out.print("CODIGO DEL PRODUCTO: ");
        String codigo = sc.nextLine();
        boolean existe = false;

        //Busca el producto por código
        for (int i = 0; i < totalProductos; i++) {
            if (codigos[i].equals(codigo)) {
                existe = true;
                System.out.print("CANTIDAD VENDIDA: ");
                int cantidadVendida = sc.nextInt(); sc.nextLine();

                //Valida que haya suficiente stock
                if (cantidadVendida <= 0 || cantidadVendida > cantidades[i]) {
                    System.out.println("ERROR: CANTIDAD INVALIDA o STOCK INSUFICIENTE");
                    registrarBitacora("Registrar Venta", "Error: stock insuficiente");
                    return;
                }

                //Calcula el total de la venta
                double totalVenta = cantidadVendida * precios[i];
                cantidades[i] -= cantidadVendida; //Resta del stock

                //Obtiene la fecha y hora actual
                String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

                //Guarda la venta del archivo
                try (FileWriter fw = new FileWriter("ventas.txt", true);
                     PrintWriter pw = new PrintWriter(fw)) {
                    pw.println(codigos[i] + "|" + nombres[i] + "|" + cantidadVendida + "|" + totalVenta + "|" + fecha);
                } catch (IOException e) {
                    System.out.println("ERROR ESCRIBIENDO EL ARCHIVO DE VENTAS");
                }

                System.out.println("VENTA REGISTRADA! Total: Q." + totalVenta);
                registrarBitacora("Registrar Venta", "Correcto");
                return;
            }
        }

        //Función no encuentra el producto
        if (!existe) {
            System.out.println("PRODUCTO NO ENCONTRADO :(");
            registrarBitacora("Registrar Venta", "Error: producto no encontrado");
        }
    }

   //Genera reportes en PDF del stock y ventas
static void generarReportes() {
    //Crea un nombre único para los archivos 
    String fechaArchivo = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());

    //Genera reporte de stock en PDF real
    try {
        String nombreArchivoStock = fechaArchivo + "_Stock.pdf";
        PdfWriter writerStock = new PdfWriter(nombreArchivoStock);
        PdfDocument pdfDocStock = new PdfDocument(writerStock);
        Document documentStock = new Document(pdfDocStock);
        
        // Título del reporte
        documentStock.add(new Paragraph("REPORTE DE STOCK").setBold().setFontSize(16));
        documentStock.add(new Paragraph(" ")); // Línea en blanco
        
        // Agregar cada producto
        for (int i = 0; i < totalProductos; i++) {
            String lineaProducto = codigos[i] + " | " + nombres[i] + " | " + categorias[i] +
                           " | Q." + precios[i] + " | Stock: " + cantidades[i];
            documentStock.add(new Paragraph(lineaProducto));
        }
        
        documentStock.close();
        System.out.println("Reporte de stock generado: " + nombreArchivoStock);
        
    } catch (Exception e) {
        System.out.println("Error generando reporte de stock: " + e.getMessage());
    }

    //Genera reporte de ventas en PDF real
    try {
        String nombreArchivoVentas = fechaArchivo + "_Ventas.pdf";
        PdfWriter writerVentas = new PdfWriter(nombreArchivoVentas);
        PdfDocument pdfDocVentas = new PdfDocument(writerVentas);
        Document documentVentas = new Document(pdfDocVentas);
        
        // Título del reporte
        documentVentas.add(new Paragraph("REPORTE DE VENTAS").setBold().setFontSize(16));
        documentVentas.add(new Paragraph(" ")); // Línea en blanco
        
        // Leer archivo de ventas y agregar cada línea
        try (BufferedReader br = new BufferedReader(new FileReader("ventas.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) { // Solo agregar líneas que no estén vacías
                    documentVentas.add(new Paragraph(linea));
                }
            }
        } catch (FileNotFoundException e) {
            documentVentas.add(new Paragraph("No hay ventas registradas"));
        }
        
        documentVentas.close();
        System.out.println("Reporte de ventas generado: " + nombreArchivoVentas);
        
    } catch (Exception e) {
        System.out.println("Error generando reporte de ventas: " + e.getMessage());
    }

    System.out.println("REPORTES GENERADOS :D");
    registrarBitacora("Generar Reportes", "Correcto");
}

    //Muesta los datos del Estudiante
    static void verDatosEstudiante() {
        System.out.println("\nDatos del Estudiante");
        System.out.println("Nombre: Jose Roberto Orozco Orozco");
        System.out.println("ID: 202200030");
        System.out.println("Curso: Introduccion a la Programacion y Computacion 1");
        System.out.println("Proyecto 1, 2do Semetre 2025, ING-USAC");
    }

    //REGISTRO de la acción en la bitácora
    static void registrarBitacora(String accion, String resultado) {
        //Verifica que haya espacio en la bitácora
        if (totalAcciones < bitacora.length) {
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            bitacora[totalAcciones] = fecha + " | " + accion + " | " + resultado + " | Usuario: Jose Orozco";
            totalAcciones++; //Incrementa el contador
        }
    }

    //MUESTRA la bitácora
    static void mostrarBitacora() {
        System.out.println("\n BITACORA");
        for (int i = 0; i < totalAcciones; i++) {
            System.out.println(bitacora[i]);
        }
        registrarBitacora("Ver Bitacora", "Correcto");
    }
}
