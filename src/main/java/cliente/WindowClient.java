package cliente;

import connection.Message;
import connection.TransactionType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase que permite crear la ventana gráfica empleando Javax Swing, Extiende de JFrame para ser usado como contenedor
 * principal y heredar todos los métodos para implementación de interfaz gráfica.
 * En esta clase se genera una primera ventana de tipo dialog para establecer la configuración de conexión con el
 * servidor socket estableciendo el Host o Ip y el número puerto de conexión del servidor. Cuando se es aceptada la
 * configuración, se genera una segunda ventana de tipo JFrame en la cual se visualizan los clientes conectados y los
 * mensajes enviados y recibidos, además de encontrar la opción de enviar mensajes seleccionando alguno de los clientes
 * conectados. El uso de eventos permite la actualización en segundo plano de la lista de clientes a medida que se
 * conectan o desconectan. La lista es visualizada en un JComboBox. El uso de eventos generados en la clase Client
 * permiten la actualización en segundo plano del TextArea en el que se visualizan los mensajes enviados y recibidos
 *
 * @Author Jorge Luis Velasquez
 */
public class WindowClient extends JFrame {
    /**
     * PanelMain: Panel principal de tipo JPanel en el cual se encuentran contenidos los demás elementos de la venta
     */
    private JPanel panelMain;

    /**
     * Destinies: lista de destinatarios conectados al servidor. Se visualiza en un JComboBox (lista desplegable), la
     * cual se actualiza en segundo plano gracias a los eventos creados para los atributos de la clase Client
     */
    private JComboBox listTransactionsPanelTransaction;

    /**
     * Console: Área multi línea de tipo JTextArea que permite visualizar los mensajes recibidos y enviados. Se
     * actualiza en segundo plano gracias a los eventos creados para los atributos de la clase Client
     */
    private JTextArea console;

    /**
     * Message: entrada de texto de tipo JTextField, permite el ingreso del mensaje que se desea enviar a otro
     * cliente conectado al servidor
     */
    private JTextField firstNameEmployedPanelEmployed;

    private JFormattedTextField idEmployedPanelEmployed;

    /**
     * SendMessage: Botón del tipo JButton. Permite ejecutar el método de envío de mensajes de la clase Client
     * empleando el evento del click del mouse sobre el botón
     */
    private JButton sendTransaction;
    private JTextField nameCountryPanelCountry;
    private JTextField nameCityPanelCity;
    private JComboBox listCountriesPanelCity;
    private JTextField addressPanelLocalization;
    private JComboBox listCityPanelLocalization;
    private JTextField nameDepartmentPanelDepartment;
    private JComboBox listLocalizationPanelDepartment;
    private JTextField namePositionPanelPosition;
    private JFormattedTextField minimumSalaryPanelPosition;
    private JFormattedTextField maximumSalaryPanelPosition;
    private JPanel panelCountry;
    private JPanel panelCity;
    private JPanel panelLocalization;
    private JPanel panelDepartment;
    private JPanel panelPosition;
    private JPanel panelEmployed;
    private JPanel panelTransactions;
    private JTextField surnameEmployedPanelEmployed;
    private JTextField emailEmployedPanelEmployed;
    private JTextField birthdateEmployedPanelEmployed;
    private JTextField salaryEmployedPanelEmployed;
    private JTextField commissionEmployedPanelEmployed;
    private JComboBox listPositionsPanelEmployed;
    private JComboBox employedDepartmentPanelEmployed;
    private JTable tableEmployees;
    private JScrollPane scrollPanelTableEmployees;
    private JPanel panelTableEmployees;
    private JTextField secondNameEmployedPanelEmployed;
    private JTextField secondSurnameEmployedPanelEmployed;
    private JComboBox managerEmployedPanelEmployed;
    private JFormattedTextField newIdEmployedPanelEmployed;
    private JFormattedTextField idCountryPanelCountry;
    private JFormattedTextField idCityPanelCity;
    private JFormattedTextField idLocalizationPanelLocalization;
    private JFormattedTextField idDepartmentPanelDepartment;
    private JFormattedTextField idPositionPanelPosition;
    private JComboBox listDepartmentsPanelLocalization;
    private JTextField retirementDateEmployedPanelEmployed;

    private ArrayList<JPanel> panels;

    private ArrayList<ArrayList<String>> listFieldsEmployeesActive;

    private ArrayList<ArrayList<String>> listFieldsEmployeesRetirement;


