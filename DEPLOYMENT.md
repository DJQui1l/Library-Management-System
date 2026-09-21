# Library Management System - Docker & AWS Deployment Guide

## Prerequisites

- Docker installed locally
- AWS CLI configured with credentials
- AWS account with ECR, EC2, and S3 access
- Maven (for building locally)

## Local Development with Docker

### Using Docker Compose (Recommended)

1. Set environment variables (optional, defaults provided):
   ```bash
   export DB_USERNAME=root
   export DB_PASSWORD=password
   export AWS_REGION=us-east-1
   export AWS_S3_BUCKET_NAME=your-bucket-name
   ```

2. Start the application:
   ```bash
   docker-compose up --build
   ```

3. Access the application at `http://localhost:8080`

4. Stop the application:
   ```bash
   docker-compose down
   ```

### Manual Docker Build

1. Build the JAR:
   ```bash
   mvn clean package
   ```

2. Build Docker image:
   ```bash
   docker build -t library-management .
   ```

3. Run with MySQL:
   ```bash
   docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=library_management -p 3306:3306 mysql:8.0
   docker run -d --name library-app -p 8080:8080 --link mysql:mysql -e DB_USERNAME=root -e DB_PASSWORD=password -e AWS_REGION=us-east-1 -e AWS_S3_BUCKET_NAME=your-bucket library-management
   ```

## AWS Deployment

### 1. Create ECR Repository

```bash
aws ecr create-repository --repository-name library-management --region us-east-1
```

### 2. Push to ECR

**Windows:**
```cmd
push-to-ecr.bat
```

**Linux/Mac:**
```bash
chmod +x push-to-ecr.sh
./push-to-ecr.sh
```

**Manual Push:**
```bash
# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com

# Build and tag
docker build -t library-management .
docker tag library-management YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/library-management:latest

# Push
docker push YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/library-management:latest
```

### 3. Deploy to EC2

**Prerequisites:**
- EC2 instance with Docker installed
- Security group allowing port 8080
- SSH access configured

**Windows:**
```cmd
set EC2_INSTANCE_ID=i-xxxxxxxxxxxxxxxxx
deploy-to-ec2.bat
```

**Linux/Mac:**
```bash
chmod +x deploy-to-ec2.sh
export EC2_INSTANCE_ID=i-xxxxxxxxxxxxxxxxx
./deploy-to-ec2.sh
```

**Manual Deployment:**
```bash
# SSH into EC2
ssh ec2-user@YOUR_EC2_PUBLIC_IP

# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com

# Pull image
docker pull YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/library-management:latest

# Stop old container
docker stop library-app
docker rm library-app

# Run new container
docker run -d --name library-app -p 8080:8080 \
  -e DB_USERNAME=YOUR_DB_USER \
  -e DB_PASSWORD=YOUR_DB_PASSWORD \
  -e DB_URL=YOUR_RDS_URL \
  -e AWS_REGION=us-east-1 \
  -e AWS_S3_BUCKET_NAME=YOUR_BUCKET \
  YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/library-management:latest
```

### 4. Configure AWS RDS (Optional)

1. Create RDS MySQL instance
2. Set environment variables:
   - `DB_URL` - RDS connection string
   - `DB_USERNAME` - RDS username
   - `DB_PASSWORD` - RDS password

3. Update `application.properties` by uncommenting AWS RDS section and commenting Docker section

### 5. Configure AWS S3

1. Create S3 bucket
2. Set bucket policy to allow uploads
3. Set environment variable:
   - `AWS_S3_BUCKET_NAME` - Your bucket name

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| DB_USERNAME | Database username | root |
| DB_PASSWORD | Database password | password |
| DB_URL | AWS RDS connection string | - |
| AWS_REGION | AWS region | us-east-1 |
| AWS_S3_BUCKET_NAME | S3 bucket name | - |

## Troubleshooting

### Docker Issues
- Check Docker is running: `docker ps`
- View logs: `docker logs library-app`
- Rebuild: `docker-compose up --build --force-recreate`

### AWS Issues
- Verify AWS credentials: `aws sts get-caller-identity`
- Check ECR repository exists: `aws ecr describe-repositories --repository-names library-management`
- Verify EC2 security group allows port 8080

### Database Connection
- Ensure MySQL container is running: `docker ps`
- Check database credentials in environment variables
- Verify RDS is accessible from EC2 (if using RDS)

## API Endpoints

- `POST /api/books` - Create book (multipart/form-data)
- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `PUT /api/books/{id}` - Update book (multipart/form-data)
- `DELETE /api/books/{id}` - Delete book
- `POST /api/authors` - Create author
- `GET /api/authors` - Get all authors
- `GET /api/authors/{id}` - Get author by ID
- `DELETE /api/authors/{id}` - Delete author
- `POST /api/publishers` - Create publisher
- `GET /api/publishers` - Get all publishers
- `GET /api/publishers/{id}` - Get publisher by ID
- `DELETE /api/publishers/{id}` - Delete publisher
