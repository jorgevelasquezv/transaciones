package connection;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase Message, se crean instancias de esta clase para ser enviados como mensajes entre servidor y cliente, estas
 * instancias indican el tipo de transacción a ejecutar y los datos ya sea para ejecutar la transacción o los datos
 * de respuesta de la transacción, implementa la interfaz Serializable por ser empleada para transmisión de datos a
 * través de ObjetStream
 * @Author Jorge Luis Velasquez Venegas
 */
@Data
public class Message implements Serializable {
    /**
     * type: tipo de transacción a efectuar sobre la base de datos
     */
    private TransactionType type;
    /**
     * idEmployed: ID de empleado
     */
    private Integer idEmployed;
    /**
     * retirementDate: fecha de retiro de empleado
     */
    private String retirementDate;
    /**
     * employedStatus: estado de empleado en la base de datos Activo o Retirado
     */
    private String employedStatus;
    /**
     * responseServer: Mensaje de respuesta del servidor para visualizar en consola o TextArea
     */
    private String responseServer;
    /**
     * fieldsCountry: listado de atributos de un país contenido en la tabla países de la base de datos
     */
    private ArrayList<String> fieldsCountry;
    /**
     * positions: listado con nombres de cargos contenido en la tabla cargos de la base de datos
     */
    private ArrayList<String> positions;
    /**
     * departments: listado con nombres de departamentos contenido en la tabla departamentos de la base de datos
     */
    private ArrayList<String> departments;
    /**
     * cities: listado con nombres de ciudades contenido en la tabla ciudades de la base de datos
     */
    private ArrayList<String> cities;
    /**
     * countries: listado con nombres de países contenido en la tabla países de la base de datos
     */
    private ArrayList<String> countries;
    /**
     * localizations: listado con nombres de localizaciones contenido en la tabla localizaciones de la base de datos
     */
    private ArrayList<String> localizations;
    /**
     * employees: listado de empleados con sus atributos contenidos en la tabla empleados de la base de datos
     */
    private ArrayList<EmployedDTO> employees;
    /**
     * employed: Objeto de la clase EmployedDTO para el manejo de los atributos del empleado contenidos en la tabla empleados de la base de datos
     */
    private EmployedDTO employed;
    /**
     * employed: listado de atributos de un empleado contenido en la tabla empleado de la base de datos
     */
    private ArrayList<String> fieldsEmployed;
    /**
     * managers: listado de cargos de gerentes contenidos en la tabla empleados de la base de datos
     */
    private ArrayList<String> managers;
    /**
     * fieldsCity: listado de atributos de una ciudad contenido en la tabla ciudades de la base de datos
     */
    private ArrayList<String> fieldsCity;
    /**
     * fieldsLocalization: listado de atributos de una localización contenido en la tabla localizaciones de la base de datos
     */
    private ArrayList<String> fieldsLocalization;
    /**
     * fieldsDepartment: listado de atributos de un departamento contenido en la tabla departamentos de la base de datos
     */
    private ArrayList<String> fieldsDepartment;
    /**
     * fieldsPosition: listado de atributos de un cargo contenido en la tabla cargos de la base de datos
     */
    private ArrayList<String> fieldsPosition;

    /**
     * Constructor de la clase que permite crear una nueva instancia basada en otra
     * @param message
     */
    public Message(Message message) {
        this.type = message.getType();
        this.idEmployed = message.getIdEmployed();
        this.retirementDate = message.getRetirementDate();
        this.employedStatus = message.getEmployedStatus();
        this.responseServer = message.getResponseServer();
        this.fieldsCountry = message.getFieldsCountry();
        this.positions = message.getPositions();
        this.departments = message.getDepartments();
        this.cities = message.getCities();
        this.countries = message.getCountries();
        this.localizations = message.getLocalizations();
        this.employees = message.getEmployees();
        this.fieldsEmployed = message.getFieldsEmployed();
        this.managers = message.getManagers();
        this.fieldsCity = message.getFieldsCity();
        this.fieldsLocalization = message.getFieldsLocalization();
        this.fieldsDepartment = message.getFieldsDepartment();
        this.fieldsPosition = message.getFieldsPosition();
        this.employed = message.getEmployed();
    }
    /**
     * Constructor vacío de la clase
     */
    public Message() {
    }
    /**
     * Constructor con argumento de tipo TransactionType
     * @param transactionType
     */
    public Message(TransactionType transactionType){
        this.type = transactionType;
    }
}
