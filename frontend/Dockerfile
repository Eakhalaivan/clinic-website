# Build stage
FROM node:22-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine

# Add a non-root user (nginx image already has 'nginx' user, but we need to ensure permissions)
RUN mkdir -p /var/cache/nginx /var/run /var/log/nginx && \
    chown -R nginx:nginx /var/cache/nginx /var/run /var/log/nginx /etc/nginx/conf.d

COPY --from=build --chown=nginx:nginx /app/dist /usr/share/nginx/html

# Add custom nginx config for React Router (SPA fallback)
RUN echo 'server { \
    listen 8080; \
    location / { \
        root /usr/share/nginx/html; \
        index index.html index.htm; \
        try_files $uri $uri/ /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf && chown nginx:nginx /etc/nginx/conf.d/default.conf

USER nginx

EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
