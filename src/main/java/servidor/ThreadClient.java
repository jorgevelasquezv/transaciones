package servidor;


import connection.EmployedDTO;
import connection.Message;
import connection.StatementSQL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Clase ThreadClient permite crear sockets del lado del servidor para cada cliente conectado a este, y de este modo
 * cada socket creado trabaja en un hilo diferente para escuchar los mensajes enviados por los clientes, y así
 * establecer la comunicación en el chat bidireccional entre los clientes conectados al servidor. Hereda de la clase
 * Thread lo cual permite crear hilos habilitando la programación concurrente
 *
 * @Author Jorge Luis Velasquez Venegas
 */
public class ThreadClient extends Thread {
    /**
     * Socket: punto final para la comunicación con el cliente.
     */
    private Socket socket;
    /**
     * ObjectInputStream: recepción en Stream de Objetos de la clase Message
     */
    private ObjectInputStream dataInputStream;
    /**
     * ObjectOutputStream: envío en Stream de Objetos de la clase Message
     */
    private ObjectOutputStream dataOutputStream;
    /**
     * Server: instancia de la clase servidor para manejo de los datos de comunicación
     */
    private Server server;
    /**
     * Connected: estado en que se encuentra el cliente (conectado, desconectado)
     */
    private boolean connected;

    /**
     * Constructor de la clase ThreadClient crea una conexión del lado del servidor mediante sockets para la escucha de
     * clientes conectados al servidor
     *
     * @param socket punto final para la comunicación entre clientes.
     * @param server instancia de la clase servidor para manejo de los datos de comunicación
     */
    public ThreadClient(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            dataInputStream = new ObjectInputStream(socket.getInputStream());
            dataOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.start();
    }

