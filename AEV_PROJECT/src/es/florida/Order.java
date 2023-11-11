package es.florida;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

public class Order extends JFrame {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> piecesToManufacture;
    private JCheckBox logToFileCheckbox;
    private JTextField logFilenameField;
    private JTextField typeField; // Movido a ser un atributo de clase
    private JTextField quantityField; // Movido a ser un atributo de clase

    /**
     *
     */
    public Order() {
        // Inicialización del mapa para almacenar las piezas a fabricar
        piecesToManufacture = new HashMap<>();

        // Configuración de la ventana principal
        setTitle("Fabricación de Tetrominos");
        setLayout(null);
        
        JLabel typeLabel = new JLabel("Tipo de pieza:");
        typeLabel.setBounds(20, 20, 100, 20);
        add(typeLabel);

        typeField = new JTextField();
        typeField.setBounds(130, 20, 150, 20);
        add(typeField);

        JLabel quantityLabel = new JLabel("Cantidad:");
        quantityLabel.setBounds(20, 50, 100, 20);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(130, 50, 150, 20);
        add(quantityField);
        
        // Creación de botón para agregar piezas
        JButton addButton = new JButton("Agregar pieza");
        addButton.setBounds(50, 80, 150, 30);
        add(addButton);

        // Creación de botón para iniciar la fabricación
        JButton manufactureButton = new JButton("Iniciar fabricación");
        manufactureButton.setBounds(50, 120, 200, 30);
        add(manufactureButton);

        // Configuración de la casilla de verificación para el registro en archivo
        logToFileCheckbox = new JCheckBox("Guardar registro en un archivo");
        logToFileCheckbox.setBounds(20, 160, 200, 20);
        add(logToFileCheckbox);

        // Configuración del campo de texto para el nombre del archivo de registro
        logFilenameField = new JTextField("LOG_" + System.currentTimeMillis() + ".txt");
        logFilenameField.setBounds(20, 190, 200, 20);
        add(logFilenameField);

        // Asignación del ActionListener al botón 'Agregar pieza'
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPieceToManufacture();
            }
        });

        // Asignación del ActionListener al botón 'Iniciar fabricación'
        manufactureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startManufacturing();
            }
        });

        // Configuración de la ventana principal
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Método para agregar piezas al mapa de piezas a fabricar

    /**
     * Método para añadir nuevas piezas a fabricar
     */
    private void addPieceToManufacture() {
        String type = typeField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        piecesToManufacture.put(type, quantity);

        // Limpiar los campos después de agregar la pieza
        typeField.setText("");
        quantityField.setText("");
    }

    /**
     * Método para iniciar la fabricación al llamar a Manufacture
     */
    private void startManufacturing() {
        boolean logToFile = logToFileCheckbox.isSelected();
        String logFilename = logFilenameField.getText();
        Manufacture fabricator = new Manufacture(piecesToManufacture, logToFile, logFilename);
        fabricator.startManufacturing();
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Order();
        });
    }
}