    /**
     * IP_ADDRESS: dirección ip del servidor que se establece por defecto cuando se crea una instancia de la clase
     * cliente sin asignar el valor
     */
    private final String IP_ADDRESS = "127.0.0.1";

    /**
     * PORT: puerto por defecto para conexión con el servidor
     */
    private final int PORT = 2022;

    /**
     * Client: instancia del tipo Client la cual permite crear un cliente para comunicación con sockets
     */
    private Client client;

    private Message message;

    /**
     * Constructor de la clase WindowClient. Crea los objetos para visualización gráfica de ventana de configuración,
     * objeto de la clase Client para establecer la conexión con el servidor, ventana principal de interacción de
     * cliente con el chat y los eventos necesarios para la actualización en segundo plano de lista de destinatarios
     * (clientes activos) y la consola de mensajes enviados y recibidos, además el evento del mouse al hacer click, se
     * realizan estas acciones a través de métodos
     */
    public WindowClient() {
        super("Cliente Transacciones SQL Con Sockets");
        setUp();
        this.message = new Message();
        setContentPane(panelMain);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        eventSendStatement();
        eventPrintMessage();
        eventLoadPositions();
        eventLoadDepartments();
        eventLoadCities();
        eventLoadCountries();
        eventLoadLocalizations();
        eventLoadManagers();
        eventLoadEmployees();
        eventLoadEmployed();
        eventClose();
        eventItemTransactionSelected();
        panels = new ArrayList<>(Arrays.asList(panelEmployed, panelTableEmployees, panelCountry, panelCity, panelLocalization, panelDepartment, panelPosition));
        panelsNotVisible();
        panelEmployed.setVisible(true);
        listFieldsEmployeesActive = new ArrayList<>();
        listFieldsEmployeesRetirement = new ArrayList<>();
    }

    private void loadingPositions() {
        Message message = new Message(TransactionType.SELECT_POSITIONS);
        client.sendStatement(message);
    }

    private void loadingDepartments() {
        Message message = new Message(TransactionType.SELECT_DEPARTMENTS);
        client.sendStatement(message);
    }

    private void loadingCities() {
        Message message = new Message(TransactionType.SELECT_CITIES);
        client.sendStatement(message);
    }

    private void loadingCountries() {
        Message message = new Message(TransactionType.SELECT_COUNTRIES);
        client.sendStatement(message);
    }

    private void loadingLocalizations() {
        Message message = new Message(TransactionType.SELECT_LOCALIZATIONS);
        client.sendStatement(message);
    }

    private void loadingManagers() {
        Message message = new Message(TransactionType.SELECT_MANAGERS);
        client.sendStatement(message);
    }

