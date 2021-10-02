#! /bin/bash

state=$1
db_user=$2
db_pass=$3
container="jrvs-psql"
container_status=$(docker container ls -a -f name=$container | wc -1)

#Start docker
#Make sure you understand `||` cmd
sudo systemctl status docker || systemctl start docker

#check container status (try the following cmds on terminal)
docker container inspect jrvs-psql
container_status=$?

#User switch case to handle create|stop|start options
case $state in
  create)

  # Check if the container is already created
  if [ $container_status -eq 2 ];
  then
		echo "The Container jrvs-psql has already been created on this machine"
		exit 1
	elif [ "$#" -lt 3 ] #Check if user input the username and password of container they want to create
	then
	  echo "Invalid syntax, please use this script with the following correct syntax: "
	  echo "./psql_docker.sh create|start|stop [db_username][db_password]"
	  exit 1
	else #If user passes all checks then create the postgres container with arguments given
	  docker pull postgres
	  docker volume create pgdata
	  docker run --name $container -e POSTGRES_PASSWORD=${db_pass} -e POSTGRES_USER=${db_user} -d -v pgdata:/var/lid/postgressql/data -p 5432:5432 postgres
	  exit $?
	fi
	;;

  start)

  # Check if container exists then start docker container
    if [ $container_status -ne 2 ];
    then
      echo "The container jrvs-psql has not yet been created on this machine, to create one please use the following syntax"
      echo "./psql_docker.sh create|start|stop [db_username][db_password]"
      exit 1
    else
      docker container start $container
      exit $?``
    fi
    ;;

  stop)

    # Check if container exists then stop docker container
    if [ $container_status -ne 2 ];
    then
      echo "The container jrvs-psql has not yet been created on this machine, to create one please use the following syntax"
      echo "./psql_docker.sh create|start|stop [db_username][db_password]"
      exit 1
    else
      docker container stop $container
      exit $?
    fi
    ;;

  *)

    # If user input does not match any of these statements output error and correct syntax
    echo "Invalid syntax, please use this script with the following correct syntax: "
    echo "./psql_docker.sh create|start|stop [db_username][db_password]"
    exit 1
    ;;
esac

