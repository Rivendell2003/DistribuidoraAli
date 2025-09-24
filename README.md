Proyecto: Distribuidora Ali - Calculadora de Despachos
Descripción
Esta es una aplicación móvil desarrollada en Android para la distribuidora Ali. Su propósito es calcular de forma automática el costo de despacho de mercancías en pesos chilenos ($) basándose en la distancia desde nuestra bodega en Quilpué hasta la ubicación actual del cliente. La aplicación utiliza la geolocalización del dispositivo y aplica una lógica de cálculo que depende del monto total de la compra.

Características Principales
Geolocalización Automática: Obtiene la ubicación del cliente de forma automática para calcular la distancia.

Cálculo de Costo de Despacho: Determina el costo de envío basándose en el monto de la compra y la distancia.

Compras superiores a $50.000 CLP: Despacho gratuito.

Compras entre $25.000 y $49.999 CLP: El costo se calcula a $150 CLP por kilómetro.

Compras inferiores a $25.000 CLP: El costo se calcula a $300 CLP por kilómetro.

Interfaz de Usuario Sencilla: Una interfaz limpia e intuitiva para que los usuarios puedan ingresar el monto y ver el resultado rápidamente.

Integración con Firebase: Los datos de cada despacho se guardan en una base de datos de Firebase para un registro histórico y análisis.

Tecnologías Utilizadas
Lenguaje de Programación: Kotlin

Plataforma: Android Studio

Librerías Clave:

Google Play Services Location: Para la geolocalización.

Firebase Authentication & Realtime Database: Para el almacenamiento de datos.

View Binding: Para una interacción segura con la interfaz de usuario.
