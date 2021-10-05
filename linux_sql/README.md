# Linux Cluster Monitoring Agent
# Introduction
The Linux Cluster Monitoring Agent is a tool that is used to monitor
remote linux servers. Each server is connected through a network switch
communicating through IPv4 addresses. It first initializes a PostgreSQL
database with a docker container by executing a Bash script. It is then used
to gather hardware specifications as well as monitor usage in real time with 
monitor agent scripts. All this information is stored in the docker container
that was created. The technologies used to create this tool are as follows:

* Bash
* GitFlow
* Docker
* Linux
* PostgreSQL
# Quick start

* Create a PostgreSQL instance using `psql_docker.sh`

`./scripts/psql_docker.sh create [username] [password]`


* Start/Stop a PostgreSQL instance using `psql_docker.sh`

`./scripts/psql_docker.sh start|stop`

* Create database table that will contain hardware specification and resource usage using `ddl.sql`

`psql -h localhost -U [username] -d [database -f sql/ddl.sql`

* Retrieve and insert hardware specifications into the database using `host_info.sh`

`./scripts/host_info.sh localhost 5432 [database] [username] [password]`

* Retrieve and insert resource usage into the database using `host_usage.sh`

`./scripts/host_usage.sh localhost 5432 [database] [username] [password]`

* Implementing `host_usage.sh` automation with `crontab`

```
# Open crontab editor to create a new job
crontab -e

# Add the line to the open editor to collect the usage data every minute
***** [full file path to]/linux_sql/scripts/host_usage.sh local 5432 [databae] [username] [password]
```

# Implementation
* To run this project, you must first provision a PostgreSQL database using docker. This is all
completed within the `psql_docker.sh` script. This script asks for 3 input arguments when creating a 
database, the first one will be either `create`, `start`, or `stop` (in this case use `create`) a 
provision of a PostgreSQL database, and the following input arguments are the `username` and `password`
of PostgreSQL database on this server.
* After the creation of the database, you must start the docker container, by running the `psql_docker.sh` again
, but this time using the `start` input argument.
* Once the docker container of the PSQL database is active and running, you can now proceed with creating and 
implementing the tables for the database by running the `ddl.sql` script.
* After the tables are successfully created, the database is now ready to be populated with the hardware 
specifications and resource usage of the server by executing both `host_info.sh` and `host_usage.sh` scripts
* The `host_info.sh` script is used to collect the hardware specifications using the `lscpu` command and then inserting
the values into the PSQL database tables. 
* The `host_usage.sh` script collects the real time usage of the server using the `vmstat` command and then inserting
the data into the PSQL database tables.
  * `host_usage.sh` can be automated using `contrab`. This will allow the user to see the CPU usages in real time, which
  will ultimately allow for better maintenance or monitoring of any server.

## Architecture

## Scripts

## Database Modeling

# Test

# Development

# Improvements
