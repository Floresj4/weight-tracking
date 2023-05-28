# Batch

Batch populate a database with weight tracking entries.

<br/>

## Container setup

This project uses containers for storage and manipulation with MySQL.

<br/>

### Creating a custom network

A custom network is required to allow DNS resolution between containers in a network.  This ensures the container client can connect for setup.

`docker network create -d bridge mysql-bridge-network`

<br/>

### MySQL database container

The `hostname` option is used to explicitly define the container hostname otherwise it would inherit the container ID.

`docker run --name weight-database -p 3306:3306 --hostname weight-db-host --network=mysql-bridge-network -e MYSQL_ROOT_PASSWORD=1234 -d mysql`

<br/>

### Client Connect

The MySQL container can be used as a client to configure the database.

`docker run -it --network mysql-bridge-network --rm mysql mysql -hweight-db-host -u root -p`

<br/>

Create and select a database to use.

`create database weight_tracking;` <br/>
`use weight_tracking;`

<br/>

Table setup

```
create table weight_entries(
  entry_date date not null
  , entry_value float not null
  , unique key(entry_date)
);
```

<br/>

- [Weight Tracking](../readme.md)
- [API](../api/)
- [UI](../ui/)