    /**
     * Método que permite crear la ventana de configuración para la conexión
     */
    private void setUp() {
        try {
            this.client = new Client(IP_ADDRESS, PORT, "ManagerSQL");
            loadingPositions();
            loadingDepartments();
            loadingManagers();
        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Método para manejar el evento del click del mouse sobre el botón de enviar mensaje
     */

    private void eventSendStatement() {
        sendTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> fieldsEmployed = new ArrayList<>();

                selectStatementToSend(fieldsEmployed);

                client.sendStatement(new Message(message));

                if (!client.isConnected()) {
                    System.exit(0);
                }
            }
        });
    }

    private void selectStatementToSend(ArrayList<String> fieldsEmployed) {
        switch (message.getType()) {
            case INSERT_EMPLOYED:
                loadListFieldsEmployed(fieldsEmployed, managerEmployedPanelEmployed);
                fieldsEmployed.add((String) employedDepartmentPanelEmployed.getSelectedItem());
                message.setEmployed(fieldsEmployed);
                break;
            case UPDATE_EMPLOYED:
                fieldsEmployed.add((String) managerEmployedPanelEmployed.getSelectedItem());
                loadListFieldsEmployed(fieldsEmployed, employedDepartmentPanelEmployed);
                fieldsEmployed.add(newIdEmployedPanelEmployed.getText());
                message.setEmployed(fieldsEmployed);
                break;
            case DELETE_EMPLOYED:
                message.setIdEmployed((Integer) idEmployedPanelEmployed.getValue());
                message.setRetirementDate(retirementDateEmployedPanelEmployed.getText());
                break;
            case SELECT_ONE_EMPLOYED:
                message.setIdEmployed((Integer) idEmployedPanelEmployed.getValue());
                break;
            case INSERT_COUNTRY:
                message.setFieldsCountry(new ArrayList<String>(
                        Arrays.asList(String.valueOf(idCountryPanelCountry.getValue()), nameCountryPanelCountry.getText()))
                );
                idCountryPanelCountry.setValue(0);
                nameCountryPanelCountry.setText("");
                break;
            case INSERT_CITY:
                message.setFieldsCity(new ArrayList<String>(
                        Arrays.asList(String.valueOf(idCityPanelCity.getValue()),
                                (String) listCountriesPanelCity.getSelectedItem(),
                                nameCityPanelCity.getText()))
                );
                idCityPanelCity.setValue(0);
                nameCityPanelCity.setText("");
                break;
            case INSERT_LOCALIZATION:
                message.setFieldsLocalization(new ArrayList<String>(
                        Arrays.asList(
                                String.valueOf(idLocalizationPanelLocalization.getValue()),
                                (String) listCityPanelLocalization.getSelectedItem(),
                                (String) listDepartmentsPanelLocalization.getSelectedItem(),
                                addressPanelLocalization.getText()
                        ))
                );
                idLocalizationPanelLocalization.setValue(0);
                addressPanelLocalization.setText("");
                break;
            case INSERT_DEPARTMENT:
                message.setFieldsDepartment(new ArrayList<String>(
                        Arrays.asList(
                                String.valueOf(idDepartmentPanelDepartment.getValue()),
                                nameDepartmentPanelDepartment.getText()
                        )
                ));
                idDepartmentPanelDepartment.setValue(0);
                nameDepartmentPanelDepartment.setText("");
                break;
            case INSERT_POSITION:
                message.setFieldsPosition(new ArrayList<String>(
                        Arrays.asList(
                                String.valueOf(idPositionPanelPosition.getValue()),
                                namePositionPanelPosition.getText(),
                                String.valueOf(minimumSalaryPanelPosition.getValue()),
                                String.valueOf(maximumSalaryPanelPosition.getValue())
                        ))
                );
                idPositionPanelPosition.setValue(0);
                namePositionPanelPosition.setText("");
                minimumSalaryPanelPosition.setValue(0);
                maximumSalaryPanelPosition.setValue(0);
                break;
        }
    }


    private void loadListFieldsEmployed(ArrayList<String> fieldsEmployed, JComboBox comboBoxAlterno) {
        fieldsEmployed.add(String.valueOf(idEmployedPanelEmployed.getValue()));
        fieldsEmployed.add(firstNameEmployedPanelEmployed.getText());
        fieldsEmployed.add(secondNameEmployedPanelEmployed.getText());
        fieldsEmployed.add(surnameEmployedPanelEmployed.getText());
        fieldsEmployed.add(secondSurnameEmployedPanelEmployed.getText());
        fieldsEmployed.add(emailEmployedPanelEmployed.getText());
        fieldsEmployed.add(birthdateEmployedPanelEmployed.getText());
        fieldsEmployed.add(salaryEmployedPanelEmployed.getText());
        fieldsEmployed.add(commissionEmployedPanelEmployed.getText());
        fieldsEmployed.add((String) listPositionsPanelEmployed.getSelectedItem());
        fieldsEmployed.add((String) comboBoxAlterno.getSelectedItem());
    }

    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se recibe un mensaje y asi
     * actualizar el área multi línea donde se visualizan los mensajes enviados y recibidos en segundo plano
     */
    public void eventPrintMessage() {
        EventChangeClientListener clientPrintMessage = new EventChangeClientListener() {
            @Override
            void onMessageChange(EventChangeClient event) {
                console.setText("**** Mensaje recibido ****\n" + client.getMessage());
            }
        };
        client.addEventListener(clientPrintMessage);
    }

    public void eventLoadPositions() {
        EventChangeClientListener loadPositions = new EventChangeClientListener() {
            @Override
            void onPositions(EventChangeClient event) {
                listPositionsPanelEmployed.removeAllItems();
                client.getPositions().forEach(listPositionsPanelEmployed::addItem);
            }
        };
        client.addEventListener(loadPositions);
    }

    public void eventLoadDepartments() {
        EventChangeClientListener loadDepartments = new EventChangeClientListener() {
            @Override
            void onDepartments(EventChangeClient event) {
                employedDepartmentPanelEmployed.removeAllItems();
                listDepartmentsPanelLocalization.removeAllItems();
                client.getDepartments().forEach(department -> {
                    employedDepartmentPanelEmployed.addItem(department);
                    listDepartmentsPanelLocalization.addItem(department);
                });
            }
        };
        client.addEventListener(loadDepartments);
    }

    public void eventLoadCities() {
        EventChangeClientListener loadCities = new EventChangeClientListener() {
            @Override
            void onCities(EventChangeClient event) {
                listCityPanelLocalization.removeAllItems();
                client.getCities().forEach(listCityPanelLocalization::addItem);
            }
        };
        client.addEventListener(loadCities);
    }

    public void eventLoadCountries() {
        EventChangeClientListener loadCountries = new EventChangeClientListener() {
            @Override
            void onCountries(EventChangeClient event) {
                listCountriesPanelCity.removeAllItems();
                client.getCountries().forEach(listCountriesPanelCity::addItem);
            }
        };
        client.addEventListener(loadCountries);
    }

    public void eventLoadLocalizations() {
        EventChangeClientListener loadLocalizations = new EventChangeClientListener() {
            @Override
            void onLocalizations(EventChangeClient event) {
                listLocalizationPanelDepartment.removeAllItems();
                client.getLocalizations().forEach(listLocalizationPanelDepartment::addItem);
            }
        };
        client.addEventListener(loadLocalizations);
    }

    public void eventLoadEmployees() {
        EventChangeClientListener loadEmployees = new EventChangeClientListener() {
            @Override
            void onEmployees(EventChangeClient event) {
                if (client.getEmployedStatus().equals("Activo")) {
                    listFieldsEmployeesActive = client.getEmployees();
                    createTable(listFieldsEmployeesActive);
                }else {
                    listFieldsEmployeesRetirement = client.getEmployees();
                    createTable(listFieldsEmployeesRetirement);
                }
            }
        };
        client.addEventListener(loadEmployees);
    }

    public void eventLoadEmployed() {
        EventChangeClientListener loadEmployed = new EventChangeClientListener() {
            @Override
            void onEmployed(EventChangeClient event) {
                ArrayList<String> fieldsEmployed = client.getEmployed();
                if (!fieldsEmployed.isEmpty()) {
                    idEmployedPanelEmployed.setText(fieldsEmployed.get(0));
                    newIdEmployedPanelEmployed.setText(fieldsEmployed.get(0));
                    firstNameEmployedPanelEmployed.setText(fieldsEmployed.get(1));
                    secondNameEmployedPanelEmployed.setText(fieldsEmployed.get(2));
                    surnameEmployedPanelEmployed.setText(fieldsEmployed.get(3));
                    secondSurnameEmployedPanelEmployed.setText(fieldsEmployed.get(4));
                    birthdateEmployedPanelEmployed.setText(fieldsEmployed.get(5));
                    emailEmployedPanelEmployed.setText(fieldsEmployed.get(6));
                    salaryEmployedPanelEmployed.setText(fieldsEmployed.get(7));
                    commissionEmployedPanelEmployed.setText(fieldsEmployed.get(8));
                    listPositionsPanelEmployed.setSelectedItem(fieldsEmployed.get(9));
                    employedDepartmentPanelEmployed.setSelectedItem(fieldsEmployed.get(10));
                    managerEmployedPanelEmployed.setSelectedItem(fieldsEmployed.get(11));
                }
            }
        };
        client.addEventListener(loadEmployed);
    }

    public void eventLoadManagers() {
        EventChangeClientListener loadManagers = new EventChangeClientListener() {
            @Override
            void onManagers(EventChangeClient event) {
                managerEmployedPanelEmployed.removeAllItems();
                if (client.getManagers() != null) {
                    client.getManagers().forEach(managerEmployedPanelEmployed::addItem);
                }
            }
        };
        client.addEventListener(loadManagers);
    }

    private void eventItemTransactionSelected() {
        listTransactionsPanelTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                switch ((String) listTransactionsPanelTransaction.getSelectedItem()) {
                    case "Consultar empleado":
                        message.setType(TransactionType.SELECT_ONE_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(false);
                        idEmployedPanelEmployed.setEditable(true);
                        newIdEmployedPanelEmployed.setEditable(false);
                        break;
                    case "Eliminar empleado":
                        message.setType(TransactionType.DELETE_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(false);
                        idEmployedPanelEmployed.setEditable(true);
                        retirementDateEmployedPanelEmployed.setEditable(true);
                        newIdEmployedPanelEmployed.setEditable(false);
                        break;
                    case "Consultar empleados activos":
                        message.setType(TransactionType.SELECT_ALL_EMPLOYED);
                        panelSetVisible(panelTableEmployees);
                        createTable(listFieldsEmployeesActive);
                        break;
                    case "Consultar empleados retirados":
                        message.setType(TransactionType.SELECT_ALL_EMPLOYED_RETIREMENT);
                        panelSetVisible(panelTableEmployees);
                        createTable(listFieldsEmployeesRetirement);
                        break;
                    case "Ingresar Pais":
                        message.setType(TransactionType.INSERT_COUNTRY);
                        panelSetVisible(panelCountry);
                        break;
                    case "Ingresar Ciudad":
                        message.setType(TransactionType.INSERT_CITY);
                        panelSetVisible(panelCity);
                        loadingCountries();
                        break;
                    case "Ingresar Localización":
                        message.setType(TransactionType.INSERT_LOCALIZATION);
                        panelSetVisible(panelLocalization);
                        loadingCities();
                        break;
                    case "Ingresar Departamento":
                        message.setType(TransactionType.INSERT_DEPARTMENT);
                        panelSetVisible(panelDepartment);
                        loadingLocalizations();
                        break;
                    case "Ingresar Cargo":
                        message.setType(TransactionType.INSERT_POSITION);
                        panelSetVisible(panelPosition);
                        break;
                    case "Actualizar empleado":
                        message.setType(TransactionType.UPDATE_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(true);
                        break;
                    case "Ingresar empleado":
                        message.setType(TransactionType.INSERT_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(true);
                        newIdEmployedPanelEmployed.setEditable(false);
                        break;
                }
            }
        });
    }

    private void createTable(ArrayList<ArrayList<String>> employees) {
        String[] columnNames = {"ID", "Primer nombre", "Segundo Nombre", "Primer Apellido", "Segundo Apellido", "Cargo", "Departamento"};
        String[][] data;
        if (employees != null && !employees.isEmpty()) {
            data = new String[employees.size()][employees.get(0).size()];
            for (int row = 0; row < employees.size(); row++) {
                ArrayList<String> columns = employees.get(row);
                for (int column = 0; column < columns.size(); column++) {
                    data[row][column] = columns.get(column);
                }
            }
        }else {
            data = new String[1][7];
        }
        DefaultTableModel tm = new DefaultTableModel(data, columnNames);
        tableEmployees.setModel(tm);
    }


    private void panelsNotVisible() {
        panels.forEach(panel -> panel.setVisible(false));
    }

    private void panelSetVisible(JPanel panelVisible) {
        panelsNotVisible();
        panelVisible.setVisible(true);
    }


    private void setEnableItemsPanelEmployed(boolean enable) {
        idEmployedPanelEmployed.setEditable(enable);
        firstNameEmployedPanelEmployed.setEditable(enable);
        secondNameEmployedPanelEmployed.setEditable(enable);
        surnameEmployedPanelEmployed.setEditable(enable);
        secondSurnameEmployedPanelEmployed.setEditable(enable);
        salaryEmployedPanelEmployed.setEditable(enable);
        birthdateEmployedPanelEmployed.setEditable(enable);
        commissionEmployedPanelEmployed.setEditable(enable);
        listPositionsPanelEmployed.setEnabled(enable);
        employedDepartmentPanelEmployed.setEnabled(enable);
        emailEmployedPanelEmployed.setEditable(enable);
        managerEmployedPanelEmployed.setEnabled(enable);
        newIdEmployedPanelEmployed.setEditable(enable);
        retirementDateEmployedPanelEmployed.setEditable(false);
    }

    /**
     * Método para manejar el cierre de la ventana al oprimir el botón de cerrar o finalizar
     */
    private void eventClose() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.closeConnection();
            }
        });
    }

    private void createUIComponents() {
        idEmployedPanelEmployed = new JFormattedTextField(0);
        newIdEmployedPanelEmployed = new JFormattedTextField(0);
        idCountryPanelCountry = new JFormattedTextField(0);
        idCityPanelCity = new JFormattedTextField(0);
        idLocalizationPanelLocalization = new JFormattedTextField(0);
        idDepartmentPanelDepartment = new JFormattedTextField(0);
        minimumSalaryPanelPosition = new JFormattedTextField(0);
        maximumSalaryPanelPosition = new JFormattedTextField(0);
        idPositionPanelPosition = new JFormattedTextField(0);
        listTransactionsPanelTransaction = new JComboBox<>(new String[]{"Ingresar empleado", "Actualizar empleado", "Consultar empleado",
                "Consultar empleados activos", "Consultar empleados retirados", "Eliminar empleado", "Ingresar Pais", "Ingresar Ciudad",
                "Ingresar Localización", "Ingresar Departamento", "Ingresar Cargo"});
    }
}
