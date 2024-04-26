package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pl.gr.veterinaryapp.mapper.VisitMapper;
import pl.gr.veterinaryapp.model.dto.AvailableVisitDto;
import pl.gr.veterinaryapp.model.dto.VisitEditDto;
import pl.gr.veterinaryapp.model.dto.VisitRequestDto;
import pl.gr.veterinaryapp.model.dto.VisitResponseDto;
import pl.gr.veterinaryapp.service.VisitService;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RequestMapping("api/visits")
@RestController
public class VisitRestController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        visitService.deleteVisit(id);
    }

    @GetMapping("/{id}")
    public VisitResponseDto getVisit(@AuthenticationPrincipal User user, @PathVariable long id) {
        var visit = visitMapper.toVisitResponseDto(visitService.getVisitById(user, id));
        addLinks(visit);
        return visit;
    }

    @GetMapping("/available")
    public List<AvailableVisitDto> getAvailableVisits(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") OffsetDateTime startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") OffsetDateTime endDateTime,
            @RequestParam(required = false) List<Long> vetIds) {
        Set<Long> vetIdsSet;
        if (vetIds == null) {
            vetIdsSet = Collections.emptySet();
        } else {
            vetIdsSet = vetIds
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }

        var availableVisits = visitService
                .getAvailableVisits(startDateTime, endDateTime, vetIdsSet);

        for (var availableVisit : availableVisits) {
            for (var vetId : availableVisit.getVetIds()) {
                availableVisit.add(createVetLink(vetId));
            }
        }
        return availableVisits;
    }

    @GetMapping
    public List<VisitResponseDto> getAllVisits(@AuthenticationPrincipal User user) {
        var visits = visitMapper.toVisitResponseDtoList(visitService.getAllVisits(user));

        for (var visit : visits) {
            addLinks(visit);
            var link = linkTo(methodOn(VisitRestController.class).getVisit(user, visit.getId()))
                    .withSelfRel();
            visit.add(link);
        }

        return visits;
    }

    @PostMapping
    public VisitResponseDto createVisit(@AuthenticationPrincipal User user,
                                        @RequestBody VisitRequestDto visitRequestDto) {
        var visit = visitMapper.toVisitResponseDto(visitService.createVisit(user, visitRequestDto));
        addLinks(visit);
        return visit;
    }

    @PatchMapping
    public VisitResponseDto finalizeVisit(@RequestBody VisitEditDto visitEditDto) {
        var visit = visitMapper.toVisitResponseDto(visitService.finalizeVisit(visitEditDto));
        addLinks(visit);
        return visit;
    }

    public Link createVetLink(long id) {
        return linkTo(VetRestController.class)
                .slash(id)
                .withRel("vet");
    }

    public Link createPetLink(long id) {
        return linkTo(PetRestController.class)
                .slash(id)
                .withRel("pet");
    }

    public void addLinks(VisitResponseDto visitResponseDto) {
        visitResponseDto.add(createVetLink(visitResponseDto.getVetId()),
                createPetLink(visitResponseDto.getPetId()));
    }
}
