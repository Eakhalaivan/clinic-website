# ──────────────────────────────────────────────────────────────
# Stage 1: Build
# ──────────────────────────────────────────────────────────────
FROM node:22-alpine AS build
WORKDIR /app

# Accept build-time API URL (optional — used when you DO know the URL at build time)
ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# ──────────────────────────────────────────────────────────────
# Stage 2: Serve with nginx + runtime env injection
# ──────────────────────────────────────────────────────────────
FROM nginx:alpine

# Ensure proper permissions
RUN mkdir -p /var/cache/nginx /var/run /var/log/nginx && \
    chown -R nginx:nginx /var/cache/nginx /var/run /var/log/nginx /etc/nginx/conf.d

COPY --from=build --chown=nginx:nginx /app/dist /usr/share/nginx/html

# nginx config: SPA fallback + serve /env-config.js dynamically
RUN printf 'server {\n\
    listen 8080;\n\
    location / {\n\
        root /usr/share/nginx/html;\n\
        index index.html index.htm;\n\
        try_files $uri $uri/ /index.html;\n\
    }\n\
}\n' > /etc/nginx/conf.d/default.conf && chown nginx:nginx /etc/nginx/conf.d/default.conf

# Entrypoint script: inject VITE_API_BASE_URL at container start time
# This writes window.__ENV__ so the app can read it without a rebuild
COPY --chown=nginx:nginx docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
