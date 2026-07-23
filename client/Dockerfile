# ---- Stage 1: Build the production React bundle ----
FROM node:20 AS build

WORKDIR /client

COPY package*.json /client/
RUN npm install

COPY . /client/

ENV PUBLIC_URL=/
RUN npm run build

# ---- Stage 2: Serve the built static files with Nginx ----
FROM nginx:1.27-alpine

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build /client/build /usr/share/nginx/html

EXPOSE 3000

CMD ["nginx", "-g", "daemon off;"]
