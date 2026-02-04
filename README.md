docker-compose down -v            остановить/удалить контейнеры (запустить)
docker-compose up -d --build      пересобрать контейнеры (остановить)


mvn clean package                 пересобрать проект


Ручное создание топиков:
docker exec -it kafka kafka-topics \
  --bootstrap-server kafka:9092 \
  --create \
  --if-not-exists \
  --topic mock-requests \
  --partitions 1 \
  --replication-factor 1
docker exec -it kafka kafka-topics \
  --bootstrap-server kafka:9092 \
  --create \
  --if-not-exists \
  --topic mock-responses \
  --partitions 1 \
  --replication-factor 1
  
  
Как читать сообщения из Kafka?
# 1. Зайдите в контейнер Kafka
docker exec -it kafka /bin/bash

# 2. Просмотр всех топиков
kafka-topics --list --bootstrap-server localhost:9092

# 3. Чтение всех сообщений из топика
kafka-console-consumer \
  --topic mock-requests \
  --from-beginning \
  --bootstrap-server localhost:9092







POST    http://localhost:8080/info/postBalances
GET     http://localhost:8081/actuator/health
GET     http://localhost:8081/actuator/prometheus
GET     http://localhost:8081/actuator
GET     http://localhost:8081/actuator/info

Пример запроса:
{
    "rqUID": "58dgtf565j8547f64ke7",
    "clientId": "3050000000000000000",
    "account": "30500000000000000001",
    "openDate": "2020-01-01",
    "closeDate": "2025-01-01"
}

Пример ответа:
{
    "rqUID": "58dgtf565j8547f64ke7",
    "clientId": "3050000000000000000",
    "account": "30500000000000000001",
    "currency": "RU",
    "balance": "16000.00",
    "maxLimit": "50000.00"
}

Если номер клиента начинается с 8 то валюта счета(currency) доллар - US, максимальный лимит(maxLimit) - 2000.00
Если номер клиента начинается с 9 то валюта счета(currency) евро - EU, максимальный лимит(maxLimit) - 1000.00
Если начинается с любой другой цифру то валюта счета(currency) доллар - RUB, максимальный лимит(maxLimit) - 10000.00