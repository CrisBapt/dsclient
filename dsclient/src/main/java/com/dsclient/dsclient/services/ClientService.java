package com.dsclient.dsclient.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsclient.dsclient.dtos.ClientDTO;
import com.dsclient.dsclient.entities.Client;
import com.dsclient.dsclient.repositories.ClientRepository;
import com.dsclient.dsclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> clients = repository.findAll();
		
		return clients.stream().map(c -> new ClientDTO(c)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		
		Client client = obj.orElseThrow(() -> new ResourceNotFoundException("ENtity Not Found")); 
		
		return new ClientDTO(client);
	}
	
	
	
}
