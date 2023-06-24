package connection;

import lombok.Data;

import java.io.Serializable;

/**
 * Clase EmployedDTO de tipo data transfer object para manipulación y transferencia de objetos con los atributos del empleado entre sockets
 * @Author Jorge Luis Velasquez
 */
@Data
public class EmployedDTO implements Serializable                            {

    private String ID;

    private String name;

    private String lastName;

    private String surname;

    private String secondSurname;

    private String email;

    private String birthday;

    private String salary;

    private String commission;

    private String position;

    private String department;

    private String city;

    private String address;

    private String manager;

    private String status;

}
