CIVOK BANK V2
A spring boot banking backend application built to model core banking operations while applying secure API design, layered microservice 
architecture and simulate real-world banking process

Features
- Implementation of Exception handling in all services with custom API error responses

- Authentication Service
  * Authentication of administrators
  * JWT generation for authenticated users
  * Stateless JWT based authentication
  * Role based access authorization based on JWT claims
  * Separation of administrative access based on roles (OWNER/STAFF/AUDITOR)

 
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
  * Internal Tracking and logging of all major Application actions


- Notification Service
  * Internal creation of Notifications 
  * Sending notification to recipient email address using Java Mail Sender and custom Thymeleaf Templates
  * Logging of total sending attempts and successful attempts
  * 

- Customer Service 
  * Creation of Customer Profiles based on RBAC for (Owner and Staff only)
  * Profile Management

- Account Service 
  * Customer Bank Account generation and creation
  * Balance Enquiry by Customer

Technologies
- Java 21
- Spring Boot
- Spring Security
- Spring Security 0Auth 2 Server
- Spring Web
- Spring Data JPA
- Jakarta Validation
- JWT
- Maven
- MySQL workbench
- Lombok
- Postman
- Validation


Architecture
CivokBankV2 solution follows a microservice architecture currently with implementation for services for 
1. Auth Service
2. Admin Service
3. Audit Service
4. Notification Service
5. Customer Service
6. Account Service

Each service in turn follows a layered architecture
Controller → Service → Repository → Database
Service interfaces separate business contracts from their implementation while spring dependency injection manages application dependencies
and promotes loose coupling between application components

Current Service Communication
The services communicate through REST APIs.
For example, during admin authentication, Auth Service → Admin Service → Database  | The Auth service delegates credential verification to 
Admin Service which owns the Admin data. Following successful verification, Auth Service then generates JWT for subsequent requests.

Administrative actions that require auditing are sent to Audit serves, Admin Service → Audit Service → Database

NOTE
Project is still under development. Next focus is on development of Transaction Service
