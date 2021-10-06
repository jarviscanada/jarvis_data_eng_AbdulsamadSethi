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

```
./scripts/psql_docker.sh create [username] [password]
```


* Start/Stop a PostgreSQL instance using `psql_docker.sh`

```
./scripts/psql_docker.sh start|stop
```

* Create database table that will contain hardware specification and resource usage using `ddl.sql`

```
psql -h localhost -U [username] -d [database] -f sql/ddl.sql
```

* Retrieve and insert hardware specifications into the database using `host_info.sh`

```
./scripts/host_info.sh localhost 5432 [database] [username] [password]
```

* Retrieve and insert resource usage into the database using `host_usage.sh`

```
./scripts/host_usage.sh localhost 5432 [database] [username] [password]
```

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

![Architecture Diagram](/assets/Diagram.png)

## Scripts
* `psql_docker.sh`
  * This script allows for 3 input arguments `create|start|stop [username] [password]`
  * Use `create` when you want to create a PostgreSQL instance provisioned by Docker. Whenever this command is triggered,
  the script will check if a container with the name (jrvs-psql) already exists on the server. If it doesn't already exist,
  it will create a new container with the designated name.
  * Use `start` after a PostgreSQL database is already created to start running the Docker container.
  * Use `stop` to halt the active/running Docker container that has the PostgreSQL database within it.
  * Use `username` and `password` arguments when using the `create` command. You can choose whatever username or password 
  for the PostgreSQL database. It is preferred to have `username=postgres` and `password=password`.
   ```
   # Creating a PSQL docker container with the given username and password
  ./scripts/psql_docker.sh create [username] [password]
  
  # Start PSQL docker container
  ./scripts/psql_docker.sh start
  
  # Stop PSQL docker container
  ./scripts/psql_docker.sh stop
   ```
  
* `host_info.sh`
  * This script is used to analyze the hardware specifications of the server and then store it in the `host_info.sh` table 
  in the database.
  ```
  # Get hardware specifications and insert them into the host_info table within the PSQL database
  ./scripts/host_info.sh localhost 5432 [database][username][password]
  ```

* `host_usage.sh`
  * This script is used to analyze the resource usage of the server and then store it in the PSQL database
  ```
  # Get resource usage information and insert them into the host_usage table within the PSQL database
  ./scripts/host_usage.sh localhost 5432 [database][username][password]
  ```
* `crontab`
  * Allows for the `host_usage.sh` script to analyze real time information about the servers resource
  usage and then update the host_usage table by executing that script every minute.
  ```
  # Open contrab editor to create a new job
  contrab -e

  # Add the line to the open editor to collect the usage data every minute
  [full file path to]/linux_sql/scripts/host_usage.sh localhost 5432 [database] [username] [password]

  # To check if the crontab implementation was successful use this command to ensure that the job is running
  crontab -l
  ```
* `queries.sql`
  * This script contains some useful queries that help some questions about the resource planning and host information: 
  1. Group hosts by number of CPUs and total memory size in descending order
  2. Get the average memory usage percentage in 5 minute intervals for each host
  3. Detect if a host has failed during a crontab job. 

    
## Database Modeling
The `host_info` table contains/stores all the hardware specifications of each node/server. The table schema is as 
follows:

| Field Name      |                          Description                                                            |
|:-----------:    |:--------------------------------------------------------:                                       |
|id               |Unique identifier for each host machine that is auto-incremented                                 |
|hostname         |Unique name for each host                                                                        |
|cpu_number       |Number of CPU cores                                                                              |
|cpu_architecture |Name of CPU architecture                                                                         |
|cpu_model        |Name of CPU model                                                                                |
|cpu_mhz          |Clock speed of the CPU in MHz                                                                    |
|L2_cache         |Size of Level 2 cache memory in kB                                                               |
|total_mem        |Size of total memory on host                                                                     |
|timestamp        |Time of when host_info specifications were recorded in `Year-Month-Day Hour:Minute:Second` format|

The `host_usage` table contains/stores all the resource usage information (which is updated every minute) 
The table schema is as follows:

| Field Name      |                          Description                                                            |
|:-----------:    |:--------------------------------------------------------:                                       |
|timestamp        |Time of when host_usage information was recorded in `Year-Month-Day Hour:Minute:Second` format|
|host_id          |Host identifier that corresponds to the one present in the `host_info` table                     |
|memory_free      |Amount of available memory space                                                                 |
|cpu_idle         |Percentage of time that CPU is in idle state                                                     |
|cpu_kernel       |Percentage of time CPU is running in kernel mode                                                 |
|disk_io          |Number of disks undergoing Input/Output processes                                                |
|disk_available   |Amount of available space in the disk's root directory in MB                                     |

# Test
The bash scripts were all tested manually through the linux shell. The following scripts that were tested are listed 
below: 
* `psql_docker.sh`
* `host_info.sh`
* `host_usage.sh`

To test the SQL queries and files, the following steps had to be taken:
1. First, the `ddl.sql` file had to be tested manually by executing it in the linux shell. 
    ```
   # Open the PSQL editor to check if the database is created
   psql -h localhost -U [username] -W
    ```
2. Then, to check if the table within the `ddl.sql` file are created successfully, you can use following command:
    ```
    # List all tables to confirm if tables have been created
   postgres = # \dt
    ```
3. Finally, to test the queries within `queries.sql`, synthetic data is inserted into a mock database and runs the 
queries. This allows the results produced to be compared to the expected results.
# Development
The application was deployed with the use of GitHub, Docker and Crontab.
* GitHub allowed for the implementation of this project to be available to clients with a very in-depth README.md for 
instructions.
* Docker was used to create a container that provisioned a PSQL instance that is able to run the project locally.
* Crontab was used to automate `host_usage.sh`, which allowed the resource usage of the CPU to be viewed in real-time.
* # Improvements
Some future improvements that could be implemented are:
* An alert system that goes off when SQL queries detects that the server has failed. This will allow for better maintenance 
of the server.
* Find a way to make the reports of the resource usage data more readable. This can be done in many ways, such as storing 
the data within a JSON file.