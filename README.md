# FarmFresh — Architecture & Design (Theory)

## Project overview
FarmFresh is a layered Java web application for managing milk suppliers, daily milk collections, product prices, automated payment generation and supplier notifications. It is built with Spring MVC, JPA/Hibernate and server-rendered JSP views. The system separates presentation, web/controller, service (business) and persistence layers to keep responsibilities clear and maintainable.

## Architecture & Layers
- Presentation layer  
  - Server-rendered JSPs provide HTML/CSS/JS for admin and supplier UIs. Handles basic input validation and file uploads.
- Web / Controller layer  
  - Controllers map HTTP requests to application actions, validate requests, call services, and return views or data (JSON/CSV/PDF).
- Service layer (business logic)  
  - Implements workflows: supplier onboarding, milk-collection management, pricing, payment generation and notification logic. Coordinates repositories and utility components (email, PDF, CSV).
- Persistence / Repository layer  
  - Entities map to database tables. Repositories perform CRUD and queries. Services use repositories to persist state.
- Infrastructure  
  - Configuration for database, file storage, mail, scheduler and multipart handling.

## Domain modelling
- Entities represent persistent state (Supplier, CollectMilk, PaymentDetails, Notification, audit entities).  
- DTOs decouple web/UI models from internal persistence models and simplify validation and data transfer.

## Transactions & Data Integrity
- Group related DB updates in transactional boundaries to ensure atomicity and consistency (e.g., generating payments and creating notifications).  
- Use audit entities to record historical changes for traceability; ensure audit writes align with transaction semantics.

## Key business workflows
- Supplier onboarding  
  - Validate input, persist supplier and bank details, store uploaded documents as files (DB stores paths), and send welcome/verification emails.
- Milk collection recording  
  - Admin records daily volumes per supplier. Collections become input for payment calculation.
- Payment generation  
  - Scheduled aggregation computes payables over a period using product prices and supplier data; persists PaymentDetails and Notification entries.
- Notifications & emails  
  - Send email notifications on payment creation or status changes. Prefer asynchronous delivery (queue/background tasks) for resilience and responsiveness.

## Scheduling & Asynchronous processing
- Use a scheduler for periodic payment generation and notification tasks.  
- Offload long-running tasks (email sending, PDF and CSV generation) to background workers to avoid blocking web requests and improve UX.

## File handling & static resources
- Store uploaded documents, product images and generated artifacts (PDF, CSV) on the filesystem or object storage; store only paths/URIs in DB.  
- Configure multipart limits and perform file content/type validation to prevent malicious uploads.
