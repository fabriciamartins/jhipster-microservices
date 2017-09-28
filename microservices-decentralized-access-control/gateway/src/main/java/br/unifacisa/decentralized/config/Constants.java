package br.unifacisa.decentralized.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    //Microservices URL
    public static final String MICROSERVICE_PRODUTO = "http://localhost:8081";
    public static final String MICROSERVICE_PEDIDO = "http://localhost:8082";

    private Constants() {
    }
}
