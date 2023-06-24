package connection;

import lombok.Data;

import java.io.Serializable;

@Data
public class Employed implements Serializable {

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

}
