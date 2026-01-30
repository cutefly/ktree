package kr.co.kpcard.ktree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("kr.co.kpcard.ktree.mapper")
public class KtreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KtreeApplication.class, args);
	}

}
