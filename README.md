## Notification Service
* This service is to send the notifications (email, SMS & Whats app) to the user when a user registered on the app.
* In future, this service going to handle initiation of notifications when order confirmation and cancellation .
* At the moment this service triggering the email notification only. In future scope SMS and what's app notification logic will be added.
* We are using *Zookeeper* and *Kafka* event queuing for establishing communication between User service (producer) and Notification service (consumer)
