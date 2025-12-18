## <a href="https://demoqa.com/"><img alt="DemoQa" height="52" src="images/logo/logo.jpg" width="134"/></a>
# Проект по автоматизации тестирования для проекта [DemoQa](https://demoqa.com/)

* [Репозиторий с проектом](https://github.com/nryazanov13/my_project_16_api_ui/tree/configuration)

## **Содержание:**
____

* <a href="#tools">Технологии и инструменты</a>

* <a href="#cases">Примеры автоматизированных тест-кейсов</a>

* <a href="#jenkins">Сборка в Jenkins</a>

* <a href="#console">Запуск из терминала</a>

* <a href="#allure">Allure отчет</a>

____
<a id="tools"></a>
## <a name="Технологии и инструменты">**Технологии и инструменты:**</a>

<p align="center">  
<a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50"  alt="Java"/></a>  
<a href="https://github.com/"><img src="images/logo/GitHub.svg" width="50" height="50"  alt="GitHub"/></a>  
<a href="https://junit.org/junit5/"><img src="images/logo/JUnit5.svg" width="50" height="50"  alt="JUnit 5"/></a>  
<a href="https://gradle.org/"><img src="images/logo/Gradle.svg" width="50" height="50"  alt="Gradle"/></a>  
<a href="https://selenide.org/"><img src="images/logo/Selenide.svg" width="50" height="50"  alt="Selenide"/></a>  
<a href="https://aerokube.com/selenoid/"><img src="images/logo/Selenoid.svg" width="50" height="50"  alt="Selenoid"/></a>  
<a href="https://allurereport.org/"><img src="images/logo/Allure.svg" width="50" height="50"  alt="Allure"/></a>  
<a href="https://www.jenkins.io/"><img src="images/logo/Jenkins.svg" width="50" height="50"  alt="Jenkins"/></a>   
</p>

____
<a id="cases"></a>
## <a name="Примеры автоматизированных тест-кейсов">**Примеры автоматизированных тест-кейсов:**</a>
____
API тесты
- ✓ *Проверка генерации токена*
- ✓ *Проверка добавленной книги в профиль пользователя через API*
- ✓ *Проверка удаления книги из профиля пользователя через API*

API и UI тесты
- ✓ *Проверка добавленной книги в профиль пользователя через UI*
- ✓ *Проверка удаления книги из профиля пользователя через UI»*

____
<a id="jenkins"></a>
## <img alt="Jenkins" height="25" src="images/logo/Jenkins.svg" width="25"/></a><a name="Сборка"></a>Сборка в [Jenkins](https://jenkins.autotests.cloud/job/nryazanov_my_project_16/)</a>
____
<p align="center">  
<a href="https://jenkins.autotests.cloud/job/nryazanov_my_project_16/"><img src="images/screen/Jenkins.png" alt="Jenkins" width="950"/></a>  
</p>


### **Параметры сборки в Jenkins:**

- *browser (браузер, по умолчанию chrome)*
- *version (версия браузера, по умолчанию 128)*
- *windowSize (размер окна браузера, по умолчанию 1920x1080)*
- *remoteHost (адрес удаленного сервера Selenoid)*

<a id="console"></a>
### Команды для запуска из терминала
___
***Локальный запуск всех тестов:***

- Denv (запуск локально/удаленно, по умолчанию локально)
```bash  
./gradlew clean test -Denv=local
```
***Удаленный запуск всех тестов локально:***
```bash  
./gradlew clean test -Denv=remote
```

***Удалённый запуск через Jenkins:***
```bash  
clean test
-Dbrowser=${browser}
-DbrowserVersion=${browserVersion}
-DbrowserSize=${browserSize}
-DremoteHost=${remoteHost}
```
___
<a id="allure"></a>
## <img alt="Allure" height="25" src="images/logo/Allure.svg" width="25"/></a> <a name="Allure"></a>Allure [отчет](https://jenkins.autotests.cloud/job/037-sandraboticelli-escaperoom-12/allure/)</a>
___

### *Основная страница отчёта*

<p align="center">  
<img title="Allure Overview Dashboard" src="images/screen/allure_report_1.png" width="850">  
</p>  

### *Тест-кейсы*

<p align="center">  
<img title="Allure Tests" src="images/screen/allure_report_4.png" width="850">  
</p>

### *Графики*

  <p align="center">  
<img title="Allure Graphics" src="images/screen/allure_report_2.png" width="850">
</p>

