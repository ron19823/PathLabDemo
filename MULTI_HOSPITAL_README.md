# PathLab Multi-Hospital Management System

## Overview

The PathLab Management System has been transformed into a comprehensive multi-hospital platform that allows different hospitals to have their own customizable patient registration workflows. Each hospital can define their own form fields, validation rules, and workflow steps based on their specific requirements.

## Key Features

### 🏥 Multi-Hospital Support
- **Hospital Selection**: Users can choose from multiple hospitals on the main dashboard
- **Hospital-Specific Dashboards**: Each hospital has its own customized interface
- **Hospital Management**: Complete CRUD operations for hospital entities

### 📋 Customizable Workflows
- **Dynamic Form Fields**: Each hospital can define custom form fields
- **Multiple Field Types**: Support for text, number, email, phone, date, select, textarea, and password fields
- **Field Validation**: Custom validation rules for each field
- **Step-by-Step Workflows**: Multi-step registration processes
- **Field Ordering**: Customizable field order within each workflow step

### 🎯 Hospital-Specific Examples

#### Apollo Hospital (APOLLO)
- **Aadhar Required**: Mandatory Aadhar number verification
- **Comprehensive Fields**: Name, Age, Aadhar, Blood Group, Phone
- **Validation**: Aadhar number must be 12 digits
- **Single Step Workflow**: Streamlined registration process

#### City General Hospital (CITY)
- **No Aadhar Required**: Flexible ID requirements
- **Optional Fields**: ID number is optional
- **Simple Workflow**: Basic registration without complex validation
- **Community Focus**: Designed for local community access

#### Metro Medical Center (METRO)
- **Multi-Step Process**: 3-step registration workflow
- **Step 1 - Basic Information**: Name, Date of Birth, Gender, Aadhar
- **Step 2 - Medical History**: Blood Group, Allergies, Current Medications, Medical History
- **Step 3 - Emergency Contact**: Emergency contact details and relationship
- **Comprehensive Data Collection**: Detailed patient information gathering

## Technical Architecture

### Backend Components

#### Data Models
- **Hospital**: Hospital information and configuration
- **Workflow**: Multi-step registration processes
- **FormField**: Dynamic form field definitions
- **Patient**: Patient data with custom fields
- **PatientRecord**: Medical records and history

#### Services
- **HospitalService**: Hospital management operations
- **WorkflowService**: Workflow configuration and management
- **FormFieldService**: Dynamic form field management
- **DataInitializationService**: Sample data population

#### Controllers
- **DashboardController**: Main dashboard with hospital selection
- **HospitalController**: Hospital CRUD operations
- **WorkflowController**: Workflow management
- **FormFieldController**: Form field management

### Frontend Components

#### Dynamic Dashboard
- **Hospital Selection Interface**: Card-based hospital selection
- **Dynamic Form Generation**: Forms generated based on workflow configuration
- **Step Navigation**: Multi-step form navigation with progress indicators
- **Real-time Validation**: Client-side validation based on field rules

#### Workflow Management
- **Visual Workflow Editor**: Drag-and-drop workflow creation
- **Field Configuration**: Dynamic field creation and editing
- **Validation Rules**: Custom validation rule configuration
- **Preview Mode**: Real-time form preview

## Database Schema

