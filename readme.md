
### Creating a custom network

`docker network create -d bridge mysql-bridge-network`

A custom network is required to allow DNS resolution between containers in a network.  This ensures the container client can connect for setup.

### Launching MySQL database container

`docker run --name weight-database -p 3306:3306 --hostname weight-db-host --network=mysql-bridge-network -e MYSQL_ROOT_PASSWORD=1234 -d mysql`

The `hostname` option is used to explicitly define the container hostname otherwise it would inherit the container ID.

### Client connect for setup

`docker run -it --network mysql-bridge-network --rm mysql mysql -hweight-db-host -u root -p`

`create database weight_tracking` <br/>
`use weight_tracking`

Create and select a database to use.

`create table weight_entries( entry_date date not null, entry_value float not null, unique key(entry_date));`

Setup table.