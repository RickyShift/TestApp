
<h1 align="center">GPI Scrum </h1>

### Install Tools

#### Windows 

```
instalar : Java, Maven, Postgresql (Criar base de dados no pgAdmin).
```

#### Linux

```
sudo apt update && sudo apt upgrade
sudo apt install openjdk-21-jdk postgresql
sudo apt install maven -y
```

### Start db, change to postgres user and create DB (Linux)
```
sudo service postgresql start
sudo su -l postgres
dropdb gpi
createdb gpi
```
### Create user to access db (Linux)
```
psql gpi
CREATE USER your-username WITH SUPERUSER LOGIN PASSWORD 'yourpassword';
\q
exit
```
### Go to `backend/src/main/resources/application-dev.properties` and fill its fields 




### Run server 
```
cd backend
mvn clean install 
mvn clean spring-boot:run
```