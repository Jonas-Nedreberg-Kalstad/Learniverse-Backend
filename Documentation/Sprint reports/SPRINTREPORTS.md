# Sprint Retrospective Report

## Sprint 1: Planning (Due date: February 18, 2025)
**When did you work with the project – which weeks?**  
We worked on the planning phase from week 3 to week 7, covering the period from January 20 to February 18, 2025.

**What was the goal in this sprint?**  
The primary goal of this sprint was to establish the foundation for our Course Management Backend Application. This involved making critical decisions about the technology stack, database system, and designing the database schema. We needed to determine which backend framework would best suit our needs, select appropriate database tools, and set up the initial project structure. We also aimed to define the core entities that would form the basis of our application's data model.

**How was the work distributed among group members?**  
The work was distributed between Jonas and Tobias, as we are the only two members in this group project. Jonas was responsible for setting up the initial project structure, developing the User and Course data models, implementing payment-related entities, and establishing the security foundation. Tobias was assigned the tasks of creating Provider-related entities, implementing Order data models with their relationships, documenting the entity classes, and ensuring appropriate structure for payment-related classes. Both team members shared responsibility for making technology stack decisions, designing the database schema, and planning the overall entity relationship architecture for the application.

**What was accomplished in this sprint?**  
We successfully completed several key planning tasks:

1. We discussed and decided on Spring Boot as our backend framework, setting up the starter project configuration and Maven build system.

2. After thorough evaluation, we selected MySQL as our database system and chose appropriate database design tools for creating the schema diagrams.

3. We designed a comprehensive database schema that would support all our application's requirements, including tables for courses, users, payments, and related entities.

4. We created the foundation for our data model by defining the core entity classes:
   - Course-related entities including Course, Topic, Category, and related course details
   - User and related entities for authentication and profile management
   - Payment-related entities to handle various payment methods
   - Order-related entities for tracking purchases and transactions
   - Advertisement-related entities for promotional content

By the end of the sprint, we had established a solid foundation for the implementation phase. The decisions made regarding technology stack and database design were documented, and the initial entity classes were created. This preparation set us up for successful implementation in the subsequent sprints.

## Sprint 2: Core Backend Implementation (Due date: March 17, 2025)
**When did you work with the project – which weeks?**  
We worked on this sprint from week 8 to week 11, covering the period from February 19 to March 17, 2025.

**What was the goal in this sprint?**  
The primary goal of this sprint was to implement the core backend functionality of our Course Management Application. This included setting up the security infrastructure, implementing user authentication, creating the database entity models with ORM, and developing the fundamental API endpoints. We aimed to establish a solid foundation for the more advanced features to be built upon in the subsequent sprint.

**How was the work distributed among group members?**  
Jonas was assigned responsibility for the entire security infrastructure, including JWT implementation, authentication mechanisms, and filter configuration. He was also tasked with building the user authentication endpoints, signup functionality, and developing the core user service business logic. Additionally, he was responsible for creating a global exception handling system for the application. Tobias was responsible for implementing the user repository layer, adding the review system entity structure, developing the user management endpoints, and creating the course management system. He was also tasked with implementing the data transfer object (DTO) mapping system and ensuring comprehensive API documentation. Both team members shared responsibility for testing the security implementation, fixing authentication issues, ensuring appropriate error handling, and maintaining documentation standards across the codebase.

**What was accomplished in this sprint?**  
We successfully delivered the following core components of our backend application:

1. Implemented Spring Security with JWT authentication, including:
   - Security configuration for endpoint protection
   - JWT request filter for authenticating API requests
   - JWT utilities for token generation and validation

2. Created user authentication endpoints:
   - Sign-up functionality with proper validation
   - Login endpoint with JWT token generation
   - User controller with basic CRUD operations

3. Established the database architecture:
   - Auto-generated database tables using JPA/Hibernate ORM
   - Refactored and improved the initial entity models
   - Added review entity and related models

4. Set up API documentation and testing:
   - Created Swagger documentation for user and authentication endpoints
   - Developed Postman tests for user-related functionality
   - Implemented a global exception handler for consistent error responses

By the end of this sprint, we had a functioning authentication system with secure endpoints, user management capabilities, and a solid database structure. The foundation was well-established making it possible to build and expand upon the core implementation. This makes it easier for future development expansion which can include more advanced features.

