package helpers;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/webtest-${webtest.env:local}.properties")
public interface WebTestConfig extends Config {

    @Key("browser.name")
    @DefaultValue("chrome")
    String browserName();

    @Key("browser.version")
    @DefaultValue("latest")
    String browserVersion();

    @Key("run.remote")
    @DefaultValue("false")
    boolean runRemote();

    @Key("remote.host")
    @DefaultValue("selenoid.autotests.cloud")
    String remoteHost();

    @Key("browser.size")
    @DefaultValue("1920x1080")
    String browserSize();

    @Key("enable.vnc")
    @DefaultValue("true")
    boolean enableVnc();

    @Key("enable.video")
    @DefaultValue("true")
    boolean enableVideo();

    @Key("timeout.page.load")
    @DefaultValue("30")
    int pageLoadTimeout();
}