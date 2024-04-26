package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gr.veterinaryapp.mapper.ClientMapper;
import pl.gr.veterinaryapp.model.dto.ClientRequestDto;
import pl.gr.veterinaryapp.model.dto.ClientResponseDto;
import pl.gr.veterinaryapp.service.ClientService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/clients")
public class ClientRestController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @GetMapping("/{id}")
    public ClientResponseDto getClient(@PathVariable long id) {
        return clientMapper.toClientResponseDto(clientService.getClientById(id));
    }

    @PostMapping
    public ClientResponseDto createClient(@RequestBody ClientRequestDto clientRequestDTO) {
        return clientMapper.toClientResponseDto(clientService.createClient(clientRequestDTO));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        clientService.deleteClient(id);
    }

    @GetMapping
    public List<ClientResponseDto> getAllClients() {
        return clientMapper.toClientResponseDtoList(clientService.getAllClients());
    }
}