    /**
     * Método que se ejecuta al terminar de construir el objeto de la clase ThreadClient, el cual da inicio al hilo de
     * programación concurrente para la escucha activa del lado del servidor
     */
    @Override
    public void run() {
        try {
            System.out.println("Cliente Conectado");
            server.setMessageConsole("Cliente Conectado\n");
            listen();
        } catch (Exception e) {
            System.out.println("Error al escuchar cliente " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Loop de escucha activa de mensajes enviados por el cliente
     */
    public void listen() {
        connected = true;
        while (connected) {
            try {
                Message message = (Message) dataInputStream.readObject();
                this.operations(message);
            } catch (IOException e) {
                System.out.println("Cliente Desconectado");
                server.setMessageConsole("Cliente Desconectado\n");
                endConnection();
                System.out.println("Esperando....");
                server.setMessageConsole("Esperando....\n");
            } catch (ClassNotFoundException e) {
                System.out.println("Error en listen() HiloCliente error de datos recibidos: " + e.getMessage());
            }
        }
    }

    /**
     * Ejecuta la acción determinada en el atributo type del objeto message que determina el tipo de transacción a efectuar en la base de datos
     *
     * @param message objeto que contiene la acción a ejecutar y los datos a procesar
     */
    private void operations(Message message) {
        Integer employedId = message.getIdEmployed();
        String retirementDate = message.getRetirementDate();

        switch (message.getType()) {
            case SELECT_ONE_EMPLOYED:
                message.setResponseServer("Consulta Por ID De Empleado Satisfactoria\n");
                selectOne(message);
                break;
            case SELECT_ALL_EMPLOYED:
                message.setResponseServer("Consulta De Empleados Satisfactoria\n");
                message.setEmployedStatus("Activo");
                selectAll(message);
                break;
            case SELECT_ALL_EMPLOYED_RETIREMENT:
                message.setResponseServer("Consulta De Empleados Retirados Satisfactoria\n");
                message.setEmployedStatus("Retirado");
                selectAll(message);
                break;
            case INSERT_EMPLOYED:
                try {
                    StatementSQL.insertEmployed(message.getEmployed());
                    message.setResponseServer("Se Ingreso Empleado Correctamente\n");
                    selectAll(message);
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error ingresando empleado, ya existe un empleado con el ID indicado!!");
                    sendMessage(message);
                }
                break;
            case UPDATE_EMPLOYED:
                try {
                    StatementSQL.updateEmployed(message.getEmployed());
                    message.setResponseServer("Se Actualizo Empleado Correctamente\n");
                    selectAll(message);
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error al actualizar empleado, verifique todos los campos!!");
                    sendMessage(message);
                }
                break;
            case DELETE_EMPLOYED:
                StatementSQL.deleteEmployed(employedId, retirementDate);
                message.setResponseServer("Se Elimino Empleado Correctamente\n");
                message.setEmployedStatus("Activo");
                selectAll(message);
                break;
            case INSERT_COUNTRY:
                try {
                    StatementSQL.insertCountry(message.getFieldsCountry());
                    message.setResponseServer("Pais Ingresado Correctamente\n");
                    sendMessage(message);
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error ingresando país, ya existe un país con el ID indicado!!");
                    sendMessage(message);
                }
                break;
            case INSERT_CITY:
                try {
                    StatementSQL.insertCity(message.getFieldsCity());
                    message.setResponseServer("Ciudad Ingresada Correctamente\n");
                    sendMessage(message);
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error ingresando ciudad, ya existe una ciudad con el ID indicado!!");
                    sendMessage(message);
                }
                break;
            case INSERT_POSITION:
                try {
                    StatementSQL.insertPosition(message.getFieldsPosition());
                    message.setResponseServer("Cargo Ingresado Correctamente\n");
                    sendMessage(message);
                    sendMessage(StatementSQL.selectPositions());
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error ingresando cargo, ya existe un cargo con el ID indicado!!");
                    sendMessage(message);
                }
                break;
            case INSERT_DEPARTMENT:
                try {
                    StatementSQL.insertDepartment(message.getFieldsDepartment());
                    message.setResponseServer("Departamento Ingresado Correctamente\n");
                    sendMessage(message);
                    sendMessage(StatementSQL.selectDepartments());
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error ingresando departamento, ya existe un departamento con el ID indicado!!");
                    sendMessage(message);
                }
                break;
            case INSERT_LOCALIZATION:
                try {
                    StatementSQL.insertLocalization(message.getFieldsLocalization());
                    message.setResponseServer("Localización Ingresada Correctamente\n");
                    sendMessage(message);
                } catch (RuntimeException e) {
                    message.setResponseServer("¡¡Error ingresando localización, ya existe una localización con el ID indicado!!");
                    sendMessage(message);
                }
                break;
            case SELECT_POSITIONS:
                sendMessage(StatementSQL.selectPositions());
                break;
            case SELECT_DEPARTMENTS:
                sendMessage(StatementSQL.selectDepartments());
                break;
            case SELECT_CITIES:
                sendMessage(StatementSQL.selectCities());
                break;
            case SELECT_COUNTRIES:
                sendMessage(StatementSQL.selectCountries());
                break;
            case SELECT_LOCALIZATIONS:
                sendMessage(StatementSQL.selectLocalizations());
                break;
            case SELECT_MANAGERS:
                sendMessage(StatementSQL.selectManagers());
                break;
        }
    }

    /**
     * selectAll: método que permite realizar consulta de listado de empleados por estado (Activo o Retirado)
     * empleando la clase static StatementSQL
     *
     * @param message objeto con el tipo de sentencia a efectuar y los datos para llevarla a cabo
     */
    private void selectAll(Message message) {
        ArrayList<EmployedDTO> employeesFound = StatementSQL.findAllEmployees(message.getEmployedStatus());
        message.setEmployees(employeesFound);
        sendMessage(message);
    }

    /**
     * selectOne: método que permite realizar consulta de un empleado por ID
     * empleando la clase static StatementSQL
     *
     * @param message objeto con el tipo de sentencia a efectuar y los datos para llevarla a cabo
     */
    private void selectOne(Message message) {
//        ArrayList<String> fieldsEmployed = StatementSQL.findEmployed(message.getIdEmployed());
        EmployedDTO employed = StatementSQL.findEmployed(message.getIdEmployed());
        message.setEmployed(employed);
        if (employed == null) {
            message.setResponseServer("No existe empleado con el ID indicado");
        }
        sendMessage(message);
    }

    /**
     * sendMessage: método para envío de mensajes al cliente en el otro extremo del socket con la respuesta de la
     * transacción efectuada
     *
     * @param message objeto con el tipo de sentencia a efectuada y los datos de respuesta de la sentencia
     */
    private void sendMessage(Message message) {
        try {
            dataOutputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error enviando mensaje " + e);
        }
    }

    /**
     * endConnection: método que permite finalizar el loop y él cierra de conexión del socket del lado del
     * servidor
     */
    public void endConnection() {
        try {
            socket.close();
            connected = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
