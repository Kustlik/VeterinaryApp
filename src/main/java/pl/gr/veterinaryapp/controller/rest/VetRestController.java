package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gr.veterinaryapp.model.dto.VetRequestDto;
import pl.gr.veterinaryapp.model.entity.Vet;
import pl.gr.veterinaryapp.service.VetService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/vets")
public class VetRestController {

    private final VetService vetService;

    @GetMapping("/{id}")
    public Vet getVet(@PathVariable long id) {
        return vetService.getVetById(id);
    }

    @PostMapping
    public Vet addVet(@RequestBody VetRequestDto vetRequestDTO) {
        return vetService.createVet(vetRequestDTO);
    }

    @GetMapping
    public List<Vet> getAllVets() {
        return vetService.getAllVets();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        vetService.deleteVet(id);
    }
}
