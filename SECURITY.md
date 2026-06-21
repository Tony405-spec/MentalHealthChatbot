# Security Policy

**Mindful Support: AI Mental Health Chatbot**
**Version:** 1.0.0
**Last Updated:** 2026-06-21

---

## 1. Scope

This security policy applies to the Mindful Support Android application, its associated infrastructure, and all components within the `skynet-datagrid-labs/MindfulSupport` repository.

| Component | Scope |
|:---|:---|
| **Android Application** | All Java source code, resources, and configuration files |
| **APK Artifacts** | Release and debug builds distributed via GitHub Releases |
| **M-Pesa Integration** | Payment gateway credentials and transaction processing |
| **User Data** | Locally stored chat history, preferences, and session data |
| **CI/CD Pipeline** | GitHub Actions workflows and build automation |

---

## 2. Supported Versions

| Version | Supported | Security Updates |
|:---|:---|:---|
| **1.0.x** | ✅ Yes | Active security patches |
| **0.9.x (Beta)** | ❌ No | End of life - upgrade required |
| **0.8.x (Alpha)** | ❌ No | End of life - upgrade required |

---

## 3. Reporting a Vulnerability

### Confidential Reporting Channel

**Email:** [tonykenga23@gmail.com](mailto:tonykenga23@gmail.com)

**PGP Key:** Available upon request via email.

### Reporting Process

| Step | Action | Expected Timeline |
|:---|:---|:---|
| **1** | Submit vulnerability report via encrypted email | Immediate |
| **2** | Acknowledgment of receipt | 24 hours |
| **3** | Initial triage and severity assessment | 48 hours |
| **4** | Status update | 5 business days |
| **5** | Fix development and testing | 7-14 business days (depending on severity) |
| **6** | Coordinated disclosure | 30 days from initial report |

### What to Include

To facilitate rapid assessment, please include:

- **Description:** Clear, detailed description of the vulnerability
- **Reproduction Steps:** Step-by-step instructions to reproduce
- **Impact Assessment:** What systems or data are affected
- **Proof of Concept:** Code snippets, logs, or screenshots (redacted)
- **Proposed Fix:** If available, suggested remediation approach

### Responsible Disclosure

We follow responsible disclosure practices. We request that:

- Vulnerabilities are **not publicly disclosed** before a fix is available
- Reports are submitted through the **confidential channel**
- No **exploitative testing** is performed on production systems
- We are given a **reasonable time** to address the issue

---

## 4. Vulnerability Categories

### Critical Severity (CVSS 9.0 - 10.0)

| Category | Description |
|:---|:---|
| **Credential Exposure** | Unauthorized access to M-Pesa `CUSTOMER_KEY`, `CUSTOMER_SECRET`, or `PASSKEY` |
| **Data Breach** | Unauthorized access to user chat history or PII |
| **Remote Code Execution** | Ability to execute arbitrary code on user devices |
| **Authentication Bypass** | Circumvention of login or session management |

### High Severity (CVSS 7.0 - 8.9)

| Category | Description |
|:---|:---|
| **Insecure Data Storage** | Exposed SharedPreferences with sensitive user data |
| **Man-in-the-Middle** | Insecure network communication without TLS |
| **API Key Leakage** | Hardcoded API keys in decompiled APK |
| **Security Misconfiguration** | Permissive Android manifest settings |

### Medium Severity (CVSS 4.0 - 6.9)

| Category | Description |
|:---|:---|
| **Information Disclosure** | Exposure of non-sensitive application metadata |
| **Denial of Service** | Application crash or unresponsiveness |
| **Improper Error Handling** | Error messages revealing internal logic |

### Low Severity (CVSS 0.1 - 3.9)

| Category | Description |
|:---|:---|
| **UI Spoofing** | Minor UI inconsistencies that could mislead users |
| **Deprecated Libraries** | Outdated dependencies with no known CVEs |

---

## 5. Security Best Practices

### Code Security

| Practice | Implementation |
|:---|:---|
| **Input Validation** | Validate all user inputs before intent classification |
| **Output Encoding** | Encode all outputs to prevent injection attacks |
| **Secure Random** | Use `SecureRandom` for cryptographic operations |
| **Proguard/R8** | Enable code obfuscation for release builds |
| **Hardcoded Secrets** | Remove all credentials from source code before release |

### Dependency Management

| Practice | Implementation |
|:---|:---|
| **Vulnerability Scanning** | Regular dependency scanning using GitHub Dependabot |
| **Library Pinning** | Specify exact library versions in `build.gradle` |
| **Frequent Updates** | Apply security patches within 30 days of release |

### Network Security

