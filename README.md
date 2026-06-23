# Pasos para ejecutarlo
1. Instalar todas las dependencias necesarias con pnpm install en la raiz del proyecto
2. Ir a la carpeta /BD y levanta el contenedor de PostgreSQL con el comando docker-compose up -d
3. Ir a la carpeta /JavaHibernate para compilar y ejecutar la aplicación con el comando mvn clean compile exec:java -Dexec.mainClass="com.modelo.Main"
