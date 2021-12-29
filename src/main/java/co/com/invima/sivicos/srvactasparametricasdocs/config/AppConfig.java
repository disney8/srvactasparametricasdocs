package co.com.invima.sivicos.srvactasparametricasdocs.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/*
    User:Eduardo Noel<enoel@soaint.com>
    Date: 1/7/21
    Time: 15:03
*/
@Configuration
@ComponentScan({"co.com.invima.canonicalmodelsivico.dtosivico"})
@EntityScan("co.com.invima.canonicalmodelsivico.entitysivico")
public class AppConfig {

    private static final String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATEFORMAT));

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {

        return builder -> builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializers(LOCAL_DATETIME_SERIALIZER);
    }

}
