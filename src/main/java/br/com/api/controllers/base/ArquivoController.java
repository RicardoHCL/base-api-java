package br.com.api.controllers.base;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.api.constants.UrlConstantes;
import br.com.api.dtos.UploadArquivoDTO;
import br.com.api.services.base.ArmazenarArquivoService;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Arquivos", description = "Endpoint para efetuar o upload e download dos Arquivos", tags = "Arquivos")
@RequestMapping(UrlConstantes.ARQUIVOS)
public class ArquivoController {
	
	//TODO FINALIZAR IMPLEMENTAÇAO PARA VINCULAR OS ARQUIVOS A SEUS DONOS E SALVAR NO BANCO

	private static Logger logger = Logger.getLogger(ArquivoController.class);

	@Autowired
	private ArmazenarArquivoService service;

	@PostMapping("/upload")
	public UploadArquivoDTO upload(@RequestParam("arquivo") MultipartFile arquivo) {
		String nomeArquivo = this.service.armazenarArquivo(arquivo);

		String uriDownload = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(UrlConstantes.ARQUIVOS + "/download/").path(nomeArquivo).toUriString();

		return new UploadArquivoDTO(nomeArquivo, uriDownload, arquivo.getContentType(), arquivo.getSize());
	}

	@PostMapping("/uploads")
	public List<UploadArquivoDTO> uploads(@RequestParam("arquivos") MultipartFile[] arquivos) {
		return Arrays.asList(arquivos).stream().map(arquivo -> this.upload(arquivo)).collect(Collectors.toList());
	}

	@GetMapping("/download/{nomeArquivo:.+}")
	public ResponseEntity<Resource> download(@PathVariable String nomeArquivo, HttpServletRequest request) {

		Resource arquivo = this.service.baixarArquivo(nomeArquivo);
		String tipoArquivo = null;

		try {
			tipoArquivo = request.getServletContext().getMimeType(arquivo.getFile().getAbsolutePath());
		} catch (Exception e) {
			logger.info("Não foi possível determinar o tipo do arquivo");
		}

		if (tipoArquivo == null)
			tipoArquivo = "application/octet-stream";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(tipoArquivo))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; nomeArquivo=\"" + arquivo.getFilename() + "\"")
				.body(arquivo);
	}

}
