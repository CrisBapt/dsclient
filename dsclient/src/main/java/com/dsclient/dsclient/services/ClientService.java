package com.dsclient.dsclient.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsclient.dsclient.dtos.ClientDTO;
import com.dsclient.dsclient.entities.Client;
import com.dsclient.dsclient.repositories.ClientRepository;
import com.dsclient.dsclient.services.exceptions.DatabaseException;
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
		
		Client client = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found")); 
		
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client client = new Client();

		client.setName(dto.getName());
		client.setCpf(dto.getCpf());		
		client.setBirthDate(dto.getBirthDate());		
		client.setIncome(dto.getIncome());		
		client.setChildren(dto.getChildren());		

		client = repository.save(client);

		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client client = repository.getOne(id);

			client.setName(dto.getName());
			client.setCpf(dto.getCpf());		
			client.setBirthDate(dto.getBirthDate());		
			client.setIncome(dto.getIncome());		
			client.setChildren(dto.getChildren());	
			
			client = repository.save(client);

			return new ClientDTO(client);
		} 
		catch(ResourceNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e)  {
			throw new DatabaseException("Integrity violation");
		}
	}
	
}
