-- Script for creating the Axon required tables and any associated indexes.
-- Resources used to create this script:
-- - https://gist.github.com/zambrovski/18b8c154a20454d78e9faa453025d162 (outdated but still useful)
-- - https://github.com/JohT/showcase-quarkus-eventsourcing/blob/master/showcase-quarkus-eventsourcing/src/main/resources/db/command/common/V1.0.0__AxonOnMicroprofileTryoutEventsourcing.sql
-- - https://groups.google.com/forum/#!topic/axonframework/jIapWg_cW6w
-- - https://hibernate.atlassian.net/browse/HHH-13106 - open issue which will hopefully fix sequence


-- Need because we are using JPA. Should be changed if change to not use JPA.
CREATE SEQUENCE IF NOT EXISTS public.hibernate_sequence start with 1 increment by 1;

CREATE TABLE IF NOT EXISTS public.association_value_entry
(
    -- Will generate a sequence (since it is bigserial) that is expected to exist.
    id                    bigserial NOT NULL,
    saga_id               text NOT NULL,
    association_key       text NOT NULL,
    association_value     text,
    saga_type             text,
    CONSTRAINT association_value_entry_pkey PRIMARY KEY (id)
);

-- Create indexes for the association_value_entry table.
CREATE INDEX ASSOCIATION_VALUE_ENTRY_INDEX_ASSOCIATION ON public.association_value_entry (saga_type, association_key, association_value);
CREATE INDEX ASSOCIATION_VALUE_ENTRY_INDEX_SAGA ON public.association_value_entry (saga_id, saga_type);


CREATE TABLE IF NOT EXISTS public.saga_entry
(
    saga_id               text NOT NULL,
    revision              text,
    saga_type             text,
    serialized_saga       oid,
    CONSTRAINT saga_entry_pkey PRIMARY KEY (saga_id)
);


CREATE TABLE IF NOT EXISTS public.token_entry
(
    processor_name        text NOT NULL,
    segment               integer NOT NULL,
    owner                 text,
    "timestamp"           text NOT NULL,
    token                 oid,
    token_type            text,
    CONSTRAINT token_entry_pkey PRIMARY KEY (processor_name, segment)
);
