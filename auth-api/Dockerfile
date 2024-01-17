# Usa una imagen de PHP 8.1 con Apache como base
FROM php:8.2-apache

# Instala los paquetes necesarios y limpia la caché de APT
RUN apt-get update && apt-get install -y \
    libaio1 \
    unzip \
    git \
    libonig-dev \
    zlib1g-dev \
    libpng-dev \
    libjpeg-dev \
    libfreetype6-dev \
    && rm -rf /var/lib/apt/lists/*

# Configura ServerName para Apache
RUN echo 'ServerName localhost' >> /etc/apache2/apache2.conf

# Define variables de entorno para las versiones del Instant Client y SDK
ENV INSTANTCLIENT_VERSION=21_12_0_0_0

# Descarga e instala Oracle Instant Client y SDK
RUN curl -o instantclient-basic.zip https://download.oracle.com/otn_software/linux/instantclient/2112000/instantclient-basic-linux.x64-21.12.0.0.0dbru.zip \
    && curl -o instantclient-sdk.zip https://download.oracle.com/otn_software/linux/instantclient/2112000/instantclient-sdk-linux.x64-21.12.0.0.0dbru.zip \
    && unzip instantclient-basic.zip -d /opt/oracle \
    && unzip instantclient-sdk.zip -d /opt/oracle \
    && ln -s /opt/oracle/instantclient_21_12 /opt/oracle/instantclient \
    && rm instantclient-basic.zip \
    && rm instantclient-sdk.zip

# Configura las variables de entorno para el Instant Client
ENV LD_LIBRARY_PATH=/opt/oracle/instantclient
ENV ORACLE_BASE=/opt/oracle/instantclient
ENV ORACLE_HOME=/opt/oracle/instantclient
ENV TNS_ADMIN=/opt/oracle/instantclient

# Instala la extensión OCI8
RUN echo 'instantclient,/opt/oracle/instantclient' | pecl install oci8 \
    && docker-php-ext-enable oci8

# Instala y configura las extensiones adicionales que Laravel necesita
RUN docker-php-ext-configure gd --with-freetype --with-jpeg \
    && docker-php-ext-install pdo pdo_mysql mbstring exif pcntl bcmath gd

# Configura el directorio raíz de Apache a la carpeta public de Laravel
ENV APACHE_DOCUMENT_ROOT /var/www/html/public
RUN sed -ri -e 's!/var/www/html!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/sites-available/*.conf \
    && sed -ri -e 's!/var/www/!${APACHE_DOCUMENT_ROOT}!g' /etc/apache2/apache2.conf /etc/apache2/conf-available/*.conf

# Copia el código fuente de tu aplicación Laravel al contenedor
COPY . /var/www/html

# Otorga permisos al directorio de almacenamiento de Laravel
RUN chown -R www-data:www-data /var/www/html/storage \
    && chmod -R 775 /var/www/html/storage

# Instala Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

# Instala las dependencias de Laravel
RUN composer install --no-interaction --no-plugins --no-scripts --prefer-dist

RUN composer require yajra/laravel-oci8
# Instala Laravel JWT
RUN composer require tymon/jwt-auth

# Genera la clave JWT
RUN php artisan jwt:secret

# Exponer el puerto 8080
EXPOSE 8080

# Comandos para iniciar Apache
CMD ["apache2-foreground"]
