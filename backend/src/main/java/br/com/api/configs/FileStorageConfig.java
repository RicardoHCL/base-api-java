package br.com.api.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties(prefix = "file")
public class FileStorageConfig {

	private String diretorioUpload;
}
