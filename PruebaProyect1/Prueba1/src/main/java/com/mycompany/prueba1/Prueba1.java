package com.mycompany.prueba1;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Prueba1 {
    // Definir tamaño maximo del inventario
    static final int MAX = 100;

    // Estructuras de datos (vectores)
    static String[] nombres = new String[MAX];
    static String[] categorias = new String[MAX];
    static double[] precios = new double[MAX];
    static int[] cantidades = new int[MAX];
    static String[] codigos = new String[MAX];
    static int totalProductos = 0;

    // Bitacora temporal
    static String[] bitacora = new String[200];
    static int totalAcciones = 0;

    // Scanner global
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n===== MENU DE INVENTARIO =====");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Registrar Venta");
            System.out.println("5. Generar Reportes");
            System.out.println("6. Ver Datos del Estudiante");
            System.out.println("7. Ver Bitacora");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1: agregarProducto(); break;
                case 2: buscarProducto(); break;
                case 3: eliminarProducto(); break;
                case 4: registrarVenta(); break;
                case 5: generarReportes(); break;
                case 6: verDatosEstudiante(); break; // No se guarda en bitacora
                case 7: mostrarBitacora(); break;
                case 8: System.out.println("Saliendo..."); break;
                default: System.out.println("Opcion invalida.");
            }
        } while (opcion != 8);
    }

    // ----------------- METODOS -----------------

    static void agregarProducto() {
        if (totalProductos >= MAX) {
            System.out.println("Inventario lleno.");
            registrarBitacora("Agregar Producto", "Error: inventario lleno");
            return;
        }

        System.out.print("Codigo unico: ");
        String codigo = sc.nextLine();

        // Validar codigo unico
        for (int i = 0; i < totalProductos; i++) {
            if (codigos[i].equals(codigo)) {
                System.out.println("Error: el codigo ya existe.");
                registrarBitacora("Agregar Producto", "Error: codigo repetido");
                return;
            }
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Categoria: ");
        String categoria = sc.nextLine();

        System.out.print("Precio: ");
        double precio = sc.nextDouble();
        if (precio <= 0) {
            System.out.println("Error: precio invalido.");
            sc.nextLine();
            registrarBitacora("Agregar Producto", "Error: precio invalido");
            return;
        }

        System.out.print("Cantidad en stock: ");
        int cantidad = sc.nextInt(); sc.nextLine();
        if (cantidad < 0) {
            System.out.println("Error: cantidad invalida.");
            registrarBitacora("Agregar Producto", "Error: cantidad invalida");
            return;
        }

        // Guardar en vectores
        codigos[totalProductos] = codigo;
        nombres[totalProductos] = nombre;
        categorias[totalProductos] = categoria;
        precios[totalProductos] = precio;
        cantidades[totalProductos] = cantidad;
        totalProductos++;

        System.out.println("Producto agregado con exito.");
        registrarBitacora("Agregar Producto", "Correcto");
    }

    static void buscarProducto() {
        System.out.print("Buscar por (nombre/categoria/codigo): ");
        String criterio = sc.nextLine().toLowerCase();
        boolean encontrado = false;

        for (int i = 0; i < totalProductos; i++) {
            if (nombres[i].toLowerCase().contains(criterio) ||
                categorias[i].toLowerCase().contains(criterio) ||
                codigos[i].toLowerCase().equals(criterio)) {
                System.out.println("Encontrado -> " + codigos[i] + " | " + nombres[i] +
                                   " | " + categorias[i] + " | $" + precios[i] +
                                   " | Stock: " + cantidades[i]);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontro ningun producto.");
            registrarBitacora("Buscar Producto", "Error: no encontrado");
        } else {
            registrarBitacora("Buscar Producto", "Correcto");
        }
    }

    static void eliminarProducto() {
        System.out.print("Ingrese codigo de producto a eliminar: ");
        String codigo = sc.nextLine();
        boolean eliminado = false;

        for (int i = 0; i < totalProductos; i++) {
            if (codigos[i].equals(codigo)) {
                for (int j = i; j < totalProductos - 1; j++) {
                    codigos[j] = codigos[j+1];
                    nombres[j] = nombres[j+1];
                    categorias[j] = categorias[j+1];
                    precios[j] = precios[j+1];
                    cantidades[j] = cantidades[j+1];
                }
                totalProductos--;
                eliminado = true;
                break;
            }
        }

        if (eliminado) {
            System.out.println("Producto eliminado.");
            registrarBitacora("Eliminar Producto", "Correcto");
        } else {
            System.out.println("Producto no encontrado.");
            registrarBitacora("Eliminar Producto", "Error: no encontrado");
        }
    }

    static void registrarVenta() {
        System.out.print("Codigo del producto: ");
        String codigo = sc.nextLine();
        boolean existe = false;

        for (int i = 0; i < totalProductos; i++) {
            if (codigos[i].equals(codigo)) {
                existe = true;
                System.out.print("Cantidad vendida: ");
                int cantidadVendida = sc.nextInt(); sc.nextLine();

                if (cantidadVendida <= 0 || cantidadVendida > cantidades[i]) {
                    System.out.println("Error: cantidad invalida o stock insuficiente.");
                    registrarBitacora("Registrar Venta", "Error: stock insuficiente");
                    return;
                }

                double totalVenta = cantidadVendida * precios[i];
                cantidades[i] -= cantidadVendida;

                // Fecha automatica
                String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

                // Guardar en archivo de ventas
                try (FileWriter fw = new FileWriter("ventas.txt", true);
                     PrintWriter pw = new PrintWriter(fw)) {
                    pw.println(codigos[i] + "," + nombres[i] + "," + cantidadVendida + "," + totalVenta + "," + fecha);
                } catch (IOException e) {
                    System.out.println("Error escribiendo archivo de ventas.");
                }

                System.out.println("Venta registrada. Total: $" + totalVenta);
                registrarBitacora("Registrar Venta", "Correcto");
                return;
            }
        }

        if (!existe) {
            System.out.println("Producto no encontrado.");
            registrarBitacora("Registrar Venta", "Error: producto no encontrado");
        }
    }

    static void generarReportes() {
        // Fecha automatica para los nombres de archivos
        String fechaArchivo = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());

        // Reporte de inventario (txt simulando PDF)
        try (PrintWriter pw = new PrintWriter(new FileWriter(fechaArchivo + "_Stock.pdf"))) {
            pw.println("=== Reporte de Stock ===");
            for (int i = 0; i < totalProductos; i++) {
                pw.println(codigos[i] + " | " + nombres[i] + " | " + categorias[i] +
                           " | $" + precios[i] + " | Stock: " + cantidades[i]);
            }
        } catch (IOException e) {
            System.out.println("Error generando reporte de stock.");
        }

        // Reporte de ventas
        try (BufferedReader br = new BufferedReader(new FileReader("ventas.txt"));
             PrintWriter pw = new PrintWriter(new FileWriter(fechaArchivo + "_Ventas.pdf"))) {
            pw.println("=== Reporte de Ventas ===");
            String linea;
            while ((linea = br.readLine()) != null) {
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error generando reporte de ventas.");
        }

        System.out.println("Reportes generados.");
        registrarBitacora("Generar Reportes", "Correcto");
    }

    static void verDatosEstudiante() {
        System.out.println("\n=== Datos del Estudiante ===");
        System.out.println("Nombre: Jose Roberto Orozco Orozco");
        System.out.println("ID: 202200030");
        System.out.println("Curso: Introduccion a la Programacion y Computacion 1");
        // No se registra en la bitacora
    }

    static void registrarBitacora(String accion, String resultado) {
        if (totalAcciones < bitacora.length) {
            // Fecha automatica
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            bitacora[totalAcciones] = fecha + " | " + accion + " | " + resultado + " | Usuario: Jose Roberto Orozco";
            totalAcciones++;
        }
    }

    static void mostrarBitacora() {
        System.out.println("\n=== Bitacora de Acciones ===");
        for (int i = 0; i < totalAcciones; i++) {
            System.out.println(bitacora[i]);
        }
        registrarBitacora("Ver Bitacora", "Correcto");
    }
}
