FROM node:lts-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
WORKDIR /usr/share/nginx/html
COPY --from=build /app/nginx-oasip.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist .
ENTRYPOINT ["nginx", "-g", "daemon off;"]
