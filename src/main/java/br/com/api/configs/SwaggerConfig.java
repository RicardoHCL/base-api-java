package br.com.api.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.api.constants.SwaggerConstantes;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(SwaggerConstantes.CONTROLLERS_PACKAGE))
				.paths(PathSelectors.any()).build().useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.POST, responseMessageForPOST()).apiInfo(apiInfo());
	}

	private List<ResponseMessage> responseMessageForPOST() {
		return new ArrayList<ResponseMessage>() {

			private static final long serialVersionUID = 1L;

			{
				add(new ResponseMessageBuilder().code(500).message("XX").build()); //TODO IMPLEMENTAR MENSAGENS
				add(new ResponseMessageBuilder().code(400).message("XX").build());
			}
		};
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(SwaggerConstantes.SWEGGER_CONFIG_TITULO)
				.description(SwaggerConstantes.SWEGGER_CONFIG_DESCRICAO)
				.version(SwaggerConstantes.SWEGGER_CONFIG_VERSAO)
				.contact(new Contact(SwaggerConstantes.SWEGGER_CONFIG_CONTATO_NOME,
						SwaggerConstantes.SWEGGER_CONFIG_CONTATO_WEBSITE,
						SwaggerConstantes.SWEGGER_CONFIG_CONTATO_EMAIL))
				.build();
	}

}
