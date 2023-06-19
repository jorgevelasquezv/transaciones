package cliente;

import connection.Connection;
import connection.Message;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * La clase Client permite crear objetos cliente que se conectan mediante sockets con el servidor para realizar las
 * solicitudes de transacciones sobre la base de datos. Esta clase hereda de la clase Connection para establecer una
 * conexión empleando sockets, la clase Connection a su vez hereda de la clase Thread, lo cual permite generar hilos
 * para manejar la concurrencia del programa. Así mientras la clase principal está atenta a la interfaz, se ejecuta
 * un hilo que se encarga de enviar y recibir mensajes del servidor
 *
 * @Author Jorge Luis Velasquez Venegas
 */
@Getter
public class Client extends Connection {
    /**
     * IpAddress: dirección ip del servidor al que se desea conectar
     */
    private String ipAddress;
    /**
     * IP_ADDRESS: dirección ip del servidor que se establece por defecto cuando se crea una instancia de la clase
     * cliente sin asignar el valor
     */
    private final String IP_ADDRESS = "localhost";
    /**
     * Port: puerto de conexión con el servidor este debe coincidir con el puerto en el que escucha el servidor
     */
    private Integer port;
    /**
     * PORT: puerto por defecto para conexión con el servidor
     */
    private final Integer PORT = 2022;
    /**
     * IdClient: nombre que identifica al cliente en la conexión
     */
    private String idClient;
    /**
     * employedStatus: identifica el estado del empleado en la base de datos Activo o Retirado
     */
    private String employedStatus;
    /**
     * positions: listado con los cargos existentes en la base de datos
     */
    private ArrayList<String> positions;
    /**
     * departments: listado de departamentos existentes en la base de datos
     */
    private ArrayList<String> departments;
    /**
     * cities: listado de ciudades existentes en la base de datos
     */
    private ArrayList<String> cities;
    /**
     * countries: listado de países existentes en la base de datos
     */
    private ArrayList<String> countries;
    /**
     * localizations: listado de localizaciones existentes en la base de datos
     */
    private ArrayList<String> localizations;
    /**
     * employees: listado de empleados consultados en la base de datos según criterio Activo o Retirado
     */
    private ArrayList<ArrayList<String>> employees;
    /**
     * employed: listado de campos obtenidos en una consulta de cliente por id a la base de datos
     */
    private ArrayList<String> employed;
    /**
     * managers: listado de gerentes consultados en la tabla de empleados de la base de datos
     */
    private ArrayList<String> managers;

    /**
     * ObjectInputStream: recepción en Stream de Objetos de la clase Message
     */
    private ObjectInputStream objectInputStream;

    /**
     * ObjectOutputStream: envío en Stream de Objetos de la clase Message
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * Connected: indica si el cliente se encuentra activo o conectado para escuchar transmisiones
     */
    private boolean connected;

    /**
     * Message: string con el mensaje recibido
     */
    private String message;

    /**
     * Listado de eventos a escuchar
     */
    private static ArrayList listeners                                              ;


