package helpers;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/credentials.properties")
public interface CredentialsConfig extends Config {

    @Key("selenoid.login")
    String selenoidLogin();

    @Key("selenoid.password")
    String selenoidPassword();

    @Key("demoqa.login")
    String demoqaUserLogin();

    @Key("demoqa.password")
    String demoqaUserPassword();
}