# install the namespace as we will not delete/create it each time
kubectl apply -f namespace.yaml

# install the helm chart
helm install strimzi-cluster-operator --set replicas=1 --version 0.45.0 oci://quay.io/strimzi-helm/strimzi-kafka-operator --namespace strimzi

# or uninstall
helm uninstall strimzi-cluster-operator --namespace strimzi

# deploy kafka cluster of 9 nodes
kubectl apply -f cluster.strimzi-0.45.0.yaml -n strimzi

# or delete
kubectl delete -f cluster.strimzi-0.45.0.yaml -n strimzi

# request Connect API for list of installed connectors
kubectl exec -n strimzi my-connect-connect-0 --   curl -s http://localhost:8083/connectors

# check status of particular connector
kubectl get kafkaconnector mssql-connector -n strimzi -o yaml
kubectl get kafkaconnector mysql-connector -n strimzi -o yaml

# view topics involved
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

# consume some cdc data from topics
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cdc-mysql.iot.iot_temp --from-beginning --max-messages 5
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cdc-mssql.FHIR.dbo.Employees --from-beginning --max-messages 5

# exec into broker to perform extra stuff
kubectl exec -it my-cluster-brokers-0 -n strimzi -- /bin/bash



