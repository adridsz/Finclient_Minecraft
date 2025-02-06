# Finclient_Minecraft

Finclient es un mod para Minecraft 1.14.4 que añade una funcionalidad de "Killaura". Este mod permite al jugador atacar automáticamente a las entidades vivas dentro de su campo de visión (FOV) y a una distancia máxima definida.

## Características

- **Killaura**: Ataca automáticamente a las entidades vivas dentro del FOV del jugador.
- **Tecla de activación**: La tecla `R` activa o desactiva el mod.
- **Retardo entre ataques**: El mod incluye un retardo aleatorio entre ataques para simular clicks por segundo (CPS).

## Instalación

1. **Descargar e instalar Minecraft Forge**:
    - Ve a la [página de descargas de Minecraft Forge](https://files.minecraftforge.net/) y descarga la versión correspondiente a Minecraft 1.14.4.
    - Ejecuta el instalador de Forge y sigue las instrucciones para instalarlo.

2. **Descargar Finclient_Minecraft**:
    - Descarga el archivo `Finclient_Minecraft-1.14.4.jar` desde la [página de releases](https://github.com/adridsz/Finclient_Minecraft/releases) de este repositorio.

3. **Instalar ExampleMod**:
    - Copia el archivo `Finclient_Minecraft-1.14.4.jar` en la carpeta `mods` de tu directorio de Minecraft. Si no existe la carpeta `mods`, créala.

## Compilación

Si deseas compilar el mod por ti mismo, sigue estos pasos:

1. **Clonar el repositorio**:
   ```sh
   git clone https://github.com/adridsz/Finclient_Minecraft.git
   cd Finclient_Minecraft
    ```
2. **Compilar el mod**:
    ```sh
    ./gradlew build
    ```
    El archivo `.jar` compilado se encontrará en la carpeta `build/libs`.
3. **Instalar el mod**:
    - Copia el archivo `.jar` en la carpeta `mods` de tu directorio de Minecraft.
    - ¡Listo! Ya puedes disfrutar de Finclient en tu juego.

## Descargo de responsabilidad

Este mod ha sido creado con fines educativos y de entretenimiento. El uso de este mod en servidores públicos puede ser considerado trampa y resultar en una sanción. Úsalo bajo tu propia responsabilidad.