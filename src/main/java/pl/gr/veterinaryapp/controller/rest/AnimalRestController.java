package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.gr.veterinaryapp.model.dto.AnimalRequestDto;
import pl.gr.veterinaryapp.model.entity.Animal;
import pl.gr.veterinaryapp.service.AnimalService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/animals")
public class AnimalRestController {

    private final AnimalService animalService;

    @GetMapping("/{id}")
    public Animal getAnimal(@PathVariable long id) {
        return animalService.getAnimalById(id);
    }

    @PostMapping
    public Animal createAnimal(@RequestBody AnimalRequestDto animalRequestDTO) {
        return animalService.createAnimal(animalRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        animalService.deleteAnimal(id);
    }

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimals();
    }
}