| Practice | Implementation |
|:---|:---|
| **TLS 1.2+** | Enforce TLS 1.2 or higher for all network connections |
| **Certificate Pinning** | Validate server certificates for M-Pesa API calls |
| **Network Security Config** | Use Android Network Security Configuration for HTTPS |

### Secure Storage

| Practice | Implementation |
|:---|:---|
| **Android Keystore** | Use for sensitive cryptographic key storage |
| **EncryptedSharedPreferences** | Encrypt SharedPreferences for sensitive data |
| **Internal Storage** | Store sensitive data in internal storage (not external) |

---

## 6. Known Vulnerabilities

We maintain transparency about known vulnerabilities. The following are actively being addressed or are mitigated by design.

| Issue ID | Description | Status | Mitigation |
|:---|:---|:---|:---|
| **MS-001** | M-Pesa credentials in source code | 🔄 **In Progress** | Move to secure backend service |
| **MS-002** | SharedPreferences storing chat history in plaintext | 🔄 **In Progress** | Migrate to EncryptedSharedPreferences |
| **MS-003** | Missing certificate pinning for network calls | 📋 **Planned** | Implement pinning for M-Pesa endpoints |
| **MS-004** | No runtime permission enforcement for critical features | ✅ **Resolved** | Android 13 permissions implemented |

---

## 7. Security Testing

### Automated Testing

| Test Type | Tool | Frequency |
|:---|:---|:---|
| **SAST** | SonarQube | On every push |
| **Dependency Check** | OWASP Dependency Check | Weekly |
| **DAST** | OWASP ZAP | Monthly |

### Manual Testing

| Test Type | Frequency | Description |
|:---|:---|:---|
| **Penetration Testing** | Quarterly | Comprehensive security assessment |
| **Code Review** | Per PR | Security-focused code review for all changes |
| **Threat Modeling** | Annually | Update threat model based on new features |

---

## 8. Incident Response

### Incident Severity Levels

| Level | Description | Response Time |
|:---|:---|:---|
| **Critical** | Active breach, data exfiltration, system compromise | Immediate |
| **High** | Confirmed vulnerability with limited exploitation | 2 hours |
| **Medium** | Potential vulnerability requiring investigation | 8 hours |
| **Low** | Non-exploitable vulnerability or configuration issue | 24 hours |

### Response Procedure

| Phase | Action | Owner |
|:---|:---|:---|
| **Detection** | Monitor security alerts and reports | Security Team |
| **Containment** | Isolate affected systems and prevent further damage | Engineering Lead |
| **Eradication** | Remove the threat and apply fixes | Development Team |
| **Recovery** | Restore systems to normal operation | Infrastructure Team |
| **Lessons Learned** | Document findings and update processes | Project Lead |

### User Notification

In the event of a confirmed breach affecting user data:

- **Notification Method:** Email (if email is collected) or in-app notification
- **Timeline:** Within 72 hours of breach confirmation
- **Required Information:** Nature of breach, data affected, remediation steps

---

## 9. Responsible Disclosure Hall of Fame

We recognize security researchers who have responsibly disclosed vulnerabilities to us.

| Researcher | Date | Finding | Acknowledgement |
|:---|:---|:---|:---|
| *List reserved for future disclosures* | - | - | - |

---

## 10. Compliance

### Regulatory Standards

| Standard | Applicability | Implementation |
|:---|:---|:---|
| **GDPR** | Data privacy for EU users | No PII collected; local storage only |
| **Kenya Data Protection Act** | Local compliance | Consent mechanism in onboarding |
| **Android Security Best Practices** | Google Play requirements | Implemented per Android guidelines |

### Privacy Policy

For detailed information on data handling, please refer to our [Privacy Policy](PRIVACY.md) (coming soon).

---

## 11. Contact Information

| Role | Contact |
|:---|:---|
| **Security Lead** | [tonykenga23@gmail.com](mailto:tonykenga23@gmail.com) |
| **Project Maintainer** | Tony Kenga |
| **Organization** | [skynet-datagrid-labs](https://github.com/skynet-datagrid-labs) |
| **Repository** | [github.com/skynet-datagrid-labs/MindfulSupport](https://github.com/skynet-datagrid-labs/MindfulSupport) |

---

## 12. Change Log

| Date | Version | Changes |
|:---|:---|:---|
| 2026-06-21 | 1.0.0 | Initial security policy creation |
| - | - | *Future updates to be logged here* |

---

**Build with rigor. Deploy with intent. Document with pride.**

**Mindful Support** — *AI Mental Health Chatbot*

---

<div align="center">

[![Security Policy](https://img.shields.io/badge/Security-Policy-FF6B6B?style=for-the-badge&logo=github&logoColor=white)](SECURITY.md)
[![Report Vulnerability](https://img.shields.io/badge/Report-Vulnerability-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:tonykenga23@gmail.com)

</div>
