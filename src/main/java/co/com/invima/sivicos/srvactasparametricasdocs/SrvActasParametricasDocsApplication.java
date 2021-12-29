package co.com.invima.sivicos.srvactasparametricasdocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "co.com.invima")
@RequiredArgsConstructor
public class SrvActasParametricasDocsApplication {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(SrvActasParametricasDocsApplication.class, args);
	}
	
}
