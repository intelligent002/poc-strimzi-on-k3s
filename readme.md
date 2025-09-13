# Strimzi on K3S — PoC Environment

This repository contains a **proof-of-concept setup** for running [Apache Kafka](https://kafka.apache.org/) on [K3S](https://k3s.io/) using the [Strimzi Kafka Operator](https://strimzi.io/) and [Debezium](https://debezium.io/releases/3.2/) CDC Plugins (mysql+mssql).
It includes two parallel environments:

- **`dev/`** → based on **K3S v1.27.14** and **Strimzi v0.45.0**  
- **`prod/`** → based on **K3S v1.33.1** and **Strimzi v0.46.1**

Each environment folder contains its own manifests, installation notes, Dockerfile for connect image, and identical plugin bundles for [Debezium](https://debezium.io/releases/3.2/) connectors.

---

## Repository Structure

```
.
├── dev/    # Development environment (K8S 1.27.14, Strimzi 0.45.0)
│   ├── cluster.strimzi-0.45.0.yaml   # Strimzi 0.45.0 cluster configuration
│   ├── Dockerfile                    # Strimzi 0.45.0 + Kafka 3.9.0 + Debezium 3.2.2
│   ├── install k3s-1.27.14.md        # Installation steps for K3S 1.27.14
│   ├── install strimzi-0.45.0.md     # Installation steps for Strimzi 0.45.0
│   ├── namespace.yaml                # Kubernetes namespace definition
│   └── plugins/                      # Debezium connectors (MySQL, SQL Server)
└── prod/   # Production-like environment (K8S 1.33.1, Strimzi 0.46.1)
    ├── cluster.strimzi-0.46.1.yaml   # Strimzi 0.46.1 cluster configuration
    ├── Dockerfile                    # Strimzi 0.46.1 + Kafka 3.9.0 + Debezium 3.2.2
    ├── install k3s-1.33.1.md         # Installation steps for K3S 1.33.1
    ├── install strimzi-0.46.1.md     # Installation steps for Strimzi 0.46.1
    ├── namespace.yaml                # Kubernetes namespace definition
    └── plugins/                      # Debezium connectors (MySQL, SQL Server)
```

---

## Documentation

### Development environment (`dev/`)
- [Install K3S 1.27.14](dev/install%20k3s-1.27.14.md)  
- [Install Strimzi 0.45.0](dev/install%20strimzi-0.45.0.md)  

### Production-like environment (`prod/`)
- [Install K3S 1.33.1](prod/install%20k3s-1.33.1.md)  
- [Install Strimzi 0.46.1](prod/install%20strimzi-0.46.1.md)  

---

## Kafka Connect Plugins

Both environments ship with Debezium **MySQL** and **SQL Server** connectors (v3.2.2), bundled into a custom Kafka Connect image via `Dockerfile`.

- **MySQL connector** → `plugins/debezium-connector-mysql`  
- **SQL Server connector** → `plugins/debezium-connector-sqlserver`

---

## Goals of this PoC

- Validate **Strimzi Kafka Operator** compatibility across different **K3S** and **Strimzi** versions.  
- Provide clear, working configuration manifests for the specified stack:
  - **K8S v1.27.14 / Strimzi v0.45.0 / Kafka 3.9.0 / Debezium 3.2.2**
  - **K8S v1.33.1  / Strimzi v0.46.1 / Kafka 3.9.0 / Debezium 3.2.2**
- Test **Debezium CDC connectors** (MySQL & SQL Server) on top of Kafka Connect.  
- Explore different ways to integrate **Kafka Connect with Microsoft SQL Server** when default SSL settings cause connection errors.
- Separate **DEV** and **PROD** environments for easier migration and troubleshooting.

---

## Resources

- [Apache Kafka](https://kafka.apache.org/documentation/)  
- [Strimzi Documentation](https://strimzi.io/documentation/)  
- [Debezium Documentation](https://debezium.io/documentation/)  
- [K3s Documentation](https://docs.k3s.io/)  

---

## Next Steps

1. Choose the target environment (`dev/` or `prod/`).  
2. Follow the K3S + Strimzi installation guides.  
3. Build and deploy the Kafka Connect image with bundled plugins.  
4. Apply the Strimzi cluster manifests.  
5. Register connectors (MySQL/MSSQL) and verify CDC streams.
