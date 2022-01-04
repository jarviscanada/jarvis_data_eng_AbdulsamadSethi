Table of contents
* [Introduction](#Introduction)
* [Hadoop Cluster](#Hadoop Cluster)
* [Hive Project](#Hive Project)
* [Improvements](#Improvements)

# Introduction
The purpose of this project was to perform data analytics and process big data using Apache Hadoop and other tools such
as: HDFS, YARN, Zeppelin, Hive and Spark. The project required a three node Hadoop cluster to be provisioned using GCP 
Dataproc service. It then required a Dataset (which was called wdi_2016) which was perpared from the source: Google Public 
Data (hosted on Google BigQuery). The Dataset was exported via BigQuery Table to Google Storage, which then allowed for the
dataset to be used for creating Tables and Querying using Hive. Zeppelin Notebook was also used to execute the code. The last steps
taken for this project was to integrate GS data to HDFS as text and also continuously improve parsing and performance through the
use of OpenCSVSerDe, Partitioning and Columnar(parquet).


# Hadoop Cluster
## Cluster architecture diagram

## Master Worker/Master-Slave/Map-Reduce Pattern
Used for parallel processing and allows applications to perform simultaneous processing across multiple machines.

### Master Node Hardware Specs

### Worker Node Hardware Specs
    
### MapReduce

### Hadoop Distributed File System - (HDFS)

### Yet Another Resource Negotiator - (YARN)

### Zeppelin

### Hive and Hive-QL


# Hive Project

## Partitioning Optimization
Partitioning is used to enable the ability to query a set portion of the data. It essentially allows for a way to divide
a table into related parts based on the values of the partitioned columns. In this particular project, it was divided in 
the columns: CountryName, IndicatorCode, and IndicatorValue.

## Columnar Optimization
Columnar database stores data in columns instead of rows as it allows for better analytical query processing. 

## Full Page Captures
![Notebook_Fullpage](assets/Notebook_fullpage.png "Notebook Fullpage")
![Notebook_output](assets/Notebook_output_fullpage.png "Notebook Fullpage Output")

# Improvements
1. Add more work nodes to improve the performance
2. Use bucketing and compare bucketing and partition
3. Use sqark-sql instead of hive