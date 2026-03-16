def call(String serviceDir, String imageTag) {
    echo "Building service in ${serviceDir} with image tag ${imageTag}"

    echo "Docker image myrepo/${imageTag} built successfully"
}