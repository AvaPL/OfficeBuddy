FROM node:20 AS build

WORKDIR /app
RUN npm install -g envsub
COPY package.json package-lock.json ./
RUN npm install
COPY . .
ARG KEYCLOAK_URL
ENV KEYCLOAK_URL=$KEYCLOAK_URL
ARG KEYCLOAK_REALM
ENV KEYCLOAK_REALM=$KEYCLOAK_REALM
ARG KEYCLOAK_CLIENT_ID
ENV KEYCLOAK_CLIENT_ID=$KEYCLOAK_CLIENT_ID
RUN envsub -d src/environments/environment.docker.ts
RUN npm run build -- --configuration docker
ARG BACKEND_PROXY_URL
ENV BACKEND_PROXY_URL=$BACKEND_PROXY_URL
RUN envsub -d nginx.conf

FROM nginx:1.27-alpine

COPY --from=build /app/dist/ui/browser /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
