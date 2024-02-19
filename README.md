# Task_GithubAPI 🚀

This project is a Spring Boot application designed to interact with the GitHub API. 
It provides a simplified interface for fetching user repositories and branches.

## Getting Started 🌟

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites 🛠️

- Java JDK 21 or later ☕
- Gradle
- A GitHub account

### Installing 📋

1. **Clone the Repository**

    ```sh
    git clone https://github.com/JuliaSawczenko/Task_GithubAPI.git
    ```

2. **Navigate to the Project Directory**

    ```sh
    cd Task_GithubAPI
    ```

3. **Set Up GitHub Personal Access Token**

    Create a fine-grained personal access token in your GitHub account to use with the application. Make sure to mark "All Repositories" in Repository Access. Set it as an environment variable:

    - **For Linux/macOS**:
        ```sh
        export GITHUB_JWT=<Your_GitHub_Personal_Access_Token>
        ```
    - **For Windows Command Prompt**:
        ```cmd
        set GITHUB_JWT=<Your_GitHub_Personal_Access_Token>
        ```
    - **In IntelliJ IDEA** 
        - Navigate to `Run` > `Edit Configurations...`
        - In the `Environment variables` field, add: `GITHUB_JWT=<Your_GitHub_Personal_Access_Token>`
        - Do this for both application and integration tests

4. **Build the Project**
   
    This command compiles the project and runs any tests.

    ```sh
    ./gradlew build
    ```
    
5. **Run the Application**

    To start the application, use:

    ```sh
    ./gradlew bootRun
    ```
    

## Usage 📝

1. **Access the Endpoint**
   
   Open your favorite HTTP client (like Postman or your browser) and navigate to:
   
   ```localhost:8080/user/{username}/repositories```

   Replace `{username}` with the GitHub username you're interested in.

3. **Set the Right Header**
   
   Make sure to include:

   ```Accept: application/json```

5. **Hit Send** 🚀
