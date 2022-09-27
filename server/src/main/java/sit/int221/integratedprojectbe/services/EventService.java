package sit.int221.integratedprojectbe.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;


import sit.int221.integratedprojectbe.dtos.CreateEventDTO;
import sit.int221.integratedprojectbe.dtos.EditEventDTO;
import sit.int221.integratedprojectbe.dtos.EventCategoryDTO;
import sit.int221.integratedprojectbe.dtos.EventDetailsDTO;
import sit.int221.integratedprojectbe.entities.Event;
import sit.int221.integratedprojectbe.entities.EventCategory;
import sit.int221.integratedprojectbe.exceptions.ArgumentNotValidException;
import sit.int221.integratedprojectbe.exceptions.DateTimeOverlapException;
import sit.int221.integratedprojectbe.repositories.EventRepository;
import sit.int221.integratedprojectbe.utils.ListMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCategoryService eventCategoryService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public List<EventDetailsDTO> getEvents() {
        return listMapper.mapList(eventRepository.findAllByOrderByEventStartTimeDesc(), EventDetailsDTO.class, modelMapper);
    }

    public List<EventDetailsDTO> getAllEventsByCategoryId(Integer categoryId) {
        return listMapper.mapList(eventRepository.findAllByCategoryId(categoryId), EventDetailsDTO.class, modelMapper);
    }
    public List<EventDetailsDTO> getAllEventByDate (String eventDate){
        return   listMapper.mapList(eventRepository.findAllByEventStartTimeEquals(eventDate), EventDetailsDTO.class, modelMapper);
    }

    public List<EventDetailsDTO> getAllFutureEvent(){
        return listMapper.mapList(eventRepository.findAllByEventStartTimeAfter(), EventDetailsDTO.class, modelMapper);
    }

    public List<EventDetailsDTO> getAllPastEvent(){
        return listMapper.mapList(eventRepository.findAllByEventStartTimeBefore(), EventDetailsDTO.class, modelMapper);
    }

    public EventDetailsDTO getEventById(Integer bookingId) {
        Event event = eventRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Booking ID %s is doesn't exist.", bookingId)
                ));
        return modelMapper.map(event, EventDetailsDTO.class);
    }

    public EventDetailsDTO addNewEvent(CreateEventDTO newEvent, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ArgumentNotValidException(bindingResult);

        Event event = modelMapper.map(newEvent, Event.class);
        EventCategory eventCategory = eventCategoryService.getCategoryById(newEvent.getCategoryId());
        event.setCategory(eventCategory);
        event.setEventDuration(eventCategory.getEventDuration());

        boolean isOverlap = checkEventPeriodOverlap(event);
        if (isOverlap) {
            throw new DateTimeOverlapException("This time is already reserve");
        }

        return modelMapper.map(eventRepository.saveAndFlush(event), EventDetailsDTO.class);
    }

    public void removeEvent(Integer bookingId) {
        eventRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Booking ID %s is doesn't exist", bookingId)
                ));
        eventRepository.deleteById(bookingId);
    }

    public EventDetailsDTO editEvent(Integer bookingId, EditEventDTO updateEvent, BindingResult bindingResult) {
        Event event = eventRepository.findById(bookingId)
                .map(existingEvent -> mapEvent(existingEvent, updateEvent))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Booking ID %s is doesn't exist.", bookingId)
                ));

        boolean isOverlap = checkEventPeriodOverlap(event);
        if (isOverlap) {
            throw new DateTimeOverlapException("This time is already reserve");
        }

        if (bindingResult.hasErrors()) {
            throw new ArgumentNotValidException(bindingResult);
        }

        return modelMapper.map(eventRepository.saveAndFlush(event), EventDetailsDTO.class);
    }

    private Event mapEvent(Event existingEvent, EditEventDTO updateEvent) {
        if (updateEvent.getEventStartTime() != null) {
            existingEvent.setEventStartTime(updateEvent.getEventStartTime());
        }
        if (updateEvent.getEventNotes() != null) {
            existingEvent.setEventNotes(updateEvent.getEventNotes());
        }
        return existingEvent;
    }

    private boolean checkEventPeriodOverlap(Event event) {
        List<Event> categoryEvents = eventRepository
                .findAllByCategoryIdAndEventStartTimeAfter(event.getCategory().getCategoryId());

        Instant eventCheckStart = event.getEventStartTime().toInstant();
        Instant eventCheckEnd = event.getEventStartTime().toInstant().plus(event.getEventDuration(), ChronoUnit.MINUTES);

        return categoryEvents.stream().anyMatch(e -> {
            if (event.getBookingId() == null || !event.getBookingId().equals(e.getBookingId())) {
                Instant eventStartTime = e.getEventStartTime().toInstant();
                Instant eventEndTime = e.getEventStartTime().toInstant().plus(e.getEventDuration(), ChronoUnit.MINUTES);
                return isEventPeriodOverlap(eventStartTime, eventEndTime, eventCheckStart, eventCheckEnd);
            }
            return false;
        });
    }

    private boolean isEventPeriodOverlap(Instant startTimeA , Instant endTimeA, Instant startTimeB, Instant endTimeB){
        if (startTimeA.isBefore(endTimeB) && startTimeB.isBefore(endTimeA)) {
                System.out.println("This time is already reserve");
                return true;
        }

//        if (startTimeA.isBefore(endTimeB)) {
//            if (endTimeB.isBefore(endTimeA)) {
//                System.out.println("Time range intersects with other end time");
//                return true;
//            }
//        }

        return false;
    }




}





