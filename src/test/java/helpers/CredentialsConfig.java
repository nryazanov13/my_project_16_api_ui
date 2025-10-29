package helpers;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/credentials.properties")
public interface CredentialsConfig extends Config {
    String selenoidLogin();
    String selenoidPassword();
    String demoqaUserLogin();
    String demoqaUserPassword();
}