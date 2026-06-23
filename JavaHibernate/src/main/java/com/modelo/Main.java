package com.modelo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.configure(); 

        try (SessionFactory sessionFactory = config.buildSessionFactory();
             Scanner teclado = new Scanner(System.in)) {
            
            int opcion = 0;

            while (opcion != 5) {
                System.out.println("\n====================================================");
                System.out.println("      Compartir ");
                System.out.println("      MENU INTERACTIVO");
                System.out.println("====================================================");
                System.out.println("1. Verificar conexión y mapeo inicial (Datos del CSV)");
                System.out.println("2. Insertar nuevo registro relacional (Simular Carga)");
                System.out.println("3. Intentar duplicar periodo único (Test Unique Constraint)");
                System.out.println("4. Eliminar registro en Cascada (Test ON DELETE CASCADE)");
                System.out.println("5. Salir del programa");
                System.out.print("Seleccione una opción: ");
                
                try {
                    opcion = Integer.parseInt(teclado.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido.");
                    continue;
                }

                System.out.println("----------------------------------------------------\n");

                switch (opcion) {
                    case 1:
                        System.out.println("🔹 OPCIÓN 1: Verificando mapeo con datos reales cargados por el ETL...");
                        try (Session session = sessionFactory.openSession()) {
                            Provincia provExistente = session.find(Provincia.class, 1);
                            if (provExistente != null) {
                                System.out.println("Mapeo Exitoso");
                                System.out.println("-> Conectado a la tabla original 'Provincia'.");
                                System.out.println("-> Registro recuperado: " + provExistente.getNombreProvincia());
                            } else {
                                System.out.println("Conexión establecida pero no se encontraron registros con ID 1.");
                            }
                        }
                        break;

                    case 2:
                        System.out.println("🔹 OPCIÓN 2: Insertando registro relacional usando Objetos Java...");
                        try (Session session = sessionFactory.openSession()) {
                            Transaction txInsert = session.beginTransaction();
                            try {
                                Provincia nuevaProvincia = session.createQuery(
                                    "from Provincia where nombreProvincia = :nombre", Provincia.class)
                                    .setParameter("nombre", "Provincia de Demo ORM")
                                    .uniqueResult();

                                if (nuevaProvincia == null) 
                                {
                                    nuevaProvincia = new Provincia("Provincia de Demo ORM");
                                    session.persist(nuevaProvincia);
                                }

                                Tiempo tiempoId1 = session.find(Tiempo.class, 1);

                                AccesoInternet nuevoAcceso = new AccesoInternet();
                                nuevoAcceso.setProvincia(nuevaProvincia);
                                nuevoAcceso.setTiempo(tiempoId1);
                                nuevoAcceso.setBandaAnchaFija(9999);
                                nuevoAcceso.setDialUp(123);
                                nuevoAcceso.setTotal(10122);

                                session.persist(nuevoAcceso);
                                txInsert.commit();
                                
                                System.out.println("Persistencia Exitosa");
                            } catch (Exception e) {
                                if (txInsert != null) txInsert.rollback();
                                System.out.println("Error al insertar: " + e.getMessage());
                            }
                        }
                        break;

                    case 3:
                        System.out.println("🔹 OPCIÓN 3: Testeando Restricción Única Compuesta (Año y Trimestre)...");
                        System.out.println("Intentando insertar en 'Tiempo' el año 2022 y trimestre 1 (Ya existente)...");
                        try (Session session = sessionFactory.openSession()) {
                            Transaction txError = session.beginTransaction();
                            try {
                                Tiempo tiempoDuplicado = new Tiempo(2022, 1);
                                session.persist(tiempoDuplicado);
                                txError.commit();
                                System.out.println("Alerta: Se guardó el registro.");
                            } catch (Exception e) {
                                if (txError != null) txError.rollback();
                                System.out.println("Acción Bloqueada de forma correcta");
                                System.out.println("-> Causa: Violación de la restricción de unicidad compuesta [@UniqueConstraint].");
                            }
                        }
                        break;

                    case 4:
                        System.out.println("🔹 OPCIÓN 4: Testeando Eliminación en Cascada (ON DELETE CASCADE)...");
                        try (Session session = sessionFactory.openSession()) {
                            Transaction txDelete = session.beginTransaction();
                            try {
                                Provincia provABorrar = session.createQuery(
                                    "from Provincia where nombreProvincia = :nombre", Provincia.class)
                                    .setParameter("nombre", "Provincia de Demo ORM")
                                    .uniqueResult();

                                if (provABorrar != null) {
                                    Integer idBorrado = provABorrar.getIdProvincia();
                                    session.remove(provABorrar);
                                    txDelete.commit();
                                    System.out.println("Borrado Exitoso");
                                    System.out.println("-> Se eliminó de forma directa la Provincia ID: " + idBorrado);
                                } else {
                                    System.out.println("No se encontró la 'Provincia de Demo ORM' en la BD. Ejecute la Opción 2 primero.");
                                    txDelete.rollback();
                                }
                            } catch (Exception e) {
                                if (txDelete != null) txDelete.rollback();
                                System.out.println("Error al borrar: " + e.getMessage());
                            }
                        }
                        break;

                    case 5:
                        System.out.println("Saliendo de la aplicación");
                        break;

                    default:
                        System.out.println("Opción inválida. Elija un número del 1 al 5.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}