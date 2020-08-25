# ########################################################################################
#                     `bug_example` Postgres DB Docker Image Creator Script              #
# ########################################################################################
#
# This script creates a docker image which will have the schema applied when a container is launched.

# Begin from the correct postgres version.
FROM postgres:12.3-alpine

# Will instruct the container to create a 'bug_example' database when first run.
ENV POSTGRES_DB bug_example

# ##################################
#     Initialise the schema
# ##################################
# SQL (and sh) files in the /docker-entrypoint-initdb.d/ directory will be executed the first
# time the container is run. We want to create a single file containing all SQL that we want run
# (rather than execute multiple files). So we copy all SQL files into a temp directory with the intent
# to merge them into a single file.
COPY sql/migration /docker-entrypoint-initdb.d-temp/

# The execution order of the SQL files is defined as the sorted name order for the
# current locale (defaults=en_US.utf8). This is problematic because it will mean our
# scripts will be sorted wrong (10,11,12,...1,21,22,..2). So to avoid this we
# concatenate all of SQL files contained in the temp directory in the corect order
# and output it as one file in the initdb.d directory.
RUN \
    # Change directory to the temp directory we have created which contains all of our SQL scripts.
    cd /docker-entrypoint-initdb.d-temp && \
    # Concatenate each SQL file and output the file in the actual (not temp) init directory in a
    # script called init.sql.
    # ls options: -1: list each item on its own line, -v: natural sort of numbers (rather than the unix default),
    #             $PWD/* (prefix the filename with the directory (so we have an absolute reference)
    cat `ls -1v $PWD/* | grep '.sql'` > /docker-entrypoint-initdb.d/init.sql