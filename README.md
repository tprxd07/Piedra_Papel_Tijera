# Cuarzo Pergamino Tenazas
## Equipo SinNombre

## Miembros:
- Curro
- Antonio
- Tomás
- Ángel
- Rafa

# Temática

- Juego de piedra, papel y tijeras con temática de street fighter.
- Se puede jugar contra la IA y 2 jugadores de forma local.
- Se guardarán los datos del nombre del jugador, victorias, racha de victorias y partidas jugadas.
- Se puede acceder a una leaderboard para ver los mejores 5 en victorias y partidas jugadas.

# Tecnologias
IDE: IntelliJ
Base de datos: MySQL
Interfaz: JavaFX

# Configuración base de datos

Es necesaria una base de datos de MariaDB o MySQL, el programa contiene un "connection_dist.xml" como plantilla se debe cambiar el nombre a "connection.xml"
En server ponemos la dirección del servidor o localhost si está en local, luego
hay que poner el puerto, el nombre de la base de datos debe ser "piedra_papel_tijeras", el usuario y la contraseña

```MySQL
DROP database if exists piedra_papel_tijeras;
CREATE database if not exists piedra_papel_tijeras;
use piedra_papel_tijeras;
CREATE TABLE Jugador (
	idJugador int unsigned primary key auto_increment,
    nombre varchar(30) not null,
    victorias int unsigned default 0 ,
    partidasJugadas int unsigned default 0 ,
    rachasDeVictoria int unsigned  default 0 )
;
```

Ejemplo de connection.xml

```XML
<?xml version="1.0" encoding="UTF-8"?>
<connection>
    <server>localhost</server>
    <port>3307</port>
    <dataBase>piedra_papel_tijeras</dataBase>
    <user>root</user>
    <password>root</password>
</connection>
```