### Hospital Table
```sql
CREATE TABLE hospitals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Workflow Table
```sql
CREATE TABLE workflows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    step_order INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    hospital_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);
```

### Form Field Table
```sql
CREATE TABLE form_fields (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    field_name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    field_type ENUM('TEXT', 'NUMBER', 'EMAIL', 'PHONE', 'DATE', 'SELECT', 'TEXTAREA', 'PASSWORD') NOT NULL,
    is_required BOOLEAN DEFAULT FALSE,
    field_order INT NOT NULL,
    validation_rules TEXT,
    options TEXT,
    placeholder VARCHAR(1000),
    help_text VARCHAR(1000),
    is_active BOOLEAN DEFAULT TRUE,
    workflow_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (workflow_id) REFERENCES workflows(id)
);
```

## API Endpoints

### Hospital Management
- `GET /hospitals` - List all hospitals
- `GET /hospitals/{code}` - Get hospital by code
- `POST /hospitals` - Create new hospital
- `PUT /hospitals/{id}` - Update hospital
- `GET /hospitals/api` - API endpoint for hospital list

### Workflow Management
- `GET /workflows/hospital/{hospitalCode}` - Get workflows for hospital
- `POST /workflows/hospital/{hospitalCode}` - Create workflow
- `PUT /workflows/{id}` - Update workflow
- `DELETE /workflows/{id}` - Delete workflow
- `GET /workflows/api/hospital/{hospitalCode}` - API endpoint for workflows

### Form Field Management
- `GET /form-fields/workflow/{workflowId}` - Get fields for workflow
- `POST /form-fields/workflow/{workflowId}` - Create form field
- `PUT /form-fields/{id}` - Update form field
- `DELETE /form-fields/{id}` - Delete form field
- `GET /form-fields/api/workflow/{workflowId}` - API endpoint for fields

## Usage Examples

### Accessing Hospital Dashboards

1. **Main Dashboard**: Navigate to `/path-lab/dashboard`
2. **Hospital Selection**: Choose from available hospitals
3. **Hospital-Specific Dashboard**: Access via `/path-lab/hospital/{hospitalCode}`

### Sample Hospital Codes
- **Apollo Hospital**: `/path-lab/hospital/APOLLO`
- **City General Hospital**: `/path-lab/hospital/CITY`
- **Metro Medical Center**: `/path-lab/hospital/METRO`

### Workflow Management
- Navigate to `/path-lab/workflows/hospital/{hospitalCode}`
- Create, edit, and manage workflows
- Add custom form fields with validation rules
- Configure field ordering and requirements

## Configuration

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:pathlabdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Security Configuration
- Device-based access control
- Location-based restrictions
- Hospital-specific access control
- User authentication and authorization

## Development Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- Spring Boot 3.5.5

### Running the Application
1. Clone the repository
2. Run `mvn clean install`
3. Start the application with `mvn spring-boot:run`
4. Access the application at `http://localhost:8080/path-lab`

### Database Console
- H2 Console: `http://localhost:8080/path-lab/h2-console`
- JDBC URL: `jdbc:h2:mem:pathlabdb`
- Username: `sa`
- Password: `password`

## Customization Guide

### Adding a New Hospital
1. Create hospital entity via API or database
2. Define workflows for the hospital
3. Configure form fields for each workflow
4. Set up validation rules and field ordering

### Creating Custom Workflows
1. Define workflow steps and order
2. Add form fields with appropriate types
3. Configure validation rules
4. Set field requirements and help text
5. Test the workflow in the dashboard

### Field Type Configuration
- **TEXT**: Basic text input
- **NUMBER**: Numeric input with validation
- **EMAIL**: Email format validation
- **PHONE**: Phone number format validation
- **DATE**: Date picker input
- **SELECT**: Dropdown with predefined options
- **TEXTAREA**: Multi-line text input
- **PASSWORD**: Password input with masking

## Future Enhancements

### Planned Features
- **Role-Based Access Control**: Different user roles for different hospitals
- **Advanced Validation**: Server-side validation with custom rules
- **Report Generation**: Hospital-specific reports and analytics
- **Integration APIs**: Third-party system integration
- **Mobile Support**: Responsive design for mobile devices
- **Audit Logging**: Complete audit trail for all operations
- **Data Export**: Export patient data in various formats
- **Backup and Recovery**: Automated backup and recovery systems

### Scalability Considerations
- **Database Optimization**: Indexing and query optimization
- **Caching**: Redis integration for improved performance
- **Load Balancing**: Multi-instance deployment support
- **Microservices**: Breaking down into microservices architecture
- **Cloud Deployment**: AWS/Azure deployment configurations

## Support and Maintenance

### Monitoring
- Application health checks
- Database performance monitoring
- User activity tracking
- Error logging and alerting

### Maintenance Tasks
- Regular database backups
- Security updates and patches
- Performance optimization
- User training and documentation updates

## Conclusion

The PathLab Multi-Hospital Management System provides a flexible, scalable solution for managing patient registration across multiple hospitals. With its customizable workflows, dynamic form generation, and hospital-specific configurations, it can adapt to the unique requirements of any healthcare organization.

The system is designed with modern web technologies and follows best practices for security, performance, and maintainability. It provides a solid foundation for future enhancements and can scale to support hundreds of hospitals and thousands of patients.

