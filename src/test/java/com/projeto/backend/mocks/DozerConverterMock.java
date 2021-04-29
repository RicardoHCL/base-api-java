//package com.projeto.backend.mocks;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.projeto.backend.dtos.outputs.UsuarioOutDTO;
//import com.projeto.backend.models.Usuario;
//
//public class DozerConverterMock {
//	
//	public Usuario mockEntity() {
//		return mockEntity(0);
//	}
//
//	public UsuarioOutDTO mockDTO() {
//		return mockDTO(0);
//	}
//
//	public List<Usuario> mockEntityList() {
//		List<Usuario> usuarios = new ArrayList<Usuario>();
//		for (int i = 0; i < 14; i++) {
//			usuarios.add(mockEntity(i));
//		}
//		return usuarios;
//	}
//
//	public List<UsuarioOutDTO> mockDTOList() {
//		List<UsuarioOutDTO> usuarios = new ArrayList<>();
//		for (int i = 0; i < 14; i++) {
//			usuarios.add(mockDTO(i));
//		}
//		return usuarios;
//	}
//
//	private Usuario mockEntity(Integer number) {
//		Usuario usuario = new Usuario();
//		usuario.setEmail("Email Test" + number);
//		usuario.setNome("Nome Test" + number);
//		usuario.setSenha("Senha Test" + number);
//		usuario.setId(number.longValue());	
//		return usuario;
//	}
//
//	private UsuarioOutDTO mockDTO(Integer number) {
//		UsuarioOutDTO usuario = new UsuarioOutDTO();
//		usuario.setEmail("Email Test" + number);
//		usuario.setNome("Nome Test" + number);
//		usuario.setId(number.longValue());	
//		return usuario;
//	}
//}
