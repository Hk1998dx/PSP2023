package es.florida;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.text.SimpleDateFormat;

public class Manufacture {
    private HashMap<String, Integer> piecesToManufacture;
    private boolean logToFile;
    private Queue<String> pieceQueue;
    private LinkedList<String> finishedPieces;
    /**
     * Método para inicializar parámetros
     * @param piecesToManufacture
     * @param logFilename // Nombre del archivo LOG generado
     */
    public Manufacture(HashMap<String, Integer> piecesToManufacture, boolean logToFile, String logFilename) {
        this.piecesToManufacture = piecesToManufacture;
        this.logToFile = logToFile;
        this.pieceQueue = new LinkedList<>();
        this.finishedPieces = new LinkedList<>();
    }

    /**
     * Método para llenar la cola de piezas a fabricar
     */
    public void startManufacturing() {
        int machineCount = 8;

        for (String pieceType : piecesToManufacture.keySet()) {
            int quantity = piecesToManufacture.get(pieceType);

            for (int i = 0; i < quantity; i++) {
                pieceQueue.offer(pieceType);
            }
        }
        // Crear hilos (máquinas) para fabricar las piezas
        Thread[] machines = new Thread[machineCount];
        for (int i = 0; i < machineCount; i++) {
            machines[i] = new Thread(() -> {
                while (true) {
                    String pieceType;
                    synchronized (pieceQueue) {
                        pieceType = pieceQueue.poll();
                    }
                    if (pieceType == null) {
                        break; // No hay piezas para manufacture
                    }
                    int timeToManufacture = getTimeToManufacture(pieceType);
                    String pieceTimestamp = pieceType + "_" + getCurrentTimestamp();
                    System.out.println("Fabricando " + pieceTimestamp);
                    try {
                        Thread.sleep(timeToManufacture);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(pieceTimestamp + " fabricado");
                    synchronized (finishedPieces) {
                        finishedPieces.add(pieceTimestamp);
                    }
                }
            });
            machines[i].start();
        }
        // Esperar a que todos los hilos (máquinas) terminen
        for (int i = 0; i < machineCount; i++) {
            try {
                machines[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Escribir el registro en un archivo si está habilitado
        if (logToFile) {
            writeLogToFile();
        }
    }

    /**
     * getTimeToManufacture // Método para obtener el tiempo de fabricación en función del tipo de pieza.
     * @param pieceType // Tipo de pieza seleccionada.
     * @return timeToManufacture // Variable en la que se almacena el tiempo de fabricación en milisegundos.
     */
    private int getTimeToManufacture(String pieceType) {
    	 int timeToManufacture;
    	    switch (pieceType) {
                case "I":
                    timeToManufacture = 1000;
                    break;
                case "O":
                    timeToManufacture = 2000;
                    break;
                case "T":
                    timeToManufacture = 3000;
                    break;
                case "J":
                    timeToManufacture = 4000;
                    break;
                case "L":
                    timeToManufacture = 4000;
                    break;
                case "S":
                    timeToManufacture = 5000;
                    break;
                case "Z":
                    timeToManufacture = 5000;
                    break;
                default:
                    // Manejar un tipo de pieza desconocido, si es necesario
                    timeToManufacture = 0;
                    break;
    	    }
    	    return timeToManufacture;
    }

    /**
     * Método para obtener la marca de tiempo actual
     * @return // Marca de tiempo actual
     */
    private String getCurrentTimestamp() {
    	 Date currentDate = new Date();
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    	    String timestamp = dateFormat.format(currentDate);
    	    return timestamp;
    }

    /**
     * Método para escribir el registro en un archivo
     */
    private void writeLogToFile() {
    	 try {
             // Obtener el nombre del archivo de registro
             String filename = "LOG_" + getCurrentTimestamp() + ".txt";
             // Crear un FileWriter para escribir en el archivo
             FileWriter writer = new FileWriter(filename);
             // Escribir cada pieza fabricada en el archivo
             synchronized (finishedPieces) {
                 for (String piece : finishedPieces) {
                     writer.write(piece + "\n");
                 }
             }
             // Cerrar el FileWriter
             writer.close();
             System.out.println("Registro escrito en el archivo: " + filename);
         } catch (IOException e) {
             // Manejar la excepción en caso de error de escritura
             e.printStackTrace();
         }
     }
}