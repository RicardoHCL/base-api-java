package br.com.api.services.base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.api.configs.FileStorageConfig;
import br.com.api.constants.ExceptionsConstantes;
import br.com.api.exceptions.base.FileNotFoundException;
import br.com.api.exceptions.base.FileStorageException;

@Service
public class ArmazenarArquivoService {

	private final Path localArquivoArmazenado;
	
	@Autowired
	public ArmazenarArquivoService(FileStorageConfig config) {
		this.localArquivoArmazenado = Paths.get(config.getDiretorioUpload()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.localArquivoArmazenado);
		} catch (Exception ex) {
			throw new FileStorageException(ExceptionsConstantes.FALHA_AO_CRIAR_DIRETORIO, ex);
		}
	}
	
	public String armazenarArquivo(MultipartFile arquivo) {
		String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
		String nomeArquivofinal = LocalDateTime.now().getNano() + "_" + StringUtils.cleanPath(arquivo.getOriginalFilename());
		
		try {
			
			if(nomeArquivo.contains("..")) {
				throw new FileStorageException(MessageFormat.format(ExceptionsConstantes.ARQUIVO_COM_NOME_INVALIDO, nomeArquivo));
			}
			
			Path localArmazenamento = this.localArquivoArmazenado.resolve(nomeArquivofinal);
			Files.copy(arquivo.getInputStream(), localArmazenamento, StandardCopyOption.REPLACE_EXISTING);
			
			return nomeArquivofinal;
		} catch (Exception ex) {
			throw new FileStorageException(MessageFormat.format(ExceptionsConstantes.FALHA_AO_ARMAZENAR_ARQUIVO, nomeArquivo), ex);
		}
	}
	
	public Resource baixarArquivo(String nomeArquivo) {
		try {
			Path localArmazenamento = this.localArquivoArmazenado.resolve(nomeArquivo).normalize();
			Resource arquivo = new UrlResource(localArmazenamento.toUri());
			
			if(arquivo.exists())
				return arquivo;
			
			throw new FileNotFoundException(MessageFormat.format(ExceptionsConstantes.ARQUIVO_NAO_ENCONTRADO, nomeArquivo));
		} catch (Exception ex) {
			throw new FileNotFoundException(MessageFormat.format(ExceptionsConstantes.ARQUIVO_NAO_ENCONTRADO, nomeArquivo), ex);
		}
	}
}
