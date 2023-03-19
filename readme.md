
### Creating a custom network

`docker network create -d bridge mysql-bridge-network`

A custom network is required to allow DNS resolution between containers in a network.  This ensures the container client can connect for setup.

### Launching MySQL database container

`docker run --name weight-database --network=mysql-bridge-network -e MYSQL_ROOT_PASSWORD=1234 -d mysql:8.0.32`

### Client connect for setup

`docker run -it --network bridge rm mysql mysql -hweight-database -u root -p`