FROM php:7.4-apache-bullseye

# Instala as dependências do sistema necessárias para o Laravel e extensões PHP
RUN apt-get update && apt-get install -y \
    libpng-dev \
    libzip-dev \
    libxml2-dev \
    libonig-dev \
    zip \
    unzip \
    git \
    curl

RUN apt-get clean && rm -rf /var/lib/apt/lists/*

RUN docker-php-ext-install pdo_mysql mbstring exif pcntl gd zip

COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

RUN a2enmod rewrite

RUN sed -i 's|/var/www/html|/var/www/html/public|g' /etc/apache2/sites-available/000-default.conf
RUN sed -i 's|/var/www/html|/var/www/html/public|g' /etc/apache2/apache2.conf

# Configura o Apache para aceitar as diretivas do arquivo .htaccess do Laravel
RUN echo '<Directory /var/www/html/public/> \n\
    Options Indexes FollowSymLinks \n\
    AllowOverride All \n\
    Require all granted \n\
</Directory>' >> /etc/apache2/apache2.conf

WORKDIR /var/www/html

RUN chown -R www-data:www-data /var/www/html