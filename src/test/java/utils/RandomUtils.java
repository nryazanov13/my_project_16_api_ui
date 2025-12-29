package utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RandomUtils {

    public static Faker faker = new Faker(Locale.ENGLISH);

    public static String getRandomFirstName() {
        return faker.name().firstName();
    }
}