    /**
     * Constructor de la clase Client para crear instancias de client con sockets y crear un chat bidireccional con el
     * servidor mediante sockets, se ejecuta el método start al terminar de construir el objeto para asi iniciar el
     * hilo de ejecución concurrente
     *
     * @param ipAddress Dirección ip del servidor
     * @param port      puerto en que escucha el servidor
     * @param idClient  nombre que identifica la cliente
     * @throws IOException
     */
    public Client(String ipAddress, Integer port, String idClient) throws IOException {
        super("cliente", port, ipAddress);
        this.ipAddress = ipAddress.isBlank() ? IP_ADDRESS : ipAddress;
        this.port = port == null || port < 1024 ? PORT : port;
        this.idClient = idClient;
        listeners = new ArrayList<>();
        this.start();
    }
    /**
     * Método que se ejecuta al terminar de construir el objeto de la clase Client, el cual da inicio al hilo de
     * programación concurrente
     */
    @Override
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            this.connected = true;
            this.listen();
        } catch (IOException e) {
            System.out.println("Error de conexión con el servidor");
            this.closeConnection();
        }
    }
    /**
     * Método para enviar un objeto de la clase Message el cual contiene el tipo de transacción a realizar
     * y los datos requeridos por la misma
     * @param message Objeto de la clase Message el cual contiene el tipo de transacción a enviar y los datos
     *                requeridos por la misma
     */
    public void sendStatement(Message message) {
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error enviando mensaje " + e);
        }
    }
    /**
     * Loop para escucha activa de mensajes enviados desde el servidor
     */
    public void listen() {
        while (connected) {
            try {
                Object request = objectInputStream.readObject();
                if (request != null && request instanceof Message) {
                    Message message = (Message) request;
                    operations(message);
                } else {
                    System.out.println("Se recibió un valor inesperado");
                }
            } catch (ClassNotFoundException | IOException e) {
                if (!clientSocket.isClosed()) System.out.println("error en listen() de cliente " + e);
                System.exit(0);
            }
        }
    }
    /**
     * Método que evalúa la acción a efectuar cuando se recibe un mensaje del servidor
     * @param message
     */
    private void operations(Message message) {
        switch (message.getType()) {
            case SELECT_POSITIONS:
                setPositions(message.getPositions());
                break;
            case SELECT_DEPARTMENTS:
                setDepartments(message.getDepartments());
                break;
            case SELECT_CITIES:
                setCities(message.getCities());
                break;
            case SELECT_COUNTRIES:
                setCountries(message.getCountries());
                break;
            case SELECT_LOCALIZATIONS:
                setLocalizations(message.getLocalizations());
                break;
            case SELECT_ALL_EMPLOYED:
            case SELECT_ALL_EMPLOYED_RETIREMENT:
                setMessage(message.getResponseServer());
                setEmployedStatus(message.getEmployedStatus());
                setEmployees(message.getEmployees());
                break;
            case DELETE_EMPLOYED:
            case UPDATE_EMPLOYED:
            case INSERT_EMPLOYED:
                setMessage(message.getResponseServer());
                setEmployees(message.getEmployees());
                break;
            case SELECT_ONE_EMPLOYED:
                setMessage(message.getResponseServer());
                setEmployed(message.getEmployed());
                break;
            case SELECT_MANAGERS:
                setManagers(message.getManagers());
                break;
            case INSERT_COUNTRY:
            case INSERT_POSITION:
            case INSERT_LOCALIZATION:
            case INSERT_DEPARTMENT:
            case INSERT_CITY:
                setMessage(message.getResponseServer());
                break;
            default:
                break;
        }
    }
    /**
     * Notifica el servidor del cierre de conexión del socket y efectúa el cierre del socket
     */
    public void closeConnection() {
        try {
            this.objectInputStream.close();
            this.objectOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }

    }
    /**
     * Agrega un evento al listado de eventos a escuchar
     *
     * @param listener evento a agregar en la lista de eventos a escuchar
     */
    public void addEventListener(EventChangeClientListener listener) {
        listeners.add(listener);
    }
    /**
     * Método para disparar el evento cuando cambie la variable message que contiene el mensaje que se debe mostrar
     * en consola
     */
    private void triggerMessageEvent() {

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onMessageChange(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable positions que contiene listado de
     * posiciones existentes en la base de datos
     */
    private void triggerPositionsEvent() {

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onPositions(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable departments que contiene listado de
     * departamentos existentes en la base de datos
     */
    private void triggerDepartmentsEvent() {

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onDepartments(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable cities que contiene listado de
     * ciudades existentes en la base de datos
     */
    private void triggerCitiesEvent() {

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onCities(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable countries que contiene listado de
     * países existentes en la base de datos
     */
    private void triggerCountriesEvent() {

        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onCountries(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable localizations que contiene listado de
     * localizaciones existentes en la base de datos
     */
    private void triggerLocalizationsEvent() {
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onLocalizations(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable employees que contiene listado de
     * empleados consultados en la base de datos según criterio Activo o Retirado
     */
    private void triggerEmployeesEvent() {
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onEmployees(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable employed que contiene listado de
     * campos obtenidos de una consulta por ID en la tabla empleados en la base de datos
     */
    private void triggerEmployedEvent() {
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onEmployed(event);
        }
    }
    /**
     * Método para disparar el evento cuando cambie la variable mangers que contiene listado de
     * gerentes existentes en la tabla empleados de la base de datos
     */
    private void triggerManagersEvent() {
        ListIterator li = listeners.listIterator();
        while (li.hasNext()) {
            EventChangeClientListener listener = (EventChangeClientListener) li.next();
            EventChangeClient event = new EventChangeClient(this, this);
            (listener).onManagers(event);
        }
    }
    /**
     * establece el valor de la variable employedStatus la cual determina si el
     * empleado está Activo o Retirado
     * @param employedStatus String con el estado del empleado Activo o Retirado
     */
    public void setEmployedStatus(String employedStatus) {
        this.employedStatus = employedStatus;
    }

    /**
     * Retorna estado en que se encuentra el cliente para escuchar mensajes
     *
     * @return estado de conexión
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Establece el valor del mensaje recibido
     *
     * @param message String que contiene el mensaje recibido
     */
    public void setMessage(String message)                                        {
        this.message = message;
        this.triggerMessageEvent();
    }
    /**
     * establece el valor de la lista de cargos positions
     * @param positions ArrayList con listado de cargos consultados
     */
    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
        this.triggerPositionsEvent();
    }
    /**
     * establece el valor de la lista de departamentos departments
     * @param departments ArraList con listado de departamentos consultados
     */
    public void setDepartments(ArrayList<String> departments) {
        this.departments = departments;
        this.triggerDepartmentsEvent();
    }
    /**
     * establece el valor de la lista de ciudades cities
     * @param cities ArrayList con listado de ciudades consultadas
     */
    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
        this.triggerCitiesEvent();
    }
    /**
     * establece el valor del listado de países consultados
     * @param countries ArrayList con listado de países consultados
     */
    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
        this.triggerCountriesEvent();
    }
    /**
     * establece el valor del listado de localizaciones consultado
     * @param localizations ArrayList con listado de localizaciones consultado
     */
    public void setLocalizations(ArrayList<String> localizations) {
        this.localizations = localizations;
        this.triggerLocalizationsEvent();
    }
    /**
     * establece el valor del listado de empleados consultado
     * @param employees ArrayList con listado de campos por cliente consultado
     *                  en la base de datos
     */
    public void setEmployees(ArrayList<ArrayList<String>> employees) {
        this.employees = employees;
        this.triggerEmployeesEvent();
    }

    /**
     * establece el valor del listado de campos de empleado consultado pir ID
     * @param employed ArrayList con listado de campos de cliente consultado
     *                 por ID
     */
    public void setEmployed(ArrayList<String> employed) {
        this.employed = employed;
        this.triggerEmployedEvent();
    }

    /**
     * establece el valor del listado de gerentes consultado en la
     * tabla empleados
     * @param managers ArrayList con listado de gerentes consultados en la tabla empleados
     */
    public void setManagers(ArrayList<String> managers) {
        this.managers = managers;
        this.triggerManagersEvent();
    }
}