## Sprint 3: Advanced Features Implementation (Due date: April 20, 2025)
**When did you work with the project – which weeks?**  
We worked on this sprint from week 12 to week 16, covering the period from March 18 to April 20, 2025.

**What was the goal in this sprint?**  
The main goal of this sprint was to implement the advanced features of our application, including comprehensive course management, payment processing, review system, provider management, and sophisticated search functionality, email and receipt generation. We also aimed to complete API documentation and testing for all endpoints to ensure a robust and well-documented system.

**How was the work distributed among group members?**  
Jonas was responsible for designing and implementing the advanced search system, including the algorithm development, data structure optimization, and scoring mechanism. He was also assigned the provider management system, receipt generation service, email notification system, and utility classes for common operations. Tobias was in charge of the course management system maintenance, user profile functionality, review system implementation, and order processing flow. He was also tasked with implementing secure payment handling, fixing database structure issues, and creating multi-criteria search capabilities for courses. Both team members shared responsibility for code quality improvements, comprehensive documentation, testing the search functionality, and enhancing the application's error handling system.

**What was accomplished in this sprint?**  
We successfully implemented a wide range of advanced features:

1. Course Management System:
   - Developed course service with comprehensive business logic
   - Implemented CRUD endpoints for courses and related entities
   - Created course enrollment functionality

2. Review and Provider Systems:
   - Built endpoints and business logic for the review system
   - Implemented provider management functionality
   - Connected courses to providers and reviews

3. Payment and Order Processing:
   - Developed payment and order processing functionality
   - Implemented a receipt generation system with PDF output
   - Created an email service to send purchase confirmations to users

4. Advanced Search Capability:
   - Implemented a sophisticated search algorithm using Damerau Levenshtein Distance
   - Optimized search performance with a custom BK-Tree data structure implementation
   - Developed a scoring system for relevant search results
   - Enhanced the search with unordered term matching and combined scoring
   - Making the search generic so it can be used for any number of different entity classes in the future, making it scalable

5. Documentation and Testing:
   - Created comprehensive Swagger documentation for all controllers
   - Developed extensive Postman test suites for all endpoints
   - Ensured proper error handling throughout the application and in the global exception handler, creating custom exceptions when needed

By the end of this sprint, we had successfully implemented all the planned features of our Course Management Backend Application. The system was now capable of handling user authentication, course management, enrollments, payments, reviews, and advanced search functionality. Comprehensive documentation and testing ensured the reliability and usability of the API.

## Sprint 4: Deploy Backend to Server (Due date: May 12, 2025)
**When did you work with the project – which weeks?**  
We worked on this sprint from week 17 to week 19, covering the period from April 21 to May 12, 2025.

**What was the goal in this sprint?**  
The primary goal of this sprint was to deploy our Course Management Backend Application to a cloud environment using Azure. We aimed to establish a robust CI/CD pipeline that would automate the build, test, and deployment processes, ensuring reliable and consistent delivery of our application.

**How was the work distributed among group members?**  
Jonas and Tobias collaborated on infrastructure deployment with containerization and VM configuration scripts. Jonas was responsible for documentation structure, Azure integration, improving provider management features and finalizing the Postman testsuite making it re-entrant. Tobias handled security enhancements for user data, endpoint security, and implementing administrative connections between users and providers. Both team members shared responsibility for deployment documentation, testing the Azure deployment process, and verifying security before final deployment.

**What was accomplished in this sprint?**  
When the sprint started, we started learning Azure, how to create resources, managing secrets etc. This took time. Then we started configuring and testing provisioning of VMs in Azure and trying to deploy our project to cloud manually with HTTPS and all the neccecary configurations that comes with that. What we managed to complete during the sprint:

1. Azure Environment Setup:
   - Created and configured Azure resources for hosting our Spring Boot application
   - Set up the Azure MySQL database service for production data
   - Configured network security and access controls
   - Set up Azure Blob Storage / Storage Container
2. Manual deployment
   - Creating scripts that sets up the environment / server on deployment which can be automated later on
   - Dockerizing the application on deployment
   - Backend is configured behind a proxy when communicating with Nginx
3. Small enhancements
   - Fixing security issues for provider and admin users
   - Enhancing swagger documentation
   - Making Postman test re-entrant

