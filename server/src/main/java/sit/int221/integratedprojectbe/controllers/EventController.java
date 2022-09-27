package sit.int221.integratedprojectbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.integratedprojectbe.dtos.CreateEventDTO;
import sit.int221.integratedprojectbe.dtos.EditEventDTO;
import sit.int221.integratedprojectbe.dtos.EventDetailsDTO;
import sit.int221.integratedprojectbe.entities.Event;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.exceptions.DateTimeOverlapException;
import sit.int221.integratedprojectbe.services.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/events")


public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public List<EventDetailsDTO> getAllEvents(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String eventDate)
    {
       if(sort != null){
           if(sort.equals("past")){
               return eventService.getAllPastEvent();
           }
           if(sort.equals("upcoming")){
             return  eventService.getAllFutureEvent();
           }
           if(sort.equals("date") && eventDate != null){
               return eventService.getAllEventByDate(eventDate);
           }
           if(sort.equals("category")){
               return eventService.getAllEventsByCategoryId(categoryId);
           }
       }
       return eventService.getEvents();
    }

    @GetMapping("/{bookingId}")
    public EventDetailsDTO getEventsByBookingId(@PathVariable Integer bookingId){
         return eventService.getEventById(bookingId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDetailsDTO create(@Valid @RequestBody CreateEventDTO newEvent, BindingResult bindingResult) {
        try {
            return eventService.addNewEvent(newEvent, bindingResult);
        } catch (DateTimeOverlapException ex) {
            FieldError error = new FieldError("createEventDTO", "eventStartTime", ex.getMessage());
            bindingResult.addError(error);
            throw new ArgumentNotValidException(bindingResult);
        } catch (ArgumentNotValidException ex) {
            throw ex;
        }
    }
    
    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Integer bookingId) {
        eventService.removeEvent(bookingId);
    }

    @PatchMapping("/{bookingId}")
    public EventDetailsDTO update(@Valid @RequestBody EditEventDTO editEvent,
                                  BindingResult bindingResult, @PathVariable Integer bookingId)
    {
        try {
            return eventService.editEvent(bookingId, editEvent, bindingResult);
        } catch (DateTimeOverlapException ex) {
            FieldError error = new FieldError("editEventDTO", "eventStartTime", "overlap");
            bindingResult.addError(error);
            throw new ArgumentNotValidException(bindingResult);
        } catch (ArgumentNotValidException ex) {
            throw ex;
        }
    }

}
