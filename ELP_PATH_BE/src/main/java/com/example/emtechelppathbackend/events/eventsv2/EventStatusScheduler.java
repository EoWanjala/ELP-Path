package com.example.emtechelppathbackend.events.eventsv2;

import com.example.emtechelppathbackend.events.EventStatus;
import com.example.emtechelppathbackend.events.eventsv2.EventsServicev2;
import com.example.emtechelppathbackend.events.eventsv2.EventsV2;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventStatusScheduler {

    private final EventsServicev2 eventService;
    private final EventsRepov2 eventsRepository;

    @Scheduled(fixedRate = 60000)//60000 milliseconds so as to update every 1 minute
    public void updateActivityStatuses() {
        List<EventsV2> events = eventsRepository.findAll();

        //filtering only the events which are not already updated to past
        List<EventsV2> eventsToUpdate = events.stream()
                .filter(activity -> activity.getEventStatus() != EventStatusv2.PAST)
                .toList();

        for (EventsV2 event : eventsToUpdate) {
            eventService.updateEventStatus(event);
            eventsRepository.save(event);
        }
    }
}


