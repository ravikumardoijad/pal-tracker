package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private final Map<Long, TimeEntry> database = new HashMap<>();
    private long timeEntryId = 1L;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = new TimeEntry(timeEntryId,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours());
        database.put(timeEntryId, createdTimeEntry);
        TimeEntry getTimeEntry = database.get(timeEntryId);

        timeEntryId++;
        return getTimeEntry;
    }

    @Override
    public TimeEntry update(Long id, TimeEntry expectedTimeEntry) {
        if (!database.containsKey(id)) {
            return null;
        }

        TimeEntry timeEntryOld = database.get(id);
        expectedTimeEntry.setId(timeEntryOld.getId());
        database.put(id, expectedTimeEntry);

        return database.get(id);
    }

    @Override
    public void delete(Long id) {
        TimeEntry deletedTimeEntry = database.get(id);
        database.remove(id, deletedTimeEntry);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(database.values());
    }

    @Override
    public TimeEntry find(Long id) {
        return database.get(id);
    }
}

