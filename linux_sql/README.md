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

*Create database table that will contain hardware specification and resource usage using `ddl.sql`

`psql -h localhost -U [username] -d [database -f sql/ddl.sql`

*Retrieve and insert hardware specifications into the database using `host_info.sh`

`./scripts/host_info.sh localhost 5432 [database] [username] [password]`

*Retrieve and insert resource usage into the database using `host_usage.sh`

`./scripts/host_usage.sh localhost 5432 [database] [username] [password]`

*Implementing `host_usage.sh` automation with `crontab`

`# Open crontab editor to create a new job`
`crontab -e`

`Add the line to the open editor to collect the usage data every minute`
`***** [full file path to]/linux_sql/scripts/host_usage.sh local 5432 [databae] [username] [password]`

# Implementation

## Architecture

## Scripts

## Database Modeling

# Test

# Development

# Improvements
