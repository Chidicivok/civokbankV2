CIVOK BANK V2
A spring boot banking backend application built to model core banking operations while applying secure API design, layered microservice 
architecture and simulate raal world banking transaction processing

Features
- Authentication Service
  * Authentication of administrators
  * JWT generation for authenticated users
  * Stateless JWT based authentication
  * Role based acces authorization based on JWT claims
  * Seperation of administrative access based on roles (OWNER/STAFF/AUDITOR)

 
- Admin Service
  * Creation of Administrators
  * Secure one-way hashing using BCryptPasswordEncoder
  * Admin Account management
      <> Password reset (OWNER only) for any admin
      <> Name change (OWNER only) for any admin
      <> Password change by the account holder
  * Retrieval of Administrator details 
  * Integration with Audit service to create audit logs using RestClient


- Audit Service
  * Internal creation and storage of audit logs
  * Tracking of audit logs by (id, actor, actor type, target, target type, service, action, date)
  * Role based access to Audit Logs granted to (OWNER/AUDITOR only)



Technologies
- Java 21
- Spring Boot
- Spring Security
- Spring Security 0Auth 2 Server
- Spring Web
- Spring Data JPA
- Jarkata Validation
- JWT
- Maven
- MySQL workbench
- Lombok
- Postman
- Validation


Architecture
CivokbankV2 solution follows a microservice architecture currently with implementation for services for 
1. Auth Service
2. Admin Service
3. Audit Service

Each service in turn follows a layered architecture
Controller -> Service -> Repository -> Database
Service interfaces seperate business contracts from their implementation while spring dependency injection manages application dependencies
and promotes loose coupling between application components

Current Service Communication
The service communicate through REST APIs.
For example, during admin authentication, Auth Service -> Admin Service -> Database  | The Auth service deleagtes credential verification to 
Admin Service which owns the Admin data. Following succesful verification, Auth Service then genreates JWT for subsequent requests.

Administrive actions that require auditing are sent to Audit servies, Admin Service -> Audit Service -> Database

NOTE
Project is still under developemnt. Next focus is on Exception handling and Integration with External APIs
