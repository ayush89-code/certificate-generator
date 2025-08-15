
Certificate Generator Spring Boot (PNG template)
-----------------------------------------------

How to run:
1. Import this project into IntelliJ as a Maven project (or unpack and run mvn spring-boot:run).
2. Ensure Java 17+ is configured in IntelliJ.
3. Run the Application class in com.example.certgen.Application
4. Open http://localhost:8080

Notes:
- Template: src/main/resources/sciencekedeewane_certificate.png
- Excel: src/main/resources/data.xlsx (expects header row; uses 2nd column as Name, 3rd as Description)
- Adjust name/skill positions in ImageService.java if alignment needs fine-tuning.
