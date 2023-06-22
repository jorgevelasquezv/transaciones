package cliente;

import connection.Employed;
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
 * En esta clase se genera una ventana de tipo JFrame en la cual se visualizan los tipos de transacciones a efectuar
 * en la base de datos y el retorno de cada consulta si esta lo regresa. El uso de eventos generados en la clase Client
 * permiten la actualización en segundo plano del TestArea denominado console, en el cual se visualiza cuando es
 * enviado una transacción y cuando es recibida una respuesta de la transacción. Dichos eventos también permiten la
 * actualización en segundo plano de los componentes JComboBox que alojan el listado de localizaciones, países,
 * gerentes, ciudades y cargos
 *
 * @Author Jorge Luis Velasquez
 */
public class WindowClient extends JFrame {
    /**
     * panelMain: Panel principal de tipo JPanel en el cual se encuentran contenidos los demás elementos de la venta
     */
    private JPanel panelMain;
    /**
     * listTransactionsPanelTransaction: lista de transacciones que se pueden solicitar al servidor. Se visualiza en un
     * JComboBox (lista desplegable)
     */
    private JComboBox listTransactionsPanelTransaction;
    /**
     * console: Área multi línea de tipo JTextArea que permite visualizar cuando se envía y recibe una transacción. Se
     * actualiza en segundo plano gracias a los eventos creados para los atributos de la clase Client
     */
    private JTextArea console;
    /**
     * firstNameEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del
     * primer nombre de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField firstNameEmployedPanelEmployed;
    /**
     * idEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del ID del empleado, ubicado en el JPanel de empleado
     */
    private JFormattedTextField idEmployedPanelEmployed;
    /**
     * SendMessage: Botón del tipo JButton. Permite ejecutar el método de envío de una solicitud de transacción a al socket server
     * empleando el evento del click del mouse sobre el botón
     */
    private JButton sendTransaction;
    /**
     * nameCountryPanelCountry: entrada de texto de tipo JTextField, permite el ingreso o visualización del nombre de un país, ubicado en el JPanel de país
     */
    private JTextField nameCountryPanelCountry;
    /**
     * nameCityPanelCity: entrada de texto de tipo JTextField, permite el ingreso o visualización del nombre de una ciudad, ubicado en el JPanel de ciudad
     */
    private JTextField nameCityPanelCity;
    /**
     * listCountriesPanelCity: lista de países que se pueden solicitar al servidor. Se visualiza en un
     * JComboBox (lista desplegable) ubicado en el panel de ciudad
     */
    private JComboBox listCountriesPanelCity;
    /**
     * addressPanelLocalization: entrada de texto de tipo JTextField, permite el ingreso o visualización de una dirección, ubicado en el JPanel de localización
     */
    private JTextField addressPanelLocalization;
    /**
     * listCityPanelLocalization: lista de ciudades que se pueden solicitar al servidor. Se visualiza en un
     * JComboBox (lista desplegable) ubicado en el panel de localización
     */
    private JComboBox listCityPanelLocalization;
    /**
     * nameDepartmentPanelDepartment: entrada de texto de tipo JTextField, permite el ingreso o visualización del nombre de un departamento, ubicado en el JPanel de departamento
     */
    private JTextField nameDepartmentPanelDepartment;
    /**
     * listLocalizationPanelDepartment: lista de ciudades que se pueden solicitar al servidor. Se visualiza en un
     * JComboBox (lista desplegable) ubicado en el panel de departamentos
     */
    private JComboBox listLocalizationPanelDepartment;
    /**
     * namePositionPanelPosition: entrada de texto de tipo JTextField, permite el ingreso o visualización del nombre de un cargo, ubicado en el JPanel de cargos
     */
    private JTextField namePositionPanelPosition;
    /**
     * minimumSalaryPanelPosition: entrada de texto de tipo JTextField, permite el ingreso o visualización del salario mínimo de un cargo, ubicado en el JPanel de cargo
     */
    private JFormattedTextField minimumSalaryPanelPosition                          ;
    /**
     * maximumSalaryPanelPosition: entrada de texto de tipo JTextField, permite el ingreso o visualización del salario máximo de un cargo, ubicado en el JPanel de cargos
     */
    private JFormattedTextField maximumSalaryPanelPosition;
    /**
     * panelCountry: Panel con atributos para cargar un país a la base de datos
     */
    private JPanel panelCountry;
    /**
     * panelCity: Panel con atributos para cargar una ciudad a la base de datos
     */
    private JPanel panelCity;
    /**
     * panelLocalization: Panel con atributos para cargar una localización a la base de datos
     */
    private JPanel panelLocalization;
    /**
     * panelDepartment: Panel con atributos para cargar un departamento a la base de datos
     */
    private JPanel panelDepartment;
    /**
     * panelPosition: Panel con atributos para cargar un cargo a la base de datos
     */
    private JPanel panelPosition;
    /**
     * panelCountry: Panel con atributos para ver un empleado consultado, también actualizar, ingresar y eliminar un empleado en la base de datos
     */
    private JPanel panelEmployed;
    /**
     * panelCountry: Panel con atributos para selección de transacción a efectuar
     */
    private JPanel panelTransactions;
    /**
     * surnameEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del primer apellido de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField surnameEmployedPanelEmployed;
    /**
     * emailEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del email de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField emailEmployedPanelEmployed;
    /**
     * birthdateEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización de la fecha de nacimiento de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField birthdateEmployedPanelEmployed;
    /**
     * salaryEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del salario de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField salaryEmployedPanelEmployed;
    /**
     * commissionEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización de la comisión de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField commissionEmployedPanelEmployed;
    /**
     * listPositionsPanelEmployed: lista desplegable de tipo JComboBox que permite visualizar y/o seleccionar cargo del listado de cargos consultado en la base de datos
     */
    private JComboBox listPositionsPanelEmployed;
    /**
     * employedDepartmentPanelEmployed: lista desplegable de tipo JComboBox que permite visualizar y/o seleccionar departamento del listado de departamentos consultado en la base de datos
     */
    private JComboBox employedDepartmentPanelEmployed;
    /**
     * tableEmployees: tabla para visualizar empleados consultados en la base de datos según estado Activo o Retirado
     */
    private JTable tableEmployees;
    /**
     * scrollPanelTableEmployees: panel con barras desplazables para visualizar elementos de la tabla en caso de ser mayor al tamaño de ventana
     */
    private JScrollPane scrollPanelTableEmployees;
    /**
     * panelTableEmployees: Panel con atributos para ver tabla con lista de empleados consultados en la base de datos según estado Activo o Retirado en la base de datos
     */
    private JPanel panelTableEmployees;
    /**
     * secondNameEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del segundo nombre de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField secondNameEmployedPanelEmployed;
    /**
     * secondSurnameEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización del segundo apellido de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField secondSurnameEmployedPanelEmployed;
    /**
     * managerEmployedPanelEmployed: lista desplegable de tipo JComboBox que permite visualizar y/o seleccionar gerente del listado de gerentes consultado en tabla de empleados de base de datos
     */
    private JComboBox managerEmployedPanelEmployed                                  ;
    /**
     * idCountryPanelCountry: entrada de texto de tipo JTextField, permite el ingreso de ID de un país, ubicado en el JPanel de país
     */
    private JFormattedTextField idCountryPanelCountry;
    /**
     * idCityPanelCity: entrada de texto de tipo JTextField, permite el ingreso de un ID de ciudad, ubicado en el JPanel de ciudad
     */
    private JFormattedTextField idCityPanelCity;
    /**
     * idLocalizationPanelLocalization: entrada de texto de tipo JTextField, permite el ingreso de un ID de localización, ubicado en el JPanel de localización
     */
    private JFormattedTextField idLocalizationPanelLocalization;
    /**
     * idDepartmentPanelDepartment: entrada de texto de tipo JTextField, permite el ingreso de un ID de departamento, ubicado en el JPanel de departamento
     */
    private JFormattedTextField idDepartmentPanelDepartment;
    /**
     * idPositionPanelPosition: entrada de texto de tipo JTextField, permite el ingreso de un ID de cargo, ubicado en el JPanel de cargo
     */
    private JFormattedTextField idPositionPanelPosition;
    /**
     * retirementDateEmployedPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización de la fecha de retiro de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField retirementDateEmployedPanelEmployed;
    /**
     * nameCityPanelEmployed: lista desplegable de tipo JComboBox que permite visualizar y/o seleccionar
     * la ciudad donde se encuentra un empleado, ubicado en el JPanel de empleado
     */
    private JComboBox listCityPanelEmployed;
    /**
     * addressPanelEmployed: entrada de texto de tipo JTextField, permite el ingreso o visualización de la dirección de un empleado, ubicado en el JPanel de empleado
     */
    private JTextField addressPanelEmployed;
    /**
     * panels: ArrayList con lista de paneles creados en la ventana principal para manipular sui visibilidad según
     * transacción a efectuar, la cual es manejada a través del evento del selectItem en la lista desplegable de transacciones
     */
    private ArrayList<JPanel> panels;
    /**
     * listFieldsEmployeesActive: ArrayList con lista de empleados activos consultados en base de datos
     */
    private ArrayList<ArrayList<String>> listFieldsEmployeesActive;
    /**
     * listFieldsEmployeesRetirement: ArrayList con lista de empleados retirados consultados en base de datos
     */
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
    /**
     * message: objeto de la clase Message, el cual es manipulado para indicar que tipo de transacción efectuara el
     * socket server en la base datos y la información requerida para completar dicha transacción. Este objeto es
     * el que se envía en cada mensaje al socket server
     */
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
        panels = new ArrayList<>(Arrays.asList(panelEmployed, panelTableEmployees, panelCountry,
                panelCity, panelLocalization, panelDepartment, panelPosition));
        panelsNotVisible();
        panelEmployed.setVisible(true);
        listFieldsEmployeesActive = new ArrayList<>();
        listFieldsEmployeesRetirement = new ArrayList<>();
        message.setType(TransactionType.INSERT_EMPLOYED);
    }

    /**
     * loadingPositions: Método que permite solicitar al socket server una consulta de todos los cargos que se
     * encuentran en la base datos en la tabla de cargos
     */
    private void loadingPositions() {
        Message message = new Message(TransactionType.SELECT_POSITIONS);
        client.sendStatement(message);
    }
    /**
     * loadingDepartments: Método que permite solicitar al socket server una consulta de todos los departamentos que se
     * encuentran en la base datos en la tabla de departamentos
     */
    private void loadingDepartments() {
        Message message = new Message(TransactionType.SELECT_DEPARTMENTS);
        client.sendStatement(message);
    }
    /**
     * loadingCities: Método que permite solicitar al socket server una consulta de todos las ciudades que se
     * encuentran en la base de datos en la tabla de ciudades
     */
    private void loadingCities() {
        Message message = new Message(TransactionType.SELECT_CITIES);
        client.sendStatement(message);
    }
    /**
     * loadingCountries: Método que permite solicitar al socket server una consulta de todos los países que se
     * encuentran en la base de datos en la tabla de países
     */
    private void loadingCountries() {
        Message message = new Message(TransactionType.SELECT_COUNTRIES);
        client.sendStatement(message);
    }
    /**
     * loadingLocalizations: Método que permite solicitar al socket server una consulta de todos las localizaciones que se
     * encuentran en la base de datos en la tabla de localizaciones
     */
    private void loadingLocalizations() {
        Message message = new Message(TransactionType.SELECT_LOCALIZATIONS);
        client.sendStatement(message);
    }
    /**
     * loadingManagers: Método que permite solicitar al socket server una consulta de todos los gerentes que se
     * encuentran en la base de datos en la tabla de empleados
     */
    private void loadingManagers() {
        Message message = new Message(TransactionType.SELECT_MANAGERS);
        client.sendStatement(message);
    }
    /**
     * Método que permite crear instancia de la clase Client y establecer la conexión con el socketSever,
     * además de realizar solicitud al socket server de consultas sobre la base de datos para obtener listado de
     * cargos, departamentos y gerentes
     */
    private void setUp() {
        try {
            this.client = new Client(IP_ADDRESS, PORT, "ManagerSQL");
            loadingPositions();
            loadingDepartments();
            loadingManagers();
            loadingCities();
        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
            System.exit(0);
        }
    }
    /**
     * Método para manejar el evento del click del mouse sobre el botón de enviar transacciones al socket server
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
    /**
     * Método para manipular el objeto message según el tipo de transacción que se le solicitara al socket server,
     * agregando los atributos necesarios para completar la transacción
     * @param fieldsEmployed
     */
    private void selectStatementToSend(ArrayList<String> fieldsEmployed) {
        switch (message.getType()) {
            case INSERT_EMPLOYED:
            case UPDATE_EMPLOYED:
                loadEmployed();
                message.setFieldsEmployed(fieldsEmployed);
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
    /**
     * Método para crear objeto de la clase Employed con atributos de empleado
     *
     */
    private void loadEmployed() {
        Employed employed = new Employed();

        employed.setID(String.valueOf(idEmployedPanelEmployed.getValue()));
        employed.setName(firstNameEmployedPanelEmployed.getText());
        employed.setLastName(secondNameEmployedPanelEmployed.getText());
        employed.setSurname(surnameEmployedPanelEmployed.getText());
        employed.setSecondSurname(secondSurnameEmployedPanelEmployed.getText());
        employed.setBirthday(birthdateEmployedPanelEmployed.getText());
        employed.setEmail(emailEmployedPanelEmployed.getText());
        employed.setSalary(salaryEmployedPanelEmployed.getText());
        employed.setCommission(commissionEmployedPanelEmployed.getText());
        employed.setPosition((String) listPositionsPanelEmployed.getSelectedItem());
        employed.setManager((String) managerEmployedPanelEmployed.getSelectedItem());
        employed.setDepartment((String) employedDepartmentPanelEmployed.getSelectedItem());
        employed.setCity((String) listCityPanelEmployed.getSelectedItem());
        employed.setAddress(addressPanelEmployed.getText());

        message.setEmployed(employed);
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
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo positions y asi
     * actualizar la lista desplegable JComboBox donde se visualizan los cargos en segundo plano
     */
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
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo departments y asi
     * actualizar la lista desplegable JComboBox donde se visualizan los departamentos en segundo plano
     */
    public void eventLoadDepartments() {
        EventChangeClientListener loadDepartments = new EventChangeClientListener() {
            @Override
            void onDepartments(EventChangeClient event) {
                employedDepartmentPanelEmployed.removeAllItems();
                client.getDepartments().forEach(employedDepartmentPanelEmployed::addItem);
            }
        };
        client.addEventListener(loadDepartments);
    }
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo cities y asi
     * actualizar la lista desplegable JComboBox donde se visualizan las ciudades en segundo plano
     */
    public void eventLoadCities() {
        EventChangeClientListener loadCities = new EventChangeClientListener() {
            @Override
            void onCities(EventChangeClient event) {
                listCityPanelLocalization.removeAllItems();
                client.getCities().forEach(city -> {
                    listCityPanelLocalization.addItem(city);
                    listCityPanelEmployed.addItem((city));
                });
            }
        };
        client.addEventListener(loadCities);
    }
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo countries y asi
     * actualizar la lista desplegable JComboBox donde se visualizan los países en segundo plano
     */
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
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo localizations y asi
     * actualizar la lista desplegable JComboBox donde se visualizan las localizaciones en segundo plano
     */
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
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo employees y asi
     * actualizar la tabla JTable donde se visualizan los empleados en segundo plano según el tipo de consulta Activo o retirado
     */
    public void eventLoadEmployees() {
        EventChangeClientListener loadEmployees = new EventChangeClientListener() {
            @Override
            void onEmployees(EventChangeClient event) {
                ArrayList<ArrayList<String >> fieldEmployees = client.getEmployees();
                if (fieldEmployees != null && !fieldEmployees.isEmpty()) {
                    if (client.getEmployedStatus() != null && client.getEmployedStatus().equals("Activo")) {
                        listFieldsEmployeesActive = client.getEmployees();
                        createTable(listFieldsEmployeesActive);
                    } else {
                        listFieldsEmployeesRetirement = client.getEmployees();
                        createTable(listFieldsEmployeesRetirement);
                    }
                }
            }
        };
        client.addEventListener(loadEmployees);
    }
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo employed y asi
     * actualizar los elementos contenidos en JPanel de empleado donde se visualizan los atributos de un empleado en segundo plano
     */
    public void eventLoadEmployed() {
        EventChangeClientListener loadEmployed = new EventChangeClientListener() {
            @Override
            void onEmployed(EventChangeClient event) {
                ArrayList<String> fieldsEmployed = client.getEmployed();
                if (!fieldsEmployed.isEmpty()) {
                    idEmployedPanelEmployed.setText(fieldsEmployed.get(0));
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
                    listCityPanelEmployed.setSelectedItem(fieldsEmployed.get(12));
                    addressPanelEmployed.setText(fieldsEmployed.get(13));
                }
            }
        };
        client.addEventListener(loadEmployed);
    }
    /**
     * Método para manejar el evento que se genera en el objeto de clase Client cuando se carga un valor en el atributo managers y asi
     * actualizar la lista desplegable JComboBox donde se visualizan los gerentes que fueron encontrados en la tabla de empleados en segundo plano
     */
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
    /**
     * Método para manejar el evento que se genera al seleccionar un item de la lista desplegable JComboBox de transacciones y asi
     * actualizar la ventana principal mostrando solo el panel que corresponde a cada tipo de transacción, además de
     * cargar en el objeto message el tipo de transacción a realizar, en segundo plano
     */
    private void eventItemTransactionSelected() {
        listTransactionsPanelTransaction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                switch ((String) listTransactionsPanelTransaction.getSelectedItem()) {
                    case "Consultar empleado":
                        message.setType(TransactionType.SELECT_ONE_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(false);
                        clearFieldsEmployed();
                        idEmployedPanelEmployed.setEditable(true);
                        console.setText("");
                        break;
                    case "Eliminar empleado":
                        message.setType(TransactionType.DELETE_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(false);
                        idEmployedPanelEmployed.setEditable(true);
                        retirementDateEmployedPanelEmployed.setEditable(true);
                        console.setText("");
                        break;
                    case "Consultar empleados activos":
                        message.setType(TransactionType.SELECT_ALL_EMPLOYED);
                        panelSetVisible(panelTableEmployees);
                        createTable(listFieldsEmployeesActive);
                        console.setText("");
                        break;
                    case "Consultar empleados retirados":
                        message.setType(TransactionType.SELECT_ALL_EMPLOYED_RETIREMENT);
                        panelSetVisible(panelTableEmployees);
                        createTable(listFieldsEmployeesRetirement);
                        console.setText("");
                        break;
                    case "Ingresar Pais":
                        message.setType(TransactionType.INSERT_COUNTRY);
                        panelSetVisible(panelCountry);
                        console.setText("");
                        break;
                    case "Ingresar Ciudad":
                        message.setType(TransactionType.INSERT_CITY);
                        panelSetVisible(panelCity);
                        loadingCountries();
                        console.setText("");
                        break;
                    case "Ingresar Localización":
                        message.setType(TransactionType.INSERT_LOCALIZATION);
                        panelSetVisible(panelLocalization);
                        loadingCities();
                        console.setText("");
                        break;
                    case "Ingresar Departamento":
                        message.setType(TransactionType.INSERT_DEPARTMENT);
                        panelSetVisible(panelDepartment);
                        loadingLocalizations();
                        console.setText("");
                        break;
                    case "Ingresar Cargo":
                        message.setType(TransactionType.INSERT_POSITION);
                        panelSetVisible(panelPosition);
                        console.setText("");
                        break;
                    case "Actualizar empleado":
                        message.setType(TransactionType.UPDATE_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(true);
                        console.setText("");
                        break;
                    case "Ingresar empleado":
                        message.setType(TransactionType.INSERT_EMPLOYED);
                        panelSetVisible(panelEmployed);
                        setEnableItemsPanelEmployed(true);
                        console.setText("");
                        break;
                }
            }
        });
    }

    /**
     * clearFieldsEmployed: método para limpiar todos los componentes del panel empleado
     */
    private void clearFieldsEmployed() {
         Arrays.stream(panelEmployed.getComponents())
                 .filter(component -> component instanceof JTextField)
                 .forEach(component -> ((JTextField) component).setText(""));
        idEmployedPanelEmployed.setValue(0);
    }

    /**
     * Método que permite cargar la JTable de empleados con los empleados encontrados en la base de datos según tipo de consulta Activo o Retirado
     * @param employees ArrayList de empleados encontrados en la base de datos según tipo de consulta Activo o Retirado
     */
    private void createTable(ArrayList<ArrayList<String>> employees) {
        String[] columnNames = {"ID", "Primer nombre", "Segundo Nombre", "Primer Apellido",
                "Segundo Apellido", "Cargo", "Departamento", "Ciudad", "Dirección"};
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
            data = new String[1][columnNames.length];
        }
        DefaultTableModel tm = new DefaultTableModel(data, columnNames);
        tableEmployees.setModel(tm);
    }
    /**
     * Método para manejo de no visibilizar paneles según item seleccionado de la lista de sentencias posibles a efectuar
     */
    private void panelsNotVisible() {
        panels.forEach(panel -> panel.setVisible(false));
    }

    /**
     * Método para manejar visibilidad de un panel en la ventana principal
     * @param panelVisible
     */
    private void panelSetVisible(JPanel panelVisible) {
        panelsNotVisible();
        panelVisible.setVisible(true);
    }
    /**
     * Método para manejo de habilitación de elementos de panel de empleados según el tipo de transacción
     * seleccionada en la lista desplegable Insertar, consultar, actualizar, eliminar
     * @param enable Valor booleano para cambio de estado de habilitación de elementos del panel
     */
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
        listCityPanelEmployed.setEnabled(enable);
        addressPanelEmployed.setEditable(enable);
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
    /**
     * Método para crear elementos de modo personalizado con el fin de establecer formato de tipo número a los
     * elementos de tipo JFormattedTextField y cargar listado or defecto de JComboBox para listado de transacciones
     * que se pueden ejecutar
     */
    private void createUIComponents() {
        idEmployedPanelEmployed = new JFormattedTextField(0);
        idCountryPanelCountry = new JFormattedTextField(0);
        idCityPanelCity = new JFormattedTextField(0);
        idLocalizationPanelLocalization = new JFormattedTextField(0);
        idDepartmentPanelDepartment = new JFormattedTextField(0);
        minimumSalaryPanelPosition = new JFormattedTextField(0);
        maximumSalaryPanelPosition = new JFormattedTextField(0);
        idPositionPanelPosition = new JFormattedTextField(0);
        listTransactionsPanelTransaction = new JComboBox<>(new String[]{"Ingresar empleado",
                "Actualizar empleado", "Consultar empleado",
                "Consultar empleados activos", "Consultar empleados retirados", "Eliminar empleado",
                "Ingresar Pais", "Ingresar Ciudad",
                "Ingresar Localización", "Ingresar Departamento", "Ingresar Cargo"});
        listTransactionsPanelTransaction.setSelectedIndex(0);
    }
}
