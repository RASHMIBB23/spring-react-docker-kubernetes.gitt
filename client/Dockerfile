# Use the official Node.js image as the parent image
FROM node:20 AS build

# Set the working directory inside the container
WORKDIR /client

# Copy package*.json which is used by npm install
COPY package*.json  /client/

# Install the dependnecies
RUN npm install

# Copy everything from client folder 
COPY . /client/

# Expose the port the app runs on
 EXPOSE 3000

# Command to run the app
CMD ["npm","start"]

# build an image using docker:
    # docker build -t roomapp/frontend .
# to run an image:
    # docker run -p 3000:3000 roomapp/frontend