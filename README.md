Introducción
En el contexto actual del comercio electrónico y la logística, la eficiencia en la gestión de despachos es un factor importante para el éxito de cualquier negocio. La Distribuidora Ali busca modernizar sus procesos para ofrecer un servicio más ágil y transparente a sus clientes. Actualmente, el cálculo de los costos de envío se realiza de forma manual, lo que es propenso a errores y consume tiempo valioso.

Para solucionar esta problemática, este proyecto propone el desarrollo de una aplicación móvil en Android que automatiza este proceso. La aplicación permitirá a los clientes ingresar el monto de su compra y, utilizando la geolocalización del teléfono, obtendrá de manera precisa la distancia a la bodega de la distribuidora.

Con esta información, la aplicación calculará instantáneamente el costo del despacho, brindando una experiencia de usuario superior y optimizando las operaciones de la empresa. El enfoque se centra en la creación de un producto mínimo viable (MVP) que cumpla con las funcionalidades esenciales para luego ser escalado.

Desarrollo
El desarrollo de la aplicación se centró en tres áreas principales: la interfaz de usuario, la lógica de negocio y la integración con la base de datos.

1. Funcionalidades y Casos de Uso
Caso de Uso 1: Cálculo de Despacho: El usuario ingresa el monto total de su compra. La aplicación solicita permiso para acceder a la ubicación del dispositivo. Después de obtenerla, calcula la distancia al punto de distribución. Luego, aplica la siguiente lógica:

Si el monto es superior a $50.000 CLP, el costo es $0 CLP.

Si el monto está entre $25.000 y $49.999 CLP, el costo es de $150 CLP por kilómetro.

Si el monto es menor a $25.000 CLP, el costo es de $300 CLP por kilómetro.

Nótese: La guía de orientaciones de la actividad no menciona dónde está ubicada la empresa para poder hacer el cálculo real. Se asume que está en la localidad de Quilpué.

Caso de Uso 2: Visualización de Resultados: La aplicación muestra de manera clara y organizada la distancia calculada y el costo total del despacho en la interfaz principal.

Caso de Uso 3: Almacenamiento de Datos: Cada cálculo de despacho exitoso se registra en la base de datos de Firebase, incluyendo el monto de la compra, la distancia, el costo y la fecha del registro.

2. Implementación Técnica
Lenguaje de Programación: El proyecto se desarrolló en Kotlin, el lenguaje preferido para el desarrollo de aplicaciones Android nativas, por su seguridad y concisión.

Gestión de Dependencias: Se utilizó Gradle para manejar las librerías necesarias, como Google Play Services Location para la geolocalización, Firebase para la base de datos y autenticación, y Androidx para la compatibilidad con el diseño.

Interfaz de Usuario (UI): El diseño de la interfaz se implementó utilizando XML y ConstraintLayout para una disposición flexible. Se utilizó View Binding para acceder de forma segura y eficiente a los elementos de la interfaz, evitando errores comunes como NullPointerException.

Geolocalización: La obtención de la ubicación se manejó a través del Fused Location Provider Client, una API de Google que fusiona la información del GPS, Wi-Fi y redes móviles para obtener una ubicación precisa y optimizada.

Conclusión
La creación de esta aplicación para la Distribuidora Ali demuestra cómo la tecnología móvil puede resolver problemas operativos del mundo real. Al automatizar un proceso manual, no solo se elimina el error humano, sino que se proporciona un servicio más transparente y eficiente para el cliente final. La integración con Firebase sienta las bases para futuras mejoras, como la creación de un historial de compras, la gestión de perfiles de usuario y la posibilidad de implementar un sistema de pagos. Este proyecto ha sido un ejercicio valioso en la aplicación de conceptos de ingeniería de sistemas, desde la planificación de la arquitectura hasta la implementación y depuración del código, confirmando la viabilidad de la solución propuesta.

Bibliografía
Firebase Documentation. Google. Recuperado de https://firebase.google.com/docs

Google Play Services Location API. Google. Recuperado de https://developers.google.com/android/guides/location

Kotlin Documentation. JetBrains. Recuperado de https://kotlinlang.org/docs/home.html
