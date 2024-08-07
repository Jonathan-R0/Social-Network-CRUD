# Trabajo Práctico 1

## Iniciar el Servicio

Primero se debe definir una red compartida entre los contenedores de Docker. Se crea con el siguiente comando:

```bash
docker network create tdd-network
```

Se levanta con el siguiente comando:

```bash
docker compose up
```

## Front End Mock

También disponemos de un script en Python que simula el Front End. Se puede correr con el siguiente comando:

```bash
python3 frontend_mock.py
```

Requiere tener instalado el paquete `Flask`.
