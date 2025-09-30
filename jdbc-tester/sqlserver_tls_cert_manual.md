# Replacing SQL Server's SHA‑1 Fallback Certificate with a SHA‑256 Certificate (Windows)

## Why we see a SHA‑1 certificate on SQL Server

When you install SQL Server and don’t configure a TLS certificate, the server **auto‑generates a self‑signed fallback certificate** every time it starts.

- Appears as `CN=SSL_Self_Signed_Fallback` or similar
- Uses **SHA‑1** and is **self‑signed**
- Invisible in normal cert stores (ephemeral)
- Purpose: allow encrypted connections by default
- Limitation: modern Java (Debezium) rejects it with `Certificates do not conform to algorithm constraints`

**Production use:** Always install your own modern certificate (SHA‑256+) with the proper key usage and access rights.

---

## Goal

* Create a **SHA‑256 server authentication certificate** with key exchange capability,
* Install it to the correct store,
* Grant the SQL Server service account access,
* Bind it in SQL Server Configuration Manager (SSCM),
* Restart both SSCM and the SQL Server service,
* optionally export the public part for Java/Debezium (Strimzi) clients.

---

## Step‑by‑step

### 1. Identify the SQL Server service account

1. Open **SQL Server Configuration Manager** → **SQL Server Services**.
2. Note the **Log On As** column for your instance: e.g.
   - Default: `NT SERVICE\MSSQLSERVER`
   - Named: `NT SERVICE\MSSQL$DEBEZIUM`

> SQL Server must read the private key under this account.

---

### 2. Create a proper SHA‑256 self‑signed certificate

Run **PowerShell as Administrator**:

```powershell
New-SelfSignedCertificate `
  -DnsName "kafka-sql-2016.intel.r7g.org" `
  -CertStoreLocation "Cert:\LocalMachine\My" `
  -KeySpec KeyExchange `
  -FriendlyName "MSSQL Debezium" `
  -NotAfter (Get-Date).AddYears(10) `
  -HashAlgorithm "SHA256" `
  -TextExtension @("2.5.29.37={text}1.3.6.1.5.5.7.3.1")
```

**Why this way:**

- **DnsName** → CN + SAN for hostname validation
- **LocalMachine\My** → SQL Server scans only this store
- **KeySpec KeyExchange** → required for TLS key use
- **ServerAuth EKU** → SQL Server only lists certs with this usage
- **SHA256** → modern Java accepts; SHA1 is blocked

### Verification of the certificate availability

- **List certs:**  
  ```powershell
  Get-ChildItem Cert:\LocalMachine\My | Select Subject,Thumbprint,HasPrivateKey,EnhancedKeyUsageList
  ```
- Expect `HasPrivateKey=True` and EKU includes **Server Authentication**.

---

### 3. Grant the SQL Server service account access to the private key

1. Run `certlm.msc` (Certificates – Local Computer).
2. Go to **Personal → Certificates**.
3. Right‑click the new cert → **All Tasks → Manage Private Keys…**.
4. Add the service account from step 1 (e.g. `NT SERVICE\MSSQL$DEBEZIUM`).
5. Grant **Read** permission.

---

### 4. Restart SQL Server Configuration Manager (SSCM)

> SSCM only scans for certificates when it starts.  
> Close and reopen SSCM now to refresh the list.

---

### 5. Bind the certificate to the SQL Server instance

1. In **SSCM** → **SQL Server Network Configuration → Protocols for <Instance>**.
2. Right‑click **Protocols** → **Properties**.
3. **Certificate** tab → select your new cert.
4. **Flags** tab → optional **Force Encryption = No**.

---

### 6. Restart the SQL Server service

SQL Server loads the TLS cert only at startup.

- In SSCM: right‑click the instance → **Restart**
- Or CLI:

```cmd
net stop MSSQL$DEBEZIUM
net start MSSQL$DEBEZIUM
```

---

### 7. Export the public cert for Java / Debezium trust (optional)

```powershell
   Export-Certificate -Cert "Cert:\LocalMachine\My\<Thumbprint>" -FilePath C:\temp\mssql.cer
```

On Linux (Kafka Connect):

```bash
   keytool -importcert -file mssql.cer -alias mssql -keystore mssql-truststore.jks -storepass changeit
```

Mount `mssql-truststore.jks` into your Strimzi KafkaConnect pods and set in the Debezium connector:

> Use secrets/mounts in K8S 
> ```yaml
> database.encrypt: true
> database.trustServerCertificate: false
> database.ssl.truststore.location: /opt/kafka/connect/certs/mssql-truststore.jks
> database.ssl.truststore.password: changeit
> ```

> Quick and insecure: just use without mounting
> ```yaml
> database.encrypt: true
> database.trustServerCertificate: true
> ```

> Or even better: don't use, stay on plaintext
> ```yaml
> database.encrypt: false
> database.trustServerCertificate: false
> ```

---

With this, SQL Server presents a modern TLS certificate that works with Java/Debezium and avoids SHA‑1/algorithm constraint errors.
Other products using the MSSQL with plaintext should not be affected, best to verify that.
