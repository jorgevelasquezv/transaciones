package connection;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase Message, permite establecer conexión con un servidor enviando un tipo de acción a ejecutar, ya sea de
 * configuración para informar conexión, desconexión o un mensaje nuevo a enviar a otro cliente en la lista de
 * destinatarios, implementa la interfaz Serializable por ser empleada para transmisión de datos a través de ObjetStream
 * @Author Jorge Luis Velasquez Venegas
 */
@Data
public class Message implements Serializable {

    /**
     * Type: tipo de acción a ejecutar (tipo de acción a realizar ejemplo: conexión aceptada "accept-connection",
     * nuevo cliente conectado "new-client", cliente desconectado "disconnect-client", mensaje "message"
     */
    private TransactionType type;

    private Integer idEmployed;

    private String retirementDate;

    private String employedStatus;
    private String responseServer;

    private ArrayList<String> fieldsCountry;

    private ArrayList<String> positions;

    private ArrayList<String> departments;

    private ArrayList<String> cities;

    private ArrayList<String> countries;

    private ArrayList<String> localizations;

    private ArrayList<ArrayList<String>> employees;

    private ArrayList<String> employed;

    private ArrayList<String> managers;

    private ArrayList<String> fieldsCity;

    private ArrayList<String> fieldsLocalization;

    private ArrayList<String> fieldsDepartment;

    private ArrayList<String> fieldsPosition;


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
        this.employed = message.getEmployed();
        this.managers = message.getManagers();
        this.fieldsCity = message.getFieldsCity();
        this.fieldsLocalization = message.getFieldsLocalization();
        this.fieldsDepartment = message.getFieldsDepartment();
        this.fieldsPosition = message.getFieldsPosition();
    }

    public Message() {
    }

    public Message(TransactionType transactionType){
        this.type = transactionType;
    }


}
