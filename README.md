# Ciphon

A dockerised web-API for the decryption of classical ciphers.

[![Build Status](https://travis-ci.com/bridges-wood/Ciphon.svg?branch=master)](https://travis-ci.com/bridges-wood/Ciphon)

## Getting Started

### Prerequisites

To run this project as intended on your system, you're going to need Java 11 or higher, Docker and Maven. 

#### Java
Download the Ready for Use version from https://jdk.java.net/ .

#### Maven
Follow the [install guide](https://maven.apache.org/install.html) for your platform.

#### Docker
Follow the [install guide](https://docs.docker.com/get-docker/) for your platform.

### Installing

1. Build and package with _mvn_.
```
mvn -B package --file pom.xml
```

2. Build Docker Image with _docker_.
```
docker build . --file Dockerfile -t <name>:<tag>
```

3. __Optional__ Push to DockerHub
```
echo <DockerHub Password> | docker login -u <DockerHub Username> --password-sdtin
docker push <name>:<tag>
```

## Running the tests
```
mvn clean test
```

## Deployment

Build with docker as shown previously and deploy image.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - API endpoint management and web interface
* [Commons Math 3](https://commons.apache.org/proper/commons-math/) - Mathematical tools
* [Kyro](https://github.com/EsotericSoftware/kryo) - File serialisation
* [Gson](https://github.com/google/gson) - JSON management
* [Maven](https://maven.apache.org/) - Dependency Management
* [Docker](https://www.docker.com/) - Containerisation

## Contributing

This project is currently closed to contributions as it is part of a controlled assessment for an A-Level Computer Science qualification.

## Authors

* **Max Wood** - *Initial work* - [bridges-wood](https://github.com/bridges-wood)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
