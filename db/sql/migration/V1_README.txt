The first SQL script we have begins with V2 instead of V1 (which is
left blank). This is because the first entry in the flyway_schema_history
will be overwritten with the flyway "baseline" row (if we enable that).

To avoid hiding our V1 script in the table (by having it unintentionally overwritten) we
just sidestep the issue and start at V2.