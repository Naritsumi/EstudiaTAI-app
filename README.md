# 📚 App de estudios TAI AGE

Esta es una aplicación de código abierto desarrollada en **Kotlin** como parte de mi portfolio.  
El objetivo es ayudar a preparar oposiciones, en concreto la **convocatoria AGE para Técnicos Auxiliares de Informática (TAI)**, a través de tests temáticos sobre el temario de la convocatoria 2024.

La aplicación está en constante mantenimiento, y las preguntas incluidas son de elaboración propia.  

---

## 📥 Descarga

Puedes descargar la última versión de **EstudiaTAI App** desde los enlaces disponibles a continuación:

- **Versión v1.0.5**  
  🔗 [Descargar desde GitHub Releases](https://github.com/Naritsumi/EstudiaTAI-app/releases/tag/v1.0.5)

> También puedes encontrar versiones anteriores o futuras actualizaciones en la sección [Releases](https://github.com/Naritsumi/EstudiaTAI-app/releases) del repositorio oficial.

La aplicación estará disponible en **F-Droid**  

<!--<a href="https://f-droid.org/" target="_blank"> <img src="https://upload.wikimedia.org/wikipedia/commons/0/0d/Get_it_on_F-Droid.svg" alt="F-Droid" width="200"/> </a>-->

## ☕ Donaciones

Si te gusta el proyecto o quieres invitarme a un café, puedes hacerlo aquí:

<p align="center">
  <a href="https://ko-fi.com/tech_racoon" target="_blank">
    <img src="https://storage.ko-fi.com/cdn/kofi5.png" alt="Ko-fi" width="200"/>
  </a>  
  <a href="https://buymeacoffee.com/tech_racoon" target="_blank">
    <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me a Coffee" width="200"/>
  </a>
</p>

---

## ¿Quieres trastear tú mismo con la aplicación?

1. Clona el repositorio desde GitHub:

```bash
git clone https://github.com/naritsumi/EstudiaTAI-app.git
```

## Configuración del proyecto

Abre el proyecto con **Android Studio**.  
Necesitarás:  

- Android Studio actualizado (Versión utilizada 2025.1.3)
- Gradle configurado (Versión utilizada 8.8.0)
- JDK 11+ instalado  

---

### ¿Cómo agregar tus propias preguntas?

1. Ve a la ruta: 'app/src/main/assets/blockX/...'
2. Dentro de esa carpeta, crea archivos `.json` con la siguiente estructura (puedes crear tantos como quieras):  

```json
{
  "topicId": 1,
  "questions": [
    {
      "questionId": 1,
      "question": "¿Cuáles son las tres tareas básicas de un sistema informático?",
      "options": [
        "Almacenamiento, encriptación y salida",
        "Entrada, procesamiento y salida",
        "Cálculo, depuración y análisis",
        "Solo procesamiento"
      ],
      "correctAnswer": 1
    }
  ]
}
```

---

