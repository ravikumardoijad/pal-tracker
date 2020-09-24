package io.pivotal.pal.tracker;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;

import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;


    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Date sqlDate = Date.valueOf(timeEntry.getDate());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, sqlDate);
            ps.setLong(4, timeEntry.getHours());

            return ps;
        }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());

        return timeEntry;
    }

    @Override
    public TimeEntry find(Long timeEntryId) {

        List<TimeEntry> result = jdbcTemplate.query("SELECT * FROM time_entries WHERE id=" + timeEntryId, new RowMapper<TimeEntry>() {
            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                TimeEntry timeEntryMapper = new TimeEntry(rs.getLong("id"),
                        rs.getLong("project_id"),
                        rs.getLong("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("hours"));
                return timeEntryMapper;
            }
        });
        if (result == null || result.isEmpty()){
            return null;
        }
        return result.get(0);
    }



    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> result = jdbcTemplate.query("SELECT * FROM time_entries", new RowMapper<TimeEntry>() {
            @Override
            public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                TimeEntry timeEntryMapper = new TimeEntry(rs.getLong("id"),
                        rs.getLong("project_id"),
                        rs.getLong("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("hours"));
                return timeEntryMapper;
            }
        });
        return result;
    }

    @Override
    public TimeEntry update(Long timeEntryId, TimeEntry timeEntry) {
        String sql = "UPDATE time_entries set project_id=?, user_id=?, date=?, hours=? where id=?";
        jdbcTemplate.update(sql,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                timeEntryId);

        TimeEntry updateedTimeEntry = find(timeEntryId);
        return updateedTimeEntry;
    }

    @Override
    public void delete(Long timeEntryId) {
        jdbcTemplate.update("DELETE FROM time_entries where id=" + timeEntryId);

    }
}
