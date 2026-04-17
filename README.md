# Support Java Developer - Technical Assessment Task

## Running

1. Open project in IDE of choice (tested here with Idea 2026.1 and 21.0.9+7-nixos Java runtime)
2. Start project:
   - either through IDE
   - or terminal:
     ```bash
     mvn spring-boot:run
     ```
3. API will be accessible at `http://localhost:8888/api/v1`


## Notes:

- classic project structure is used with clear separation of concerns
- custom exceptions were introduced to make errors more understandable
- openapi spec is available [here](./openapi.yaml)
