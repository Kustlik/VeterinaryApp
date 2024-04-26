package pl.gr.veterinaryapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gr.veterinaryapp.exception.IncorrectDataException;
import pl.gr.veterinaryapp.exception.ResourceNotFoundException;
import pl.gr.veterinaryapp.model.dto.PetRequestDto;
import pl.gr.veterinaryapp.model.entity.Animal;
import pl.gr.veterinaryapp.model.entity.Client;
import pl.gr.veterinaryapp.model.entity.Pet;
import pl.gr.veterinaryapp.repository.AnimalRepository;
import pl.gr.veterinaryapp.repository.ClientRepository;
import pl.gr.veterinaryapp.repository.PetRepository;
import pl.gr.veterinaryapp.service.PetService;
import pl.gr.veterinaryapp.service.impl.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final UserValidator userValidator;
    private final PetRepository petRepository;
    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;

    @Override
    public List<Pet> getAllPets(User user) {
        return petRepository.findAll()
                .stream()
                .filter(pet -> userValidator.isUserAuthorized(user, pet.getClient()))
                .collect(Collectors.toList());
    }

    @Override
    public Pet getPetById(User user, long id) {
        Pet pet = getPet(id);

        userValidator.isUserAuthorized(user, pet.getClient());

        return pet;
    }

    @Transactional
    @Override
    public Pet createPet(User user, PetRequestDto petRequestDto) {
        if (petRequestDto.getName() == null) {
            throw new IncorrectDataException("Name cannot be null.");
        }

        if (petRequestDto.getBirthDate() == null) {
            throw new IncorrectDataException("Birth date cannot be null.");
        }

        Animal animal = animalRepository.findById(petRequestDto.getAnimalId())
                .orElseThrow(() -> new IncorrectDataException("Wrong animal id."));
        Client client = clientRepository.findById(petRequestDto.getClientId())
                .orElseThrow(() -> new IncorrectDataException("Wrong client id."));

        if (!userValidator.isUserAuthorized(user, client)) {
            throw new ResourceNotFoundException("User don't have access to this pet");
        }

        var newPet = getNewPet(petRequestDto, animal, client);

        return petRepository.save(newPet);
    }

    private static Pet getNewPet(PetRequestDto petRequestDto, Animal animal, Client client) {
        var newPet = new Pet();
        newPet.setName(petRequestDto.getName());
        newPet.setBirthDate(petRequestDto.getBirthDate());
        newPet.setAnimal(animal);
        newPet.setClient(client);
        return newPet;
    }

    @Transactional
    @Override
    public void deletePet(long id) {
        Pet result = getPet(id);
        petRepository.delete(result);
    }

    private Pet getPet(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wrong id."));
    }
}
