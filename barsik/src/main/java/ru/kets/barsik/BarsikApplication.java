package ru.kets.barsik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BarsikApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(BarsikApplication.class, args);
		} catch (Throwable e) {
			// Причину уже залогировал Spring. Потоки JDA не дают JVM завершиться самой, и контейнер
			// оставался бы "Up" с неработающим ботом. Выходим с ошибкой, чтобы Docker его перезапустил.
			System.exit(1);
		}
	}

}
