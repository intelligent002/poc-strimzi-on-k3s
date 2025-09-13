## install the namespace as we will not delete/create it each time
```
kubectl apply -f namespace.yaml
```

## install the helm chart - version 0.46.1
```
helm install strimzi-cluster-operator --set replicas=1 --version 0.46.1 oci://quay.io/strimzi-helm/strimzi-kafka-operator --namespace strimzi
```

### or uninstall
```
helm uninstall strimzi-cluster-operator --namespace strimzi
```

## deploy kafka cluster of 9 nodes
* edit the yaml prior deploy
* credentials
* connect image is different from other env
```
kubectl apply -f cluster.strimzi-0.46.1.yaml -n strimzi
```

### or delete
```
kubectl delete -f cluster.strimzi-0.46.1.yaml -n strimzi
```

# check status of connect cluster
```
kubectl get kafkaconnect my-connect -n strimzi -o yaml
```

# request Connect API for list of installed connectors
```
kubectl exec -n strimzi my-connect-connect-0 -- curl -s http://localhost:8083/connectors
```

# check status of particular connector
```
kubectl get kafkaconnector mssql-connector -n strimzi -o yaml
kubectl get kafkaconnector mysql-connector -n strimzi -o yaml
```

# view topics involved
```
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

# consume some cdc data from topics
```
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cdc-mysql.iot.iot_temp --from-beginning --max-messages 5
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cdc-mssql.FHIR.dbo.Employees --from-beginning --max-messages 5
```

# exec into broker to perform extra stuff
```
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /bin/bash
```
