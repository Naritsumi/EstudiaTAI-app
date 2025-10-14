# 📚 App de estudios TAI AGE

Esta es una aplicación de código abierto desarrollada en **Kotlin** como parte de mi portfolio.  
El objetivo es ayudar a preparar oposiciones, en concreto la **convocatoria AGE para Técnicos Auxiliares de Informática (TAI)**, a través de tests temáticos sobre el temario de la convocatoria 2024.

La aplicación está en constante mantenimiento, y las preguntas incluidas son de elaboración propia.  

---

## 📥 Descarga

La aplicación estará disponible en **F-Droid**:  

<a href="https://f-droid.org/" target="_blank">
  <img src="https://upload.wikimedia.org/wikipedia/commons/0/0d/Get_it_on_F-Droid.svg" alt="F-Droid" width="200"/>
</a>

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