**Problems encountered**  
We encountered many issues when dockerizing, configuring the nginx and setting up the cloud infrastructure, so everything took longer than we initially expected, and we were not able to complete the CI/CD pipeline during this sprint.


## Sprint 5: Finalize Project (Due date: May 23, 2025)
**When did you work with the project – which weeks?**  
We are working on this sprint in weeks 20-21, covering the period from May 13 to May 23, 2025.

**What is the goal in this sprint?**  
The goal is to finalize our project for submission by completing documentation, creating presentation materials, create and maintain a detailed README.md file and applying final polishing touches to the codebase.

**How is the work being distributed among group members?**  
Jonas is responsible for creating the project's technical documentation, including the setup guide, API references, and cloud infrastructure documentation. He is also tasked with finalizing the cloud configuration scripts. Tobias tested all API endpoints and user workflows. Both team members collaborated on scanning through project to see if we had all required documentation and mandatory specification.

**What has been accomplished in this sprint so far?**

1. Deployment:
   - Pushed all finished IaC and initialization scripts
   - Outlined a CI/CD pipeline for backend
2. Documentation:
   - Created and maintained a detailed README.md file with setup instructions for the project
   - Uploading all remaining documentation in a readable format
3. Code Finalization:
   - Scanning through project to see if all code and documentation is consistent
   - Doing minor enhancements and bug fixes

Though we weren't able to integrate Azure DevOps into our project in time, the CI/CD pipeline is still outlined and should work. However, the attempt to get everything to work perfectly took a lot of time and ended up spending a lot of the time in this sprint working on the deployment still.


# Project Retrospective

We are very satisfied with the final results as we have learnt alot about backend technology when it comes to design patterns like DTO, MVC, and handling dataflows correctly through the Spring Boot application. Figuring out smart solutions to problems like search using Damerau Levenshtein Distance algorithm to calculate the distance between words / phrases, creating and using BK-Tree to optimize the search and creating a comprehensive scoring / weighting system for the search. Also deploying the server to an industry standard cloud provider like Azure with HTTPS. This made the course very fun and very relavant to the industry.

Though we were only two people a lot has been achieved, however during this project we might have been a little too ambitious. We wanted the application to be in a minimum viable product stage with possibility for further expantion and with automatic deployment to the cloud server. This proved to be more time consuming than we initially expected and ended up spending a significant amount of extra time working on this project than expected.

## Requirements
The application has fufilled all expected features outlined in the project requirements with minor adjustments, as we found it better to separate the courses with multiple providers into all separate courses with a singular provider.

### Extra Work:

- Comprehensive exception handling with custom exception classes through a global exception handler that listens on specified exceptions
- Unordered search combined with Damerau Levenshtein Distance using a custom BK-Tree datastructure for optimization. Using factory pattern to initialize BK-Tree
- Custom scoring system for search using weights for unordered and fuzzy search.
- Extensive use of Mappers to reduce boilerplate and increase readability
- Sending Email and PDF generation
- Using DTO and MVC pattern maintain and send data following best practices

User
- Request reciept
- Change password
- Review courses

Provider
- Deactivate course
- Open an already existing course

Admin
- View all users
- View all providers
- Create new provider user and organization
- Soft delete users, courses and providers

Deployment
- Backend is dockerized
- CI/CD Pipeline (almost)
- Azure Cloud infrastructure
- Nginx communication with backend through proxy
- publicly accessible website

Security
- JWT Token
- Key vaults and secrets
- Provider users can only create/edit a course for their provider, and cant access other providers

**What could have been done better:**

For the size of the group we tried to implement too many features, which ended up with us spending too much time on features which was out of the scope of the project. We should have started with a more modest goal rather than creating a more complex CRUD application, which impacted other courses we took this semester.

Since we created a very ambitious database schema with many tables and potential features made the implementation easier, but some of the tables is not in use, since we did not have enough time to implement all those features. We should rather have implemented the core functionality and then expand later when we saw we had time.

We should have shorter sprints, maybe a sprint every 1 or 2 weeks. This is due to continuous improvement, making it easier to identify and fix issues earlier, adjust priorities more frequently, and maintain better project momentum. Shorter feedback cycles would have helped us avoid feature overload and better manage our time commitments across all courses.


# Hotfix before final release:
- Fixed Swagger API and config to point to correct URL / Path
- Updating .env variables and compose.yaml
- Finishing touches on readme.md