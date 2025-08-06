# GymMonitor-Lite

## How to run

> **Note:** You must have Docker running before starting these steps.  
> - **Windows / macOS:** Open **Docker Desktop** to start the Docker Engine.  
> - **Linux:** Ensure the Docker daemon (`dockerd`) is running (usually starts automatically).

1. Open your terminal.  

2. Navigate to the project folder:

```bash
cd path/to/GymMonitor-Lite
```

3. Start the PostgreSQL database container:

```bash
docker compose up -d db
```

4. Build the Kotlin app container:

```bash
docker compose build
```

5. Start the Kotlin app container:

```bash
3. docker compose up kotlinapp
```