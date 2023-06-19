package cliente;

import java.util.EventListener;

/**
 * Clase abstracta que define los eventos personalizados para la clase Client, al percibir cambio de los atributos
 * message y destinies a través de los métodos modificadores setters. Implementa la interfaz EventListener, por
 * obligatoriedad de escucha de eventos. Permite crear instancias que implementen uno o todos los métodos
 * abstractos de esta.
 * @Author Jorge Luis Velasquez
 */
public abstract class EventChangeClientListener implements EventListener {
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo message de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onMessageChange(EventChangeClient event){};
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo positions de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onPositions(EventChangeClient event){};
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo departments de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onDepartments(EventChangeClient event){};
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo cities de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onCities(EventChangeClient event) {
    }
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo countries de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onCountries(EventChangeClient event) {
    }
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo localizations de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onLocalizations(EventChangeClient event) {
    }
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo employees de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onEmployees(EventChangeClient event) {
    }
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo employed de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onEmployed(EventChangeClient event) {
    }
    /**
     * Método abstracto para implementar lógica del manejo de evento al cambiar el atributo managers de la clase Client
     * @param event evento ocurrido en el objeto padre
     */
    void onManagers(EventChangeClient event)                                        {
    }
}
