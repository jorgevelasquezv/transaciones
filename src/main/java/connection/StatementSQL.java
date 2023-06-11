package connection;

import java.sql.*;
import java.sql.Connection;
import java.util.*;

final public class StatementSQL {
    private static Connection connection;

    private static void startConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/recursoshumanos?" +
                    "user=Poli01&password=abc123");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Error de conexión con el servidor: " + e.getMessage());
        }
    }

    public static ArrayList<ArrayList<String>> findAllEmployees(String employedStatus) {
        startConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT \n" +
                    "  empl_ID\n" +
                    ", empl_primer_nombre\n" +
                    ", empl_segundo_nombre\n" +
                    ", empl_primer_apellido\n" +
                    ", empl_segundo_apellido\n" +
                    ", c.cargo_nombre\n" +
                    ", d.dpto_nombre\n" +
                    "FROM empleados \n" +
                    "INNER JOIN cargos c \n" +
                    "ON c.cargo_ID = empl_cargo_ID \n" +
                    "INNER JOIN departamentos d \n" +
                    "ON d.dpto_ID = empl_dpto_ID\n" +
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
            commit();
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

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
                ", cg.cargo_nombre AS Gerencia \n" +
                "FROM recursoshumanos.empleados empl \n" +
                "INNER JOIN recursoshumanos.cargos c \n" +
                "ON c.cargo_ID = empl_cargo_ID \n" +
                "INNER JOIN recursoshumanos.departamentos d \n" +
                "ON d.dpto_ID = empl_dpto_ID\n" +
                "INNER JOIN recursoshumanos.empleados e \n" +
                "ON empl.empl_gerente_ID = e.empl_ID \n" +
                "INNER JOIN recursoshumanos.cargos cg \n" +
                "ON e.empl_cargo_ID = cg.cargo_ID \n" +
                "WHERE empl.empl_ID = ? \n" +
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
            commit();
            return fieldsEmployed;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    public static void insertEmployed(ArrayList<String> fieldsEmployed) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO recursoshumanos.empleados\n" +
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
                ", empl_cargo_ID\n" +
                ", empl_gerente_ID\n" +
                ", empl_dpto_ID\n" +
                ") \n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ", (select cargo_ID from recursoshumanos.cargos where cargo_nombre = ?)\n" +
                ", (select e.empl_ID from recursoshumanos.empleados e where empl_cargo_ID = (select cargo_ID from recursoshumanos.cargos where cargo_nombre = ?))\n" +
                ", (select dpto_ID from recursoshumanos.departamentos where dpto_nombre = ?)\n" +
                ");")) {
            preparedStatement.setInt(1, Integer.parseInt(fieldsEmployed.get(0)));
            for (int index = 1; index < fieldsEmployed.size(); index++) {
                preparedStatement.setString(index + 1, fieldsEmployed.get(index));
            }

            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    public static void updateEmployed(ArrayList<String> fieldsEmployed) {
        startConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("CREATE TEMPORARY TABLE empl \n" +
                    "AS  (\n" +
                    "SELECT e.empl_ID FROM recursoshumanos.empleados e \n" +
                    "WHERE e.empl_cargo_ID = (\n" +
                    "SELECT cargo_ID FROM recursoshumanos.cargos \n" +
                    "WHERE cargo_nombre = ?));");
            preparedStatement.setString(1, fieldsEmployed.get(0));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("UPDATE recursoshumanos.empleados\n" +
                    "SET empl_ID = ?\n" +
                    ", empl_primer_nombre = ?\n" +
                    ", empl_segundo_nombre = ?\n" +
                    ", empl_primer_apellido = ?\n" +
                    ", empl_segundo_apellido = ?\n" +
                    ", empl_email = ?\n" +
                    ", empl_fecha_nac = ?\n" +
                    ", empl_sueldo = ?\n" +
                    ", empl_comision = ?\n" +
                    ", empl_cargo_ID = (select cargo_ID from recursoshumanos.cargos where cargo_nombre = ?)\n" +
                    ", empl_gerente_ID = (select empl_ID from empl)\n" +
                    ", empl_dpto_ID = (select dpto_ID from recursoshumanos.departamentos where dpto_nombre = ?)\n" +
                    "WHERE (empl_ID = ?);");

            for (int index = 1; index < fieldsEmployed.size(); index++) {
                preparedStatement.setString(index, fieldsEmployed.get(index));
            }

            preparedStatement.executeUpdate();
            commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

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

    public static void insertCountry(ArrayList<String> fieldsCountry) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO paises (pais_ID, pais_nombre) VALUES (?, ?)")) {
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

    public static void insertLocalization(ArrayList<String> fieldsLocalization) {
        startConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO \n" +
                "localizaciones (localiz_ID, localiz_ciudad_ID, localiz_dpto_ID, localiz_direccion) \n" +
                "VALUES (\n" +
                "?\n" +
                ", (SELECT ciud_ID FROM ciudades WHERE ciud_nombre = ?)\n" +
                ", (SELECT dpto_ID FROM departamentos WHERE dpto_nombre = ?)\n" +
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

    public static Message selectDepartments() {
        Message message = new Message(TransactionType.SELECT_DEPARTMENTS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT dpto_nombre FROM departamentos")) {
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

    public static Message selectCountries() {
        Message message = new Message(TransactionType.SELECT_COUNTRIES);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT pais_nombre FROM paises;")) {
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

    public static Message selectLocalizations() {
        Message message = new Message(TransactionType.SELECT_LOCALIZATIONS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT localiz_direccion FROM localizaciones;")) {
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

    public static Message selectManagers() {
        Message message = new Message(TransactionType.SELECT_MANAGERS);
        startConnection();
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT cargo_nombre FROM recursoshumanos.cargos WHERE cargo_nombre LIKE '%Gerente%';")) {
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

    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error cerrando conexión con servidor: " + e.getMessage());
        }
    }

    private static void commit() throws SQLException {
        try {
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

}
