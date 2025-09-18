# MSSQL JDBC Connection Tester

This simple Java tool (`Tester.java`) allows you to quickly test JDBC connectivity to a Microsoft SQL Server instance from the command line.

It is useful for debugging:
- Network reachability
- Login credentials
- SSL/TLS encryption settings
- Certificate trust issues (PKIX errors)

---

## ⚙️ Requirements

- Java 11+ installed (`javac` and `java` available in PATH)
- The Microsoft JDBC Driver for SQL Server (`mssql-jdbc.jar`)

Use the `mssql-jdbc.jar` driver provided in this repo, or download a newer one from:
[Microsoft JDBC Driver for SQL Server](https://learn.microsoft.com/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server)

---

## 🛠️ Build

Place `Tester.java` and `mssql-jdbc.jar` in the same folder, then compile:

```bash
    javac -cp mssql-jdbc.jar Tester.java
```

This creates `Tester.class`.

---

## ▶️ Usage

Run the tool with the required parameters:

```bash
    java -cp .:mssql-jdbc.jar Tester \
        -user <db_user> \
        -pass <db_pass> \
        -host <db_host> \
        -base <database_name> \
        [-encrypt true|false] \
        [-trustServerCertificate true|false]
```

**Notes:**
- Use `;` instead of `:` on Windows for the classpath separator.
- `encrypt` defaults to `true` if not specified.
- `trustServerCertificate` defaults to `true` if not specified.

---

## 📋 Example

```bash
    java -cp .:mssql-jdbc.jar Tester \
        -user "debezium_user" \
        -pass "StrongPassword123!" \
        -host "kafka-mssql.intel.r7g.org" \
        -base "FHIR" \
        -encrypt "true" \
        -trustServerCertificate "false"
```

Output:

```
---------------------------------------------------------------------------------------------------------------------------------------------------------------
🔌 Connecting to: jdbc:sqlserver://kafka-mssql.intel.r7g.org:1433;databaseName=FHIR;encrypt=true;trustServerCertificate=false
---------------------------------------------------------------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------------------------------------------------------------
❌ Connection failed (SQLException):
---------------------------------------------------------------------------------------------------------------------------------------------------------------
Message: "encrypt" property is set to "true" and ...
SQLState: 08S01
ErrorCode: 0
...
---------------------------------------------------------------------------------------------------------------------------------------------------------------
```

---

## 🔍 Enabling SSL Handshake Debug

To see full TLS negotiation, cipher suites, and certificates from the server:

```bash
    java -Djavax.net.debug=ssl,handshake \
        -cp .:mssql-jdbc.jar Tester \
        -user "debezium_user" \
        -pass "StrongPassword123!" \
        -host "kafka-mssql.intel.r7g.org" \
        -base "FHIR" \
        -encrypt "true" \
        -trustServerCertificate "false"
```

This prints:
- ClientHello and ServerHello
- TLS version and cipher chosen
- The full server certificate chain
- Any PKIX / validation errors

---

## ⚡ Troubleshooting

- **PKIX path building failed**
  This means the server's certificate is not trusted.
  Either set `-trustServerCertificate true` to skip validation or import the certificate into your Java truststore.


- **Login failed for user**
  Check username/password and SQL authentication mode.


- **Connection timed out**
  Check firewall rules and that TCP 1433 is open and SQL Server is listening.

---

## 📌 License

MIT — use freely for debugging and testing.
