package connection;

import java.sql.*;
import java.sql.Connection;
import java.util.*;

/**
 * Clase static StatementSQL, permite establecer conexión con base de datos y realizar transacciones sobre la base de
 * datos, como consulta de empleados por estado Activo o Retirado, Consulta y eliminación de empleado por ID, Ingresar
 * y actualizar un nuevo empleado en la base datos, consulta e ingreso de ciudades, países, cargos, localizaciones y
 * departamentos
 * @Author Jorge Luis Velasquez Venegas
 */
final public class StatementSQL {
    /**
     * connection: permite establecer conexión con base de datos y manipulación de tablas
     */
    private static Connection connection;
    /**
     * startConnection: método para manejo de conexión con base de datos
     */
    private static void startConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/recursoshumanos?" +
                    "user=Poli01&password=abc123");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
        }
    }
    /**
     * findAllEmployees: método que permite realizar consulta de empleados según estado (Activo, Retirado) a la base
     * de datos, devolviendo los campos de empleado, el nombre del cargo y nombre de departamento en consulta por union
     * de tablas según id de elementos relacionados
     * @param employedStatus String con estado del empleado (Activo o Retirado)
     * @return ArrayList con listado de empleados y sus atributos
     */
    public static ArrayList<ArrayList<String>> findAllEmployees(String employedStatus) {
        startConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT \n" +
                    "  empl_ID AS ID\n" +
                    ", empl_primer_nombre AS primer_nombre \n" +
                    ", empl_segundo_nombre AS segundo_nombre\n" +
                    ", empl_primer_apellido AS primer_apellido\n" +
                    ", empl_segundo_apellido AS segundo_apellido\n" +
                    ", c.cargo_nombre AS cargo\n" +
                    ", d.dpto_nombre AS departamento\n" +
                    ", ciud.ciud_nombre AS Ciudad\n" +
                    ", loc.localiz_direccion AS Direccion\n" +
                    "FROM empleados \n" +
                    "INNER JOIN cargos c \n" +
                    "ON c.cargo_ID = empl_cargo_ID \n" +
                    "INNER JOIN departamentos d \n" +
                    "ON d.dpto_ID = empl_dpto_ID\n" +
                    "INNER JOIN recursoshumanos.localizaciones loc\n" +
                    "ON loc.localiz_ID = empl_localiz_ID\n" +
                    "INNER JOIN recursoshumanos.ciudades ciud \n" +
                    "ON ciud.ciud_ID = loc.localiz_ciudad_ID\n" +
                    "WHERE empl_estado = ?;");
            preparedStatement.setString(1, employedStatus);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberColumns = metaData.getColumnCount();

            ArrayList<ArrayList<String>> data = new ArrayList<>();
            while (resultSet.next()) {
                int row = data.size();
                data.add(new ArrayList<>());
                for (int column = 0; column < numberColumns; column++) {
                    String value = resultSet.getString(column + 1);
                    data.get(row).add(value == null ? "" : value);
                }
            }
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * findEmployed: método que permite consultar empleado por ID, retornando todos los atributos del empleado
     * contenidos en la tabla de empleados, adicionalmente retorna nombre de cargo, nombre de departamento y nombre
     * del gerente o gerencia a la que pertenece el empleado
     * @param employedId ID de empleado a consultar
     * @return ArrayList con los atributos del empleado consultado
     */
    public static ArrayList<String> findEmployed(int employedId) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT \n" +
                "  empl.empl_ID AS ID\n" +
                ", empl.empl_primer_nombre AS primer_nombre \n" +
                ", empl.empl_segundo_nombre AS segundo_nombre\n" +
                ", empl.empl_primer_apellido AS primer_apellido\n" +
                ", empl.empl_segundo_apellido AS segundo_apellido\n" +
                ", empl.empl_fecha_nac AS fecha_nac\n" +
                ", empl.empl_email AS email\n" +
                ", empl.empl_sueldo AS Salario\n" +
                ", empl.empl_comision AS comision\n" +
                ", c.cargo_nombre AS cargo\n" +
                ", d.dpto_nombre AS departamento\n" +
                ", cg.cargo_nombre AS Gerencia\n" +
                ", ciud.ciud_nombre AS Ciudad\n" +
                ", loc.localiz_direccion AS Direccion\n" +
                "FROM recursoshumanos.empleados empl\n" +
                "INNER JOIN recursoshumanos.cargos c \n" +
                "ON c.cargo_ID = empl_cargo_ID \n" +
                "INNER JOIN recursoshumanos.departamentos d \n" +
                "ON d.dpto_ID = empl_dpto_ID\n" +
                "INNER JOIN recursoshumanos.empleados e \n" +
                "ON empl.empl_gerente_ID = e.empl_ID\n" +
                "INNER JOIN recursoshumanos.cargos cg\n" +
                "ON e.empl_cargo_ID = cg.cargo_ID\n" +
                "INNER JOIN recursoshumanos.localizaciones loc\n" +
                "ON loc.localiz_ID = empl.empl_localiz_ID\n" +
                "INNER JOIN recursoshumanos.ciudades ciud \n" +
                "ON ciud.ciud_ID = loc.localiz_ciudad_ID\n" +
                "WHERE empl.empl_ID = ?\n" +
                ";")) {
            preparedStatement.setInt(1, employedId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberColumns = metaData.getColumnCount() + 1;
            ArrayList<String> fieldsEmployed = new ArrayList<>();
            if (resultSet.next()) {
                for (int index = 1; index < numberColumns; index++) {
                    if (index == 8) {
                        fieldsEmployed.add(resultSet.getInt(index) + "");
                    } else {
                        fieldsEmployed.add(resultSet.getString(index));
                    }
                }
            }
            return fieldsEmployed;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * insertEmployed: método que permite realizar inserción de empleado en la base de datos, haciendo sub consultas
     * para obtener ID de cargo, ID de gerente, ID de departamento por nombre de dichos atributos.
     * @param employed Objeto de la clase Employed con los atributos de empleado para ingresarlo en la base de datos
     */
    public static void insertEmployed(Employed employed) {
        startConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement= connection.prepareStatement("INSERT INTO \n" +
                    "localizaciones (localiz_ID, localiz_ciudad_ID, localiz_direccion) \n" +
                    "VALUES (\n" +
                    "(SELECT MAX(loc.localiz_ID) FROM localizaciones loc) + 1 \n" +
                    ", (SELECT ciud_ID FROM ciudades WHERE ciud_nombre = ?)\n" +
                    ", ?\n" +
                    ");");

            preparedStatement.setString(1, employed.getCity());
            preparedStatement.setString(2, employed.getAddress());
            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("INSERT INTO recursoshumanos.empleados\n" +
                    "(\n" +
                    "  empl_ID\n" +
                    ", empl_primer_nombre\n" +
                    ", empl_segundo_nombre\n" +
                    ", empl_primer_apellido\n" +
                    ", empl_segundo_apellido\n" +
                    ", empl_email\n" +
                    ", empl_fecha_nac\n" +
                    ", empl_sueldo\n" +
                    ", empl_comision\n" +
                    ", empl_localiz_ID\n"+
                    ", empl_cargo_ID\n" +
                    ", empl_gerente_ID\n" +
                    ", empl_dpto_ID\n" +
                    ") \n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?" +
                    ", (SELECT MAX(loc.localiz_ID) FROM localizaciones loc)" +
                    ", (SELECT cargo_ID FROM recursoshumanos.cargos WHERE cargo_nombre = ?)\n" +
                    ", (SELECT e.empl_ID FROM recursoshumanos.empleados e WHERE empl_cargo_ID " +
                    "= (SELECT cargo_ID FROM recursoshumanos.cargos WHERE cargo_nombre = ?))\n" +
                    ", (SELECT dpto_ID FROM recursoshumanos.departamentos WHERE dpto_nombre = ?)\n" +
                    ");");

            preparedStatement.setString(1, employed.getID());
            preparedStatement.setString(2, employed.getName());
            preparedStatement.setString(3, employed.getLastName());
            preparedStatement.setString(4, employed.getSurname());
            preparedStatement.setString(5, employed.getSecondSurname());
            preparedStatement.setString(6, employed.getEmail());
            preparedStatement.setString(7, employed.getBirthday());
            preparedStatement.setString(8, employed.getSalary());
            preparedStatement.setString(9, employed.getCommission());
            preparedStatement.setString(10, employed.getPosition());
            preparedStatement.setString(11, employed.getManager());
            preparedStatement.setString(12, employed.getDepartment());

            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * updateEmployed: método que permite actualizar todos los campos de un empleado en la base de datos, se requiere
     * crear una tabla temporal, ya que MySQL no permite realizar sub consulta de la misma tabla en la que se realizara
     * la actualización de una fila. Se realizan sub consultas a las tablas de cargos, departamentos y la tabla temporal
     * de empleados para obtener ID de cargo, ID de departamento y ID de gerente o gerencia a la que pertenece el empleado
     * se realiza update de los atributos de localización como dirección y ciudad correspondientes al ID de empleado en
     * la tabla localizaciones
     * @param employed Objeto de la clase Employed con los atributos de empleado a ser actualizado en la base de datos
     */
    public static void updateEmployed(Employed employed) {
        startConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("CREATE TEMPORARY TABLE empl \n" +
                    "AS  (\n" +
                    "SELECT e.empl_ID FROM recursoshumanos.empleados e \n" +
                    "WHERE e.empl_cargo_ID = (\n" +
                    "SELECT cargo_ID FROM recursoshumanos.cargos \n" +
                    "WHERE cargo_nombre = ?));");
            preparedStatement.setString(1, employed.getManager());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("UPDATE localizaciones \n" +
                    "SET localiz_ciudad_ID = (SELECT ciud_ID FROM ciudades WHERE ciud_nombre = ?)\n" +
                    ", localiz_direccion = ?\n" +
                    "WHERE localiz_ID = (\n" +
                    "SELECT empl_localiz_ID FROM empleados \n" +
                    "WHERE empl_ID = ?);");
            preparedStatement.setString(1, employed.getCity());
            preparedStatement.setString(2, employed.getAddress());
            preparedStatement.setString(3, employed.getID());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("UPDATE recursoshumanos.empleados\n" +
                    "SET empl_primer_nombre = ?\n" +
                    ", empl_segundo_nombre = ?\n" +
                    ", empl_primer_apellido = ?\n" +
                    ", empl_segundo_apellido = ?\n" +
                    ", empl_email = ?\n" +
                    ", empl_fecha_nac = ?\n" +
                    ", empl_sueldo = ?\n" +
                    ", empl_comision = ?\n" +
                    ", empl_cargo_ID = (SELECT cargo_ID FROM recursoshumanos.cargos WHERE cargo_nombre = ?)\n" +
                    ", empl_gerente_ID = (SELECT empl_ID FROM empl)\n" +
                    ", empl_dpto_ID = (SELECT dpto_ID FROM recursoshumanos.departamentos WHERE dpto_nombre = ?)\n" +
                    "WHERE (empl_ID = ?);");

            preparedStatement.setString(1, employed.getName());
            preparedStatement.setString(2, employed.getLastName());
            preparedStatement.setString(3, employed.getSurname());
            preparedStatement.setString(4, employed.getSecondSurname());
            preparedStatement.setString(5, employed.getEmail());
            preparedStatement.setString(6, employed.getBirthday());
            preparedStatement.setString(7, employed.getSalary());
            preparedStatement.setString(8, employed.getCommission());
            preparedStatement.setString(9, employed.getPosition());
            preparedStatement.setString(10, employed.getDepartment());
            preparedStatement.setString(11, employed.getID());
            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * deleteEmployed: método que permite realizar eliminación lógica de un empleado en la base de datos, previa
     * inserción de este en la tabla de historial
     * @param idUser ID de usuario a eliminar
     * @param retirementDate Fecha de retiro del empleado
     */
    public static void deleteEmployed(int idUser, String retirementDate) {
        startConnection();
        PreparedStatement preparedStatement;
        try {

            preparedStatement = connection.prepareStatement("INSERT INTO historico \n" +
                    "(emphist_ID, emphist_fecha_retiro, emphist_cargo_ID, emphist_dpto_ID) \n" +
                    "VALUES (\n" +
                    "?\n" +
                    ", ?\n" +
                    ", (SELECT empl_cargo_ID FROM empleados WHERE empl_ID = ?)\n" +
                    ", (SELECT empl_dpto_ID FROM empleados WHERE empl_ID = ?)\n" +
                    ");");
            preparedStatement.setInt(1, idUser);
            preparedStatement.setString(2, retirementDate);
            preparedStatement.setInt(3, idUser);
            preparedStatement.setInt(4, idUser);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("UPDATE empleados \n" +
                    "SET empl_estado = 'Retirado' \n" +
                    "WHERE (empl_ID = ?);");
            preparedStatement.setInt(1, idUser);
            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * insertCountry: método para insertar un país en la tabla países de la base de datos
     * @param fieldsCountry atributos del pais a insertar
     */
    public static void insertCountry(ArrayList<String> fieldsCountry) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO paises (pais_ID, pais_nombre) VALUES (?, ?)")) {
            for (int index = 0; index < fieldsCountry.size(); index++) {
                preparedStatement.setString(index + 1, fieldsCountry.get(index));
            }
            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * insertCity: método para insertar una ciudad en la tabla ciudades de la base de datos
     * @param fieldsCity atributos de la ciudad a insertar
     */
    public static void insertCity(ArrayList<String> fieldsCity) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \n" +
                "ciudades (\n" +
                "ciud_ID\n" +
                ", ciud_pais_ID\n" +
                ", ciud_nombre\n" +
                ") values (? \n" +
                ", (SELECT pais_ID FROM paises WHERE pais_nombre = ?)\n" +
                ", ? );")) {
            for (int index = 0; index < fieldsCity.size(); index++) {
                preparedStatement.setString(index + 1, fieldsCity.get(index));
            }
            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * insertLocalization: método para insertar una localización en la tabla localizaciones de la base de datos, se
     * realiza sub consulta en tablas ciudades y departamentos para obtener el ID de la ciudad y el ID del departamento
     * según nombre de la ciudad y el departamento
     * @param fieldsLocalization atributos de la localización a insertar
     */
    public static void insertLocalization(ArrayList<String> fieldsLocalization) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \n" +
                "localizaciones (localiz_ID, localiz_ciudad_ID, localiz_direccion) \n" +
                "VALUES (\n" +
                "?\n" +
                ", (SELECT ciud_ID FROM ciudades WHERE ciud_nombre = ?)\n" +
                ", ?);")) {

            for (int index = 0; index < fieldsLocalization.size(); index++) {
                preparedStatement.setString(index + 1, fieldsLocalization.get(index));
            }

            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * insertPosition: método para insertar un cargo en la tabla cargos de la base de datos
     * @param fieldsPosition atributos del cargo a insertar
     */
    public static void insertPosition(ArrayList<String> fieldsPosition) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement(
                        "INSERT INTO cargos " +
                                "(cargo_ID, cargo_nombre, cargo_sueldo_minimo, cargo_sueldo_maximo) " +
                                "VALUES (?, ?, ?, ?)")) {

            for (int index = 0; index < fieldsPosition.size(); index++) {
                preparedStatement.setString(index + 1, fieldsPosition.get(index));
            }

            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * insertDepartment: método para insertar un departamento en la tabla departamentos de la base de datos
     * @param fieldsDepartment atributos del departamento a insertar
     */
    public static void insertDepartment(ArrayList<String> fieldsDepartment) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement(
                        "INSERT INTO departamentos " +
                                "(dpto_ID, dpto_nombre) VALUES (?, ?)")) {

            for (int index = 0; index < fieldsDepartment.size(); index++) {
                preparedStatement.setString(index + 1, fieldsDepartment.get(index));
            }

            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    /**
     * selectPositions: método que permite consultar todos los cargos en la tabla cargos de la base de datos
     * @return objeto de la clase Message con el tipo de transacción efectuada y el listado de nombres de cargos
     * encontrados
     */
    public static Message selectPositions() {
        Message message = new Message(TransactionType.SELECT_POSITIONS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT cargo_nombre FROM cargos")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> positions = new ArrayList<>();
            while (resultSet.next()) {
                String position = resultSet.getString(1);
                positions.add(position);
            }
            message.setPositions(positions);
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * selectDepartments: método que permite consultar todos los departamentos en la tabla departamentos de la base de datos
     * @return objeto de la clase Message con el tipo de transacción efectuada y el listado de nombres de departamentos
     * encontrados
     */
    public static Message selectDepartments() {
        Message message = new Message(TransactionType.SELECT_DEPARTMENTS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT dpto_nombre FROM departamentos"))     {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> departments = new ArrayList<>();
            while (resultSet.next()) {
                String position = resultSet.getString(1);
                departments.add(position);
            }
            message.setDepartments(departments);
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * selectCities: método que permite consultar todas las ciudades en la tabla ciudades de la base de datos
     * @return objeto de la clase Message con el tipo de transacción efectuada y el listado de nombres de ciudades
     * encontrados
     */
    public static Message selectCities() {
        Message message = new Message(TransactionType.SELECT_CITIES);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT ciud_nombre FROM ciudades")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> cities = new ArrayList<>();
            while (resultSet.next()) {
                String position = resultSet.getString(1);
                cities.add(position);
            }
            message.setCities(cities);
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * selectCountries: método que permite consultar todos los países en la tabla países de la base de datos
     * @return objeto de la clase Message con el tipo de transacción efectuada y el listado de nombres de países
     * encontrados
     */
    public static Message selectCountries() {
        Message message = new Message(TransactionType.SELECT_COUNTRIES);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT pais_nombre FROM paises;"))          {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> countries = new ArrayList<>();
            while (resultSet.next()) {
                String country = resultSet.getString(1);
                countries.add(country);
            }
            message.setCountries(countries);
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * selectLocalizations: método que permite consultar todas las localizaciones correspondientes a los departamentos y que no pertenezcan a los empleados en la tabla localizaciones de la base de datos
     * @return objeto de la clase Message con el tipo de transacción efectuada y el listado de nombres de las localizaciones
     * encontrados
     */
    public static Message selectLocalizations() {
        Message message = new Message(TransactionType.SELECT_LOCALIZATIONS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT localiz_direccion \n" +
                        "FROM localizaciones \n" +
                        "LEFT JOIN departamentos \n" +
                        "ON localiz_ID = dpto_localiz_ID\n" +
                        "WHERE localiz_ID NOT IN (SELECT empl_localiz_ID FROM empleados)\n" +
                        ";")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> localizations = new ArrayList<>();
            while (resultSet.next()) {
                String localization = resultSet.getString(1);
                localizations.add(localization);
            }
            message.setLocalizations(localizations);
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * selectManagers: método que permite consultar todos los gerentes en la tabla cargos de la base de datos
     * @return objeto de la clase Message con el tipo de transacción efectuada y el listado de nombres de las gerentes
     * encontrados
     */
    public static Message selectManagers() {
        Message message = new Message(TransactionType.SELECT_MANAGERS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT cargo_nombre FROM cargos " +
                        "WHERE cargo_nombre LIKE '%Gerente%';")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> managers = new ArrayList<>();
            while (resultSet.next()) {
                String localization = resultSet.getString(1);
                managers.add(localization);
            }
            message.setManagers(managers);
            return message;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }
    /**
     * closeConnection: método que permite manejar el cierre de conexión con la base de datos
     */
    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error cerrando conexión con servidor: " + e.getMessage());
        }
    }
    /**
     * commit: método que permite manejar la confirmación de una transacción exitosa en la base de datos
     * @throws SQLException Excepción por error en manejo de confirmación de transacción
     */
    private static void commit() throws SQLException {
        try {
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }
}
