package helpers;

//import org.aeonbits.owner.Config;

//@Config.Sources("classpath:config/credentials.properties")
public interface CredentialsConfig {
    String login();
    String password();